<template>
  <el-dropdown>
    <span class="el-dropdown-link">
      <img class="userimg" src="@/assets/user.jpg" />
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item @click="updateBtn">修改密码</el-dropdown-item>
        <el-dropdown-item @click="loginOut">退出登录</el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
  <!-- 修改密码弹框 -->
  <SysDialog
    :title="dialog.title"
    :width="dialog.width"
    :height="dialog.height"
    :visible="dialog.visible"
    @onClose="onClose"
    @onConfirm="commit"
  >
    <template #content>
      <el-form
        :model="param"
        ref="upForm"
        :rules="rules"
        label-width="80px"
        size="default"
      >
        <el-form-item prop="oldPassword" label="原密码">
          <el-input v-model="param.oldPassword"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="新密码">
          <el-input v-model="param.password"></el-input>
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>
<script setup lang="ts">
import type { ResetPasswordParam } from "@/api/home/HomeModel";
import type { FormInstance } from "element-plus";
import { ElMessage } from "element-plus";
import { reactive, ref } from "vue";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import useInstance from "@/hooks/useInstance";
import { updatePasswordApi } from "@/api/home";
import { userStore } from "@/store/user";

const { dialog, onClose, onConfirm, onShow } = useDialog();
const { global } = useInstance();
const upForm = ref<FormInstance>();
const store = userStore();

// 退出登录
const loginOut = async () => {
  const confirm = await global.$myconfirm("确定退出登录吗？");
  if (confirm) {
    store.setToken("");
    store.setUserId("");
    store.setUserType("");
    localStorage.clear();
    window.location.href = "/login";
  }
};

// 修改密码按钮
const updateBtn = () => {
  dialog.height = 150;
  dialog.title = "修改密码";
  onShow();
};

// 表单参数
const param = reactive<ResetPasswordParam>({
  userId: store.getUserId,
  userType: store.getUserType,
  oldPassword: "",
  password: "",
});

// 表单验证规则
const rules = reactive({
  oldPassword: [
    {
      required: true,
      trigger: "blur",
      message: "原密码不能为空!",
    },
  ],
  password: [
    {
      required: true,
      trigger: "blur",
      message: "新密码不能为空!",
    },
  ],
});

// 确认修改
const commit = () => {
  upForm.value?.validate(async (valid) => {
    if (valid) {
      const res = await updatePasswordApi(param);
      if (res && res.code === 200) {
        ElMessage.success(res.msg);
        store.setToken("");
        store.setUserId("");
        store.setUserType("");
        localStorage.clear();
        window.location.href = "/login";
      }
    }
  });
};
</script>
<style scoped>
.userimg {
  height: 38px;
  width: 38px;
  border-radius: 50%;
}
</style>