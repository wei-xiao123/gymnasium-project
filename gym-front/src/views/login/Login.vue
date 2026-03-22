<template>
  <div class="logincontainer">
    <el-form
      class="loginForm"
      :model="loginModel"
      ref="loginForm"
      :rules="rules"
      size="default"
    >
      <el-form-item class="form-item-center">
        <div class="loginTitle">系统登录</div>
      </el-form-item>

      <el-form-item>
        <el-input
          size="large"
          v-model="loginModel.username"
          placeholder="请输入账号"
        />
      </el-form-item>

      <el-form-item>
        <el-input
          size="large"
          v-model="loginModel.password"
          placeholder="请输入密码"
        />
      </el-form-item>

      <el-form-item>
        <el-row :gutter="20">
          <el-col :span="16">
            <el-input
              size="large"
              v-model="loginModel.code"
              placeholder="请输入验证码"
            />
          </el-col>
          <el-col :span="8">
            <img :src="imgSrc" class="image" @click="getImage" />
          </el-col>
        </el-row>
      </el-form-item>

      <el-form-item label="用户类型">
        <el-radio-group v-model="loginModel.userType">
          <el-radio label="1">会员</el-radio>
          <el-radio label="2">员工</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item>
        <el-row class="button-row">
          <el-col :span="12" class="col-left">
            <el-button
              class="button-full"
              size="large"
              type="primary"
              @click="onSubmit"
            >
              登录
            </el-button>
          </el-col>
          <el-col :span="12" class="col-right">
            <el-button type="danger" plain class="button-full" size="large">
              重置
            </el-button>
          </el-col>
        </el-row>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { reactive } from "vue"
import { ElMessage } from "element-plus"
import useImage from "@/composables/login/useImage"
import { loginApi } from "@/api/login/index"
import { useRouter } from "vue-router"
import { userStore } from "@/store/user"

const store = userStore()
const router = useRouter()
const { imgSrc, getImage } = useImage()

// 表单对象
const loginModel = reactive({
  username: "",
  password: "",
  code: "",
  userType: "",
})

// 表单验证规则
const rules = reactive({})

// 登录提交
const onSubmit = async () => {
  // 验证表单字段
  if (!loginModel.username || !loginModel.password || !loginModel.code || !loginModel.userType) {
    ElMessage.error("请填写完整的登录信息")
    return
  }

  try {
    const res = await loginApi(loginModel)
    if (res && res.code === 200) {
      // 保存登录信息
      store.setToken(res.data.token)
      store.setUserId(res.data.userId)

      ElMessage.success("登录成功")

      // 延迟跳转，确保状态已更新
      setTimeout(() => {
        router.push({ path: "/dashboard" })
      }, 500)
    } else {
      ElMessage.error(res?.msg || "登录失败")
    }
  } catch (error) {
    console.error("登录错误:", error)
    ElMessage.error("登录请求失败，请检查服务器连接")
  }
}
</script>

<style scoped lang="scss">
.logincontainer {
  background-color: #fff;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  background-image: url("../../assets/bg.png");
  background-size: 100% 100%;

  .loginForm {
    border-radius: 10px;
    background-color: #fff;
    width: 430px;
    height: 340px;
    padding: 20px 35px;

    .loginTitle {
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
    }
  }
}

.form-item-center {
  display: flex;
  justify-content: center;
}

.button-row {
  width: 100%;
}

.col-left {
  padding-right: 10px;
  padding-left: 0;
}

.col-right {
  padding-right: 0;
  padding-left: 10px;
}

.button-full {
  width: 100%;
}
</style>