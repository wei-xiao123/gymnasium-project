<template>
  <div class="registercontainer">
    <el-form
      :model="registerModel"
      class="registerform"
      ref="registerRef"
      :rules="rules"
      size="default"
    >
      <el-form-item>
        <div class="registertitle">会员注册</div>
      </el-form-item>
      <el-form-item prop="username">
        <el-input
          size="large"
          v-model="registerModel.username"
          placeholder="请输入账号"
        ></el-input>
      </el-form-item>
      <el-form-item prop="name">
        <el-input
          size="large"
          v-model="registerModel.name"
          placeholder="请输入姓名"
        ></el-input>
      </el-form-item>
      <el-form-item prop="phone">
        <el-input
          size="large"
          v-model="registerModel.phone"
          placeholder="请输入手机号"
        ></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          type="password"
          size="large"
          v-model="registerModel.password"
          placeholder="请输入密码"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
          type="password"
          size="large"
          v-model="registerModel.confirmPassword"
          placeholder="请确认密码"
          show-password
        ></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-row :gutter="20" style="width: 100%">
          <el-col :span="16" :offset="0">
            <el-input
              size="large"
              v-model="registerModel.code"
              placeholder="请输入验证码"
            ></el-input>
          </el-col>
          <el-col :span="8" :offset="0">
            <img :src="imgSrc" class="image" @click="getImage" />
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item>
        <el-row style="width: 100%">
          <el-col :span="12" :offset="0" style="padding-right: 10px; padding-left: 0px">
            <el-button size="large" style="width: 100%" type="primary" @click="onSubmit">注册</el-button>
          </el-col>
          <el-col :span="12" :offset="0" style="padding-right: 0px; padding-left: 10px">
            <el-button size="large" style="width: 100%" type="danger" plain @click="onReset">重置</el-button>
          </el-col>
        </el-row>
      </el-form-item>
      <el-form-item>
        <el-button link type="primary" @click="toLogin">已有账号？去登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import useImage from "@/composables/login/useImage";
import { reactive, ref } from "vue";
import { registerApi } from "@/api/login";
import { ElMessage, type FormInstance } from "element-plus";
import { useRouter } from "vue-router";

const router = useRouter();
const registerRef = ref<FormInstance>();
const { imgSrc, getImage } = useImage();

const registerModel = reactive({
  username: "",
  password: "",
  confirmPassword: "",
  name: "",
  phone: "",
  code: "",
});

const validateConfirmPassword = (_rule: any, value: string, callback: any) => {
  if (!value) {
    callback(new Error("请确认密码"));
    return;
  }
  if (value !== registerModel.password) {
    callback(new Error("两次密码不一致"));
    return;
  }
  callback();
};

const rules = reactive({
  username: [{ required: true, trigger: "blur", message: "请填写账号" }],
  name: [{ required: true, trigger: "blur", message: "请填写姓名" }],
  phone: [{ required: true, trigger: "blur", message: "请填写手机号" }],
  password: [{ required: true, trigger: "blur", message: "请填写密码" }],
  confirmPassword: [{ required: true, trigger: "blur", validator: validateConfirmPassword }],
  code: [{ required: true, trigger: "blur", message: "请填写验证码" }],
});

const onSubmit = () => {
  registerRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        const res = await registerApi(registerModel);
        if (res && res.code === 200) {
          ElMessage.success(res.msg || "注册成功");
          router.push({ path: "/login" });
        } else {
          await getImage();
          registerModel.code = "";
        }
      } catch (e) {
        registerModel.code = "";
        await getImage();
      }
    }
  });
};

const onReset = async () => {
  registerModel.username = "";
  registerModel.password = "";
  registerModel.confirmPassword = "";
  registerModel.name = "";
  registerModel.phone = "";
  registerModel.code = "";
  registerRef.value?.clearValidate();
  await getImage();
};

const toLogin = () => {
  router.push({ path: "/login" });
};
</script>

<style scoped lang="scss">
.registercontainer {
  background-color: #fff;
  height: 100%;
  background-image: url("../../assets/bg.png");
  background-size: 100% 100%;
  display: flex;
  justify-content: center;
  align-items: center;

  .registerform {
    border-radius: 10px;
    background-color: #fff;
    width: 460px;
    padding: 20px 35px;

    .registertitle {
      width: 100%;
      font-size: 24px;
      font-weight: 600;
      display: flex;
      justify-content: center;
      align-items: center;
    }

    .image {
      height: 40px;
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      cursor: pointer;
    }
  }
}
</style>