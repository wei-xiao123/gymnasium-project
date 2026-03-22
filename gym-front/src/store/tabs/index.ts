import { defineStore } from 'pinia'
import { usePersist } from '@/composables/usePersist'

// 定义选项卡数据类型
export type Tab = {
  title: string
  path: string
}

// 定义state的类型
export type TabState = {
  tabList: Tab[]
}

// 定义store
export const tabStore = defineStore('tabStore', () => {
  // 使用 usePersist 自动持久化到 localStorage
  const tabList = usePersist<Tab[]>('tabStore-tabList', [])

  // getter
  const getTabs = () => tabList.value

  // actions
  const addTab = (tab: Tab) => {
    // 判断是否已经添加数据
    if (tabList.value.some((item) => item.path === tab.path)) 
      return tabList.value.push(tab)
  }

  return {
    tabList,
    getTabs,
    addTab,
  }
})