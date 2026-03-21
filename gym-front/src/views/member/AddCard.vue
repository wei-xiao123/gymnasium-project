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
          <el-col :span="24">
            <el-form-item prop="cardType" label="类型">
              <el-radio-group v-model="addModel.cardType">
                <el-radio :label="'1'">天卡</el-radio>
                <el-radio :label="'2'">周卡</el-radio>
                <el-radio :label="'3'">月卡</el-radio>
                <el-radio :label="'4'">年卡</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="title" label="标题">
              <el-input v-model="addModel.title" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="cardDay" label="天数">
              <el-input v-model="addModel.cardDay" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="price" label="价格">
              <el-input v-model="addModel.price" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="status" label="状态">
              <el-radio-group v-model="addModel.status">
                <el-radio :label="'0'">停用</el-radio>
                <el-radio :label="'1'">启用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
</template>
<script setup lang="ts">
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import type { CardType } from "@/api/member_card/MemberModel";
import { reactive, ref } from "vue";
import { addApi, editApi } from "@/api/member_card/index";
import { ElMessage } from "element-plus";
import type { FormInstance } from "element-plus";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";

const { global } = useInstance();

// 表单 ref 属性
const addFormRef = ref<FormInstance>();

// 弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();

// 显示弹框
const show = (type: string, row?: CardType) => {
  dialog.height = 200;
  type == EditType.ADD
    ? (dialog.title = Title.ADD)
    : (dialog.title = Title.EDIT);

  if (type == EditType.ADD) {
    // 新增模式：清空所有字段
    Object.keys(addModel).forEach(key => {
      (addModel as any)[key] = "";
    });
  } else if (type == EditType.EDIT) {
    // 编辑模式：把要编辑的数据复制到表单对象
    global.$objCopy(row, addModel);
  }

  addModel.type = type;
  onShow();
};

// 暴露给父组件调用
defineExpose({
  show,
});

// 表单数据
const addModel = reactive<CardType>({
  type: "",
  title: "",
  cardType: "",
  cardId: "",
  price: "",
  cardDay: null as any,
  status: "",
});

// 表单验证规则
const rules = reactive({
  title: [
    {
      required: true,
      message: "请填写标题",
      trigger: "change",
    },
  ],
  cardType: [
    {
      required: true,
      message: "请选择类型",
      trigger: "change",
    },
  ],
  price: [
    {
      required: true,
      message: "请填写价格",
      trigger: "change",
    },
  ],
  cardDay: [
    {
      required: true,
      message: "请填写天数",
      trigger: "change",
    },
  ],
  status: [
    {
      required: true,
      message: "请选项状态",
      trigger: "change",
    },
  ],
});

// 注册事件
const emits = defineEmits<{
  refresh: [];
}>();

// 表单提交
const commit = () => {
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      const res = addModel.type == EditType.ADD
        ? await addApi(addModel)
        : await editApi(addModel);
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        emits("refresh");
        onClose();
      }
    }
  });
};
</script>
<style scoped></style>