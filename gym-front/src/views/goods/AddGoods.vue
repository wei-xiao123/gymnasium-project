<template>
  <SysDialog
    :title="dialog.title"
    :width="dialog.width"
    :height="dialog.height"
    :visible="dialog.visible"
    @onClose="onClose"
    @onConfirm="commit"
  >
    <template v-slot:content>
      <el-form
        :model="addModel"
        ref="addFormRef"
        :rules="rules"
        label-width="80px"
        size="default"
      >
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="name" label="商品名称">
              <el-input v-model="addModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="price" label="商品价格">
              <el-input v-model="addModel.price"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="unit" label="单位">
              <el-input v-model="addModel.unit"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="specs" label="规格">
              <el-input v-model="addModel.specs"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="store" label="库存">
              <el-input v-model="addModel.store"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item prop="image" label="商品图片">
          <el-input v-model="addModel.image" style="display: none"></el-input>
          <el-upload
            ref="uploadRef"
            action="#"
            :on-change="handleUploadChange"
            :on-remove="handleRemove"
            :on-exceed="handleExceed"
            list-type="picture-card"
            :auto-upload="false"
            :file-list="fileList"
            :limit="5"
            multiple
          >
            <el-icon><Plus /></el-icon>

            <template #file="{ file }">
              <div>
                <img
                  class="el-upload-list__item-thumbnail"
                  :src="file.url"
                  alt=""
                />
                <span class="el-upload-list__item-actions">
                  <span
                    class="el-upload-list__item-preview"
                    @click="handlePictureCardPreview(file)"
                  >
                    <el-icon><zoom-in /></el-icon>
                  </span>
                  <span
                    v-if="!disabled"
                    class="el-upload-list__item-delete"
                    @click="removeUploadFile(file)"
                  >
                    <el-icon><Delete /></el-icon>
                  </span>
                </span>
              </div>
            </template>
          </el-upload>

          <el-dialog v-model="dialogVisible">
            <img w-full :src="dialogImageUrl" alt="Preview Image" />
          </el-dialog>
        </el-form-item>
        <el-form-item prop="details" label="商品详情">
          <div style="border: 1px solid #ccc">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              style="height: 300px; overflow-y: hidden"
              v-model="valueHtml"
              :defaultConfig="editorConfig"
              :mode="mode"
              @onCreated="handleCreated"
            />
          </div>
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import "@wangeditor/editor/dist/css/style.css"; // 引入 css
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import {type GoodsType } from "@/api/goods/GoodsModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage,type FormInstance } from "element-plus";
import { nextTick, reactive, ref } from "vue";
import useEditor from "@/composables/course/useEditor";
import { addApi, editApi } from "@/api/goods/index";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";
import { uploadImageApi } from "@/api/course";
import { normalizeImageUrl, splitImageUrls } from "@/utils/imageUrl";
const { global } = useInstance();

const uploadRef = ref();
const dialogImageUrl = ref("");
const dialogVisible = ref(false);
const disabled = ref(false);
const fileList = ref<any[]>([]);
const imageUrls = ref<string[]>([]);
const latestUploadedUrls = ref<string[]>([]);
const uploadingCount = ref(0);

const handlePictureCardPreview = (file: any) => {
  dialogImageUrl.value = file.url || "";
  dialogVisible.value = true;
};

const handleRemove = (file: any) => {
  const rawUrl = file?.responseUrl || file?.url || "";
  const normalizedRaw = normalizeImageUrl(rawUrl);
  imageUrls.value = imageUrls.value.filter(
    (item) => item !== rawUrl && normalizeImageUrl(item) !== normalizedRaw
  );
  latestUploadedUrls.value = latestUploadedUrls.value.filter(
    (item) => item !== rawUrl && normalizeImageUrl(item) !== normalizedRaw
  );
  fileList.value = fileList.value.filter((item: any) => {
    if (file?.uid && item?.uid) {
      return item.uid !== file.uid;
    }
    const itemUrl = item?.responseUrl || item?.url || "";
    return normalizeImageUrl(itemUrl) !== normalizedRaw;
  });
  addModel.image = collectImageUrls().join(",");
};

