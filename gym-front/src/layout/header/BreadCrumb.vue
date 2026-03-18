<template>
  <el-breadcrumb class="bred" separator="/">
    <el-breadcrumb-item v-for="item in tabs" :key="item.path">
      {{ item.meta?.title }}
    </el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import type { Ref } from 'vue'
import { useRoute } from 'vue-router'
import type { RouteLocationMatched } from 'vue-router'

// 面包屑数据
const tabs: Ref<RouteLocationMatched[]> = ref([])

// 获取当前路由
const route = useRoute()

const getBreadcrumb = () => {
  // 从路由中获取所有有 meta.title 的项
  let matched = route.matched.filter((item) => item.meta && item.meta.title)

  // 判断第一个是否是首页，不是则构造一个
  const first = matched[0]
  if (first && first.path !== '/dashboard') {
    matched = [
      {
        path: '/dashboard',
        meta: { title: '首页' },
        components: {},
        redirect: undefined,
        name: undefined,
      } as unknown as RouteLocationMatched,
    ].concat(matched)
  }

  tabs.value = matched
}

getBreadcrumb()

watch(
  () => route.path,
  () => getBreadcrumb()
)
</script>

<style scoped lang="scss">
/* 修改面包屑字体颜色 */
:deep(.el-breadcrumb__inner) {
  color: #f2eaea !important;
}

.bred {
  margin-left: 20px;
}

/* 修改字体大小 (注释) */
/* :deep(.el-breadcrumb__item) {
  font-size: 16px !important;
} */
</style>