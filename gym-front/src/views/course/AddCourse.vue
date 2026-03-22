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
          <div style="border: 1px solid #ccc; position: relative; z-index: 1;">
            <Toolbar
              style="border-bottom: 1px solid #ccc"
              :editor="editorRef"
              :defaultConfig="toolbarConfig"
              :mode="mode"
            />
            <Editor
              v-model="valueHtml"
              style="height: 300px; overflow-y: hidden;"
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
import { ElMessage, type FormInstance, type UploadFile } from "element-plus";
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
const { dialog, onClose, onShow } = useDialog();

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
type CourseForm = Omit<CourseType, "courseHour" | "coursePrice"> & {
  courseHour: number | null;
  coursePrice: number | null;
};

const resetAddModel = () => {
  addModel.type = "";
  addModel.courseId = "";
  addModel.courseName = "";
  addModel.image = "";
  addModel.teacherName = "";
  addModel.courseHour = null;
  addModel.courseDetails = "";
  addModel.coursePrice = null;
};

const show = async (type: string, row?: CourseType) => {
  dialog.title = type === EditType.ADD ? Title.ADD : Title.EDIT;
  dialog.width = 900;
  dialog.height = 500;

  await listTeacher();

  const upload = uploadRef.value;
  if (upload) {
    upload.clearFiles();
  }

  fileList.value = [];
  imgurl.value = "";

  if (type === EditType.ADD) {
    resetAddModel();
    valueHtml.value = "";
    const editor = editorRef.value;
    if (editor) {
      editor.clear();
    }
    addFormRef.value?.clearValidate();
  }

  if (type === EditType.EDIT) {
    nextTick(() => {
      global.$objCopy(row, addModel as CourseType);

      if (row?.image) {
        const obj: UploadFile = {
          name: "image",
          url: row.image,
          status: "success",
          uid: Date.now(),
        };
        fileList.value.push(obj);
        imgurl.value = row.image;
      }

      // 用 setTimeout 确保编辑器完全渲染后再设置内容
      setTimeout(() => {
        valueHtml.value = row?.courseDetails ?? "";
        const editor = editorRef.value;
        if (editor && typeof editor.focus === 'function') {
          editor.focus();
        }
      }, 100);

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
const addModel = reactive<CourseForm>({
  type: "", // 区分新增/编辑
  courseId: "",
  courseName: "",
  image: "",
  teacherName: "",
  courseHour: null,
  courseDetails: "",
  coursePrice: null,
});

const validateCourseHour = (_rule: any, value: any, callback: any) => {
  if (value == null || Number(value) <= 0) {
    callback(new Error("请填写课程课时"));
  } else {
    callback();
  }
};

const validateCoursePrice = (_rule: any, value: any, callback: any) => {
  if (value == null || Number(value) <= 0) {
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
        ? await addApi(addModel as CourseType)
        : await editApi(addModel as CourseType);
        
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