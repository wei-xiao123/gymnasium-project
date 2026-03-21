<template>
  <SysDialog
    :title="dialog.title"
    :height="dialog.height"
    :width="dialog.width"
    :visible="dialog.visible"
    @onConfirm="commit"
    @onClose="onClose"
  >
    <template v-slot:content>
      <el-form
        :model="addModel"
        ref="addFormRef"
        :rules="rules"
        label-width="80px"
        size="default"
      >
        <el-form-item prop="name" label="器材名称">
          <el-input v-model="addModel.name"></el-input>
        </el-form-item>
        <el-form-item prop="numTotal" label="器材数量">
          <el-input v-model="addModel.numTotal"></el-input>
        </el-form-item>
        <el-form-item prop="details" label="简介">
          <el-input type="textarea" v-model="addModel.details" />
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>
<script setup lang="ts">
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import type { MaterialType } from "@/api/material/MaterialModel";
import { nextTick, reactive, ref } from "vue";
import { ElMessage, type FormInstance } from "element-plus";
import { addApi, editApi } from "@/api/material/index";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";

const { global } = useInstance();

// 表单的 ref 属性
const addFormRef = ref<FormInstance>();

// 弹框属性
const { dialog, onClose: useDialogClose, onShow } = useDialog();

// 弹框绑定的对象
const addModel = reactive<MaterialType>({
  type: "",
  name: "",
  numTotal: "" as any,
  details: "",
  id: ""
});

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

// 定义方法给外部使用
const show = (type: string, row?: MaterialType) => {
  // 设置弹框属性
  type == EditType.ADD
    ? (dialog.title = Title.ADD)
    : (dialog.title = Title.EDIT);
  dialog.width = 900;
  dialog.height = 500;

  if (type == EditType.ADD) {
    // 新增模式：清空所有数据
    nextTick(() => {
      addModel.id = "";
      addModel.name = "";
      addModel.details = "";
      addModel.numTotal = "" as any;
      addFormRef.value?.resetFields();
    });
  } else if (type == EditType.EDIT) {
    // 编辑模式：回显数据
    global.$objCopy(row, addModel);
    // 清除表单验证错误
    nextTick(() => {
      addFormRef.value?.clearValidate();
    });
  }

  onShow();
  addModel.type = type;
};

// 暴露出去给外部调用
defineExpose({
  show
});

const validateNum = (rule: any, value: any, callback: any) => {
  if (value === "" || value === null || value === undefined || value == 0 || value < 0) {
    callback(new Error("请填写数量"));
  } else {
    callback();
  }
};

// 表单验证规则
const rules = reactive({
  name: [
    {
      required: true,
      message: "请输入器材名称",
      trigger: "blur"
    }
  ],
  numTotal: [
    {
      required: true,
      validator: validateNum,
      trigger: "blur"
    }
  ]
});

// 注册事件
const emits = defineEmits(["reFresh"]);
// 表单提交
const commit = () => {
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (addModel.type == EditType.ADD) {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
      if (res && res.code == 200) {
        emits("reFresh");
        // 信息提示
        ElMessage.success(res.msg);
        // 关闭弹框
        onClose();
      }
    }
  });
};
</script>
<style scoped></style>