const removeUploadFile = (file: any) => {
  uploadRef.value?.handleRemove?.(file);
  handleRemove(file);
};

const handleUploadChange = async (file: any) => {
  const raw = file?.raw;
  if (!raw) {
    return;
  }
  const typeArr = ["image/png", "image/gif", "image/jpeg", "image/jpg"];
  const isImg = typeArr.includes(raw.type);
  const isLessThan3M = raw.size / 1024 / 1024 < 3;
  if (!isImg) {
    ElMessage.warning("只能上传图片类型!");
    return;
  }
  if (!isLessThan3M) {
    ElMessage.warning("图片大小不能超过3M!");
    return;
  }
  uploadingCount.value += 1;
  try {
    const formData = new FormData();
    formData.append("file", raw);
    const res = await uploadImageApi(formData);
    if (res && res.code == 200 && res.data) {
      const uploaded = res.data.msg || res.data.url || res.data.path || res.data;
      // 保持本地预览地址，避免对象存储私有策略导致回显失败
      file.url = file.url || URL.createObjectURL(raw);
      file.responseUrl = uploaded;
      // 新上传图片置顶，列表首图会立即变更
      imageUrls.value = [uploaded, ...imageUrls.value.filter((item) => item !== uploaded)];
      latestUploadedUrls.value = [uploaded, ...latestUploadedUrls.value.filter((item) => item !== uploaded)];
      addModel.image = collectImageUrls().join(",");
      addFormRef.value?.clearValidate("image");
      ElMessage.success("图片上传成功!");
    }
  } catch (e) {
    ElMessage.error("图片上传失败，请检查图片服务");
  } finally {
    uploadingCount.value -= 1;
  }
};

const collectImageUrls = (): string[] => {
  const fromFileList = (fileList.value || [])
    .map((item: any) => item?.responseUrl || item?.url || "")
    .map((item: string) => item.trim())
    .filter((item: string) => !!item)
    .filter((item: string) => !item.startsWith("blob:"))
    .map((item: string) =>
      item
        .replace("http://localhost:8088", "http://localhost:8089")
        .replace("http://127.0.0.1:8088", "http://127.0.0.1:8089")
    );
  return Array.from(new Set([...(imageUrls.value || []), ...fromFileList]));
};

