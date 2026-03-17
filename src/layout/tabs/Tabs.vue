<template>
  <el-tabs
    v-model="activeTab"
    @tab-click="clickBtn"
    type="card"
    closable
    @tab-remove="removeTab"
  >
    <el-tab-pane
      v-for="item in tabsList"
      :key="item.path"
      :label="item.title"
      :name="item.path"
    ></el-tab-pane>
  </el-tabs>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import type { TabPaneName } from 'element-plus'
import { tabStore } from '@/store/tabs'
import type { Tab } from '@/store/tabs/index'

const route = useRoute()
const router = useRouter()
const store = tabStore()

// 获取 tabs 数据
const tabsList = computed(() => {
  return store.getTabs
})

// 当前激活的选项卡
const activeTab = ref('')

const setActiveTab = () => {
  activeTab.value = route.path
}

// 删除选项卡
const removeTab = (targetName: TabPaneName) => {
  if (targetName === '/dashboard') return

  const tabs = tabsList.value
  let activeName = activeTab.value

  if (activeName === targetName) {
    tabs.forEach((tab: Tab, index: number) => {
      if (tab.path === targetName) {
        const nextTab = tabs[index + 1] || tabs[index - 1]
        if (nextTab) {
          activeName = nextTab.path
        }
      }
    })
  }

  activeTab.value = activeName
  store.tabList = tabs.filter((tab: Tab) => tab.path !== targetName)
  router.push({ path: activeName })
}

// 添加选项卡
const addTab = () => {
  const { path, meta } = route
  const tab: Tab = {
    path: path,
    title: meta.title as string,
  }
  store.addTab(tab)
}

// 监听路由的变化
watch(
  () => route.path,
  () => {
    setActiveTab()
    addTab()
  }
)

// 解决刷新数据丢失的问题
const handleRefresh = () => {
  if (route.path !== '/login') {
    window.addEventListener('beforeunload', () => {
      sessionStorage.setItem('tabsView', JSON.stringify(tabsList.value))
    })

    const tabsSession = sessionStorage.getItem('tabsView')
    if (tabsSession) {
      const oldTabs = JSON.parse(tabsSession)
      if (oldTabs.length > 0) {
        store.tabList = oldTabs
      }
    }
  }
}

onMounted(() => {
  handleRefresh()
  setActiveTab()
  addTab()
})

// 选项卡点击事件
const clickBtn = (tab: any) => {
  const { props } = tab
  router.push({ path: props.name })
}
</script>

<style scoped lang="scss">
:deep(.el-tabs__header) {
  margin: 0;
}

:deep(.el-tabs__item) {
  height: 26px !important;
  line-height: 26px !important;
  text-align: center !important;
  border: 1px solid #d8dce5 !important;
  margin: 0 3px !important;
  color: #495060;
  font-size: 12px !important;
  padding: 0 10px !important;
}

:deep(.el-tabs__nav) {
  border: none !important;
}

:deep(.is-active) {
  border-bottom: 1px solid transparent !important;
  border: 1px solid #42b983 !important;
  background-color: #42b983 !important;
  color: #fff !important;
}

:deep(.el-tabs__item:hover) {
  color: #495060 !important;
}

:deep(.is-active:hover) {
  color: #fff !important;
}

:deep(.el-tabs__nav-next) {
  line-height: 26px !important;
}

:deep(.el-tabs__nav-prev) {
  line-height: 26px !important;
}
</style>