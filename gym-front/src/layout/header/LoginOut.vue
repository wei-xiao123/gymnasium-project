<template>
<el-dropdown>
<span class="el-dropdown-link">
<img class="userimg" src="@/assets/user.jpg" />
</span>
<template #dropdown>
<el-dropdown-menu>
<el-dropdown-item @click="loginOut">退出登录</el-dropdown-item>
</el-dropdown-menu>
</template>
</el-dropdown>
</template>
<script setup lang="ts">
import useInstance from "@/hooks/useInstance"
import { userStore } from "@/store/user"
import { menuStore } from "@/store/menu"
import { useRouter } from "vue-router"

const { global } = useInstance()
const router = useRouter()
const ustore = userStore()
const mstore = menuStore()

const loginOut = async () => {
  const confirm = await global.$myconfirm("确定退出登录吗？")
  if (confirm) {
    // 清除动态路由
    mstore.clearRoutes(router)
    
    // 清空菜单信息
    mstore.menuList = []
    
    // 清空用户信息
    ustore.setToken("")
    ustore.setUserId("")
    ustore.setUserType("")
    
    window.location.href = "/login"
  }
}
</script>
<style scoped>
.userimg {
height: 38px;
width: 38px;
border-radius: 50%;
}
</style>