const handleExceed = async (files: any[]) => {
  ElMessage.warning("最多上传5张商品图");
};
//文本编辑器
const {
  editorRef,
  handleCreated,
  mode,
  editorConfig,
  valueHtml,
  toolbarConfig,
} = useEditor();
//表单ref属性
const addFormRef = ref<FormInstance>();
//弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();
//定义外部使用的方法
const show = (type: string, row?: GoodsType) => {
  onShow();
  addFormRef.value?.resetFields();
  //清空图片数据
  fileList.value = [];
  imageUrls.value = [];
  latestUploadedUrls.value = [];
  //设置弹框属性
  type == EditType.ADD
    ? (dialog.title = Title.ADD)
    : (dialog.title = Title.EDIT);
  dialog.width = 900;
  dialog.height = 500;
  //清空图片和文本编辑器
  const upload = uploadRef.value;
  if (upload) {
    upload.clearFiles();
  }
  // addModel.image = "";
  if (type == EditType.ADD) {
    addModel.goodsId = "";
    addModel.name = "";
    addModel.price = "";
    addModel.unit = "";
    addModel.specs = "";
    addModel.store = "";
    addModel.image = "";
    addModel.details = "";
    const editor = editorRef.value;
    if (editor) {
      editor.clear();
    }
    valueHtml.value = "";
  }
  // addModel.details = "";
  //编辑数据回显
  if (type == EditType.EDIT) {
    nextTick(() => {
      global.$objCopy(row, addModel);
      //图片回显
      if (row?.image) {
        const urls = splitImageUrls(addModel.image);
        imageUrls.value = [...urls];
        fileList.value = urls.map((url, index) => ({
          name: `image-${index}`,
          url: normalizeImageUrl(url),
          responseUrl: url,
          uid: `echo-${index}`,
        }));
      }
    });
  }
  if (row && row.details) {
    //文本编辑器的回显
    valueHtml.value = row.details;
  }
  addFormRef.value?.clearValidate();
  addModel.type = type;
};
//暴露出去
defineExpose({
  show,
});
//表单绑定的对象
const addModel = reactive<GoodsType>({
  type: "",
  goodsId: "",
  name: "",
  image: "",
  details: "",
  unit: "",
  specs: "",
  price: "",
  store: "",
});
const validatePrice = (rule: any, value: any, callback: any) => {
  if (value === "" || value === null || value === undefined) {
    callback(new Error("请填写商品价格"));
    return;
  }
  const price = Number(value);
  if (Number.isNaN(price) || price <= 0) {
    callback(new Error("请填写商品价格"));
  } else {
    callback();
  }
};
const validateStore = (rule: any, value: any, callback: any) => {
  if (value === "" || value === null || value === undefined) {
    callback(new Error("请填写商品库存"));
    return;
  }
  const store = Number(value);
  if (Number.isNaN(store) || store <= 0) {
    callback(new Error("请填写商品库存"));
  } else {
    callback();
  }
};
//表单验证规则
const rules = reactive({
  name: [
    {
      required: true,
      trigger: "blur",
      message: "请输入商品名称",
    },
  ],
  image: [
    {
      trigger: ["change", "blur"],
      validator: (_rule: any, value: any, callback: any) => {
        if (!value || String(value).trim().length === 0) {
          callback(new Error("请上传商品图片"));
        } else {
          callback();
        }
      },
    },
  ],
  unit: [
    {
      required: true,
      trigger: "blur",
      message: "请填写单位",
    },
  ],
  details: [
    {
      required: true,
      trigger: "blur",
      message: "请填写商品详情",
    },
  ],
  specs: [
    {
      required: true,
      trigger: "blur",
      message: "请填写商品规格",
    },
  ],
  price: [
    {
      required: true,
      validator: validatePrice,
      trigger: "blur",
    },
  ],
  store: [
    {
      required: true,
      validator: validateStore,
      trigger: "blur",
    },
  ],
});
//注册事件
const emits = defineEmits(["reFresh"]);
//提交表单
const commit = () => {
  if (uploadingCount.value > 0) {
    ElMessage.warning("图片上传中，请稍后再提交");
    return;
  }
  // 优先使用当前上传列表，其次回退已存在的image字段
  const currentImages = collectImageUrls();
  const latestFromFileList = (fileList.value || [])
    .filter((item: any) => {
      const uid = String(item?.uid || "");
      return !!item?.responseUrl && !uid.startsWith("echo-");
    })
    .map((item: any) => item.responseUrl)
    .filter((item: string) => !!item);

  if (currentImages.length > 0) {
    const latest = Array.from(
      new Set([
        ...latestFromFileList,
        ...latestUploadedUrls.value.filter((item) => currentImages.includes(item)),
      ])
    );
    const rest = currentImages.filter((item) => !latest.includes(item));
    imageUrls.value = [...latest, ...rest];
  } else if (addModel.image) {
    imageUrls.value = splitImageUrls(addModel.image);
  } else {
    imageUrls.value = [];
  }
  addModel.image = imageUrls.value.join(",");
  if (!addModel.image) {
    ElMessage.error("请上传商品图片");
    return;
  }
  //商品详情
  addModel.details = valueHtml.value;
  addFormRef.value?.clearValidate("image");
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      let res: any;
      if (addModel.type == EditType.ADD) {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        emits("reFresh");
        onClose();
      }
    }
  });
};
</script>

<style scoped></style>