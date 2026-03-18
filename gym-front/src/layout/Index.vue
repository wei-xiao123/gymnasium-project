<template>
  <div class="full-screen">
    <el-container class="layout">
      <el-aside width="auto" class="asside">
        <menu-bar></menu-bar>
      </el-aside>
      <el-container>
        <el-header height="50px" class="header">
          <Header></Header>
        </el-header>
        <el-main class="main">
          <Tabs style="padding: 0px 20px"></Tabs>
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup lang="ts">
// Vue3 中组件引入后可以直接使用，无需注册
import Header from '@/layout/header/Header.vue'
import MenuBar from '@/layout/menu/MenuBar.vue'
import Tabs from './tabs/Tabs.vue'
</script>
<style scoped lang="scss">
.full-screen {
  width: 100vw;
  height: 100vh;
}
.layout {
  height: 100%;

  .asside {
    background-color: #304156;
  }

  .header {
    display: flex;
    align-items: center;
    background-color: #009688;
  }

  .main {
    padding-top: 1px;
    padding-left: 0 !important;
    padding-right: 0 !important;
  }
}
</style>