<template>
  <SysDialog
    :title="dialog.title"
    :height="dialog.height"
    :width="dialog.width"
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
          <el-col :span="12">
            <el-form-item prop="courseName" label="课程名称">
              <el-input v-model="addModel.courseName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="courseHour" label="课程课时">
              <el-input v-model="addModel.courseHour" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="coursePrice" label="课程价格">
              <el-input v-model="addModel.coursePrice" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="teacherName" label="教练">
              <el-select
                v-model="addModel.teacherName"
                style="width: 100%"
                class="m-2"
                placeholder="请选择教练"
                size="default"
              >
                <el-option
                  v-for="item in teacherData.list"
                  :key="item.label"
                  :label="item.label"
                  :value="item.label"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="课程封面">
          <el-upload
            ref="uploadRef"
            action="#"
            :on-change="uploadFile"
            :auto-upload="false"
            :limit="1"
            :file-list="fileList"
            list-type="picture-card"
          >
            <el-icon><Plus /></el-icon>
            <template #file="{ file }">
              <div>
                <img
                  class="el-upload-list__item-thumbnail"
                  ref="imageSrc"
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
                    @click="handleRemove(file)"
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
        <el-form-item prop="courseDetails" label="课程详情">
          <div style="border: 1px solid #ccc">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              v-model="valueHtml"
              style="height: 300px; overflow-y: hidden"
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
import "@wangeditor/editor/dist/css/style.css";
import { Delete, Plus, ZoomIn } from "@element-plus/icons-vue";
// @ts-ignore
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import type { CourseType } from "@/api/course/CourseModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { ElMessage } from "element-plus";
import type { FormInstance } from "element-plus";
import { nextTick, reactive, ref } from "vue";
import useUpload from "@/composables/course/useUpload";
import useEditor from "@/composables/course/useEditor";
import useSelectTeacher from "@/composables/course/useSelectTeacher";
import { EditType, Title } from "@/type/BaseEnum";
import { addApi, editApi } from "@/api/course/index";
import useInstance from "@/hooks/useInstance";

const { global } = useInstance();

// 表单的 ref 属性
const addFormRef = ref<FormInstance>();

// 弹框属性
const { dialog, onClose: useDialogClose, onShow } = useDialog();

// 自定义关闭函数 - 支持关闭和刷新
const onClose = () => {
  useDialogClose();
  // 编辑模式关闭时，100ms后刷新页面
  if (addModel.type == EditType.EDIT) {
    setTimeout(() => {
      window.location.reload();
    }, 100);
  }
};

// 教练选择
const { teacherData, listTeacher } = useSelectTeacher();

// 图片上传
const {
  uploadRef,
  dialogImageUrl,
  dialogVisible,
  disabled,
  handleRemove,
  handlePictureCardPreview,
  fileList,
  uploadFile,
  imgurl
} = useUpload();

// 文本编辑器
const {
  editorRef,
  handleCreated,
  mode,
  editorConfig,
  valueHtml,
  toolbarConfig
} = useEditor();

// 显示弹框
const show = async (type: string, row?: CourseType) => {
  // 弹框属性
  type == EditType.ADD
    ? (dialog.title = Title.ADD)
    : (dialog.title = Title.EDIT);
  dialog.width = 900;
  dialog.height = 500;

  // 获取教练数据列表
  await listTeacher();
  
  // 清空上传组件
  const upload = uploadRef.value;
  if (upload) {
    upload.clearFiles();
  }
  
  if (type == EditType.ADD) {
    // 新增模式：清空所有数据
    fileList.value = [];
    const editor = editorRef.value;
    if (editor) {
      editor.clear();
    }
    // 确保在 nextTick 中清空所有字段
    nextTick(() => {
      addModel.courseId = "";
      addModel.courseName = "";
      addModel.courseDetails = "";
      addModel.courseHour = "" as any;
      addModel.coursePrice = "" as any;
      addModel.teacherName = "";
      addModel.image = "";
      valueHtml.value = "";
      addFormRef.value?.resetFields();
    });
  } else if (type == EditType.EDIT) {
    // 编辑模式：回显数据
    // 先复制数据
    global.$objCopy(row, addModel);
    // 设置文本编辑器数据
    valueHtml.value = addModel.courseDetails;
    // 清空并回显封面图
    fileList.value = [];
    nextTick(() => {
      if (row?.image) {
        const obj: any = {
          name: "",
          url: "",
          status: "success" as any,
          uid: Date.now()
        };
        obj.url = row.image;
        fileList.value.push(obj);
      }
    });
    // 清除表单验证错误
    nextTick(() => {
      addFormRef.value?.clearValidate();
    });
  }
  
  onShow();
  addModel.type = type;
};

// 暴露出去，给外部组件使用
defineExpose({
  show
});

// 表单绑定的数据对象
const addModel = reactive<CourseType>({
  type: "", // 区分新增/编辑
  courseId: "",
  courseName: "",
  image: "",
  teacherName: "",
  courseHour: "" as any,
  courseDetails: "",
  coursePrice: "" as any
});

const validateCourseHour = (_rule: any, value: any, callback: any) => {
  if (value === "" || value === null || value === undefined) {
    callback(new Error("请填写课程课时"));
  } else if (Number(value) <= 0) {
    callback(new Error("请填写课程课时"));
  } else {
    callback();
  }
};

const validateCoursePrice = (_rule: any, value: any, callback: any) => {
  if (value === "" || value === null || value === undefined) {
    callback(new Error("请填写课程价格"));
  } else if (Number(value) <= 0) {
    callback(new Error("请填写课程价格"));
  } else {
    callback();
  }
};

// 表单验证规则
const rules = reactive({
  courseName: [
    {
      required: true,
      trigger: "blur",
      message: "请输入课程名称"
    }
  ],
  image: [
    {
      required: true,
      trigger: "blur",
      message: "请上传课程图片"
    }
  ],
  teacherName: [
    {
      required: true,
      trigger: "blur",
      message: "请选择课程教练"
    }
  ],
  courseDetails: [
    {
      required: true,
      trigger: "blur",
      message: "请填写课程详情"
    }
  ],
  coursePrice: [
    {
      required: true,
      validator: validateCoursePrice,
      trigger: "blur"
    }
  ],
  courseHour: [
    {
      required: true,
      validator: validateCourseHour,
      trigger: "blur"
    }
  ]
});

// 注册事件
const emits = defineEmits<{
  reFresh: [];
}>();

// 表单提交
const commit = () => {
  // 课程图片地址
  addModel.image = imgurl.value;
  // 课程详情
  addModel.courseDetails = valueHtml.value;
  // 转换课时和价格为数字
  addModel.courseHour = Number(addModel.courseHour);
  addModel.coursePrice = Number(addModel.coursePrice);
  
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      const res = addModel.type == EditType.ADD
        ? await addApi(addModel)
        : await editApi(addModel);
        
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