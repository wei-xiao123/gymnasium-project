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
        ref="addRormRef"
        :rules="rules"
        label-width="80px"
        size="default"
      >
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="name" label="姓名">
              <el-input v-model="addModel.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="sex" label="性别">
              <el-radio-group v-model="addModel.sex">
                <el-radio :label="'0'">男</el-radio>
                <el-radio :label="'1'">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="phone" label="电话">
              <el-input v-model="addModel.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="age" label="年龄">
              <el-input v-model="addModel.age"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="birthday" label="生日">
              <el-date-picker
                v-model="addModel.birthday"
                type="date"
                placeholder="请选择生日"
                size="default"
                value-format="YYYY-MM-DD"
                format="YYYY-MM-DD"
                @change="calculateAge"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="joinTime" label="加入时间">
              <el-date-picker
                v-model="addModel.joinTime"
                type="date"
                placeholder="请选择加入时间"
                size="default"
                format="YYYY-MM-DD"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="height" label="身高">
              <el-input v-model="addModel.height"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="weight" label="体重">
              <el-input v-model="addModel.weight"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="waist" label="腰围">
              <el-input v-model="addModel.waist"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="roleId" label="角色">
              <el-select
                v-model="addModel.roleId"
                class="m-2"
                placeholder="请选择角色"
                size="default"
              >
                <el-option
                  v-for="item in roleData.list"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="status" label="状态">
              <el-radio-group v-model="addModel.status">
                <el-radio :label="'0'">停用</el-radio>
                <el-radio :label="'1'">启用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="12" :offset="0">
            <el-form-item prop="username" label="会员卡号">
              <el-input type="number" v-model="addModel.username"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12" :offset="0">
            <el-form-item prop="password" label="密码">
              <el-input v-model="addModel.password"></el-input>
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
import { reactive, ref } from "vue";
import type { MemberType } from "@/api/member/MemberModel";
import { ElMessage, type FormInstance } from "element-plus";
import { addApi, editApi } from "@/api/member/index";
import { EditType, Title } from "@/type/BaseEnum";
import useInstance from "@/hooks/useInstance";
import useSelectRole from "@/composables/user/useSelectRole";

const { global } = useInstance();
const addRormRef = ref<FormInstance>();

// 角色
const { roleData, listRole, roleMemberId, getMemberRole } =
  useSelectRole();

// 弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();

// 弹框显示
const show = async (type: string, row?: MemberType) => {
  await listRole();
  dialog.width = 680;
  dialog.height = 350;
  type == EditType.ADD
    ? (dialog.title = Title.ADD)
    : (dialog.title = Title.EDIT);

  // 新增模式：清空表单
  if (type == EditType.ADD) {
    // 重置表单数据和验证
    Object.keys(addModel).forEach((key) => {
      (addModel as any)[key] = "";
    });
    addRormRef.value?.clearValidate();
  } else if (type == EditType.EDIT && row) {
    // 编辑模式：复制数据到表单
    global.$objCopy(row, addModel);
    await getMemberRole(row!?.memberId);
    addModel.roleId = roleMemberId.value;
    addRormRef.value?.clearValidate();
  }

  addModel.type = type;
  onShow();
};

// 暴露给父组件调用
defineExpose({
  show,
});

// 表单绑定的数据对象
const addModel = reactive<MemberType>({
  type: "",
  memberId: "",
  name: "",
  sex: "",
  phone: "",
  age: "",
  birthday: "",
  height: "",
  weight: "",
  waist: "",
  joinTime: "",
  endTime: "",
  username: "",
  password: "",
  status: "",
  roleId: "",
});

// 表单验证规则
const rules = reactive({
  name: [
    {
      required: true,
      trigger: "change",
      message: "请填写姓名",
    },
  ],
  sex: [
    {
      required: true,
      trigger: "change",
      message: "请选择性别",
    },
  ],
  phone: [
    {
      required: true,
      trigger: "change",
      message: "请填写电话",
    },
  ],
  joinTime: [
    {
      required: true,
      trigger: "change",
      message: "请填写加入时间",
    },
  ],
  username: [
    {
      required: true,
      trigger: "change",
      message: "请填写会员卡号",
    },
  ],
  password: [
    {
      required: true,
      trigger: "change",
      message: "请填写密码",
    },
  ],
  status: [
    {
      required: true,
      trigger: "change",
      message: "请选择状态",
    },
  ],
});

// 根据生日计算年龄
const calculateAge = () => {
  if (addModel.birthday) {
    const birthDate = new Date(addModel.birthday);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDiff = today.getMonth() - birthDate.getMonth();
    // 如果这年生日还没到，年龄减 1
    if (
      monthDiff < 0 ||
      (monthDiff === 0 && today.getDate() < birthDate.getDate())
    ) {
      age--;
    }
    addModel.age = age.toString();
  }
};

// 注册事件
const emits = defineEmits(["refresh"]);

// 表单提交
const commit = () => {
  addRormRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null;
      if (addModel.type == EditType.ADD) {
        res = await addApi(addModel);
      } else {
        res = await editApi(addModel);
      }
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