<template>
  <el-icon class="icons" @click="setCollapse">
    <component :is="status ? Fold : Expand" />
  </el-icon>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { Fold, Expand } from '@element-plus/icons-vue'
import { collapseStore } from '@/store/collapse/index'

// 获取 store
const store = collapseStore()

// 获取 collapse 状态
const status = computed(() => {
  return !store.getCollapse
})

// 改变 collapse
const setCollapse = () => {
  store.setCollapse(!store.getCollapse)
}
</script>

<style scoped lang="scss">
.icons {
  color: #fff;
  font-size: 24px;
}
</style>