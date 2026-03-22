import { defineStore } from "pinia"
import { usePersist } from "@/composables/usePersist"

// 定义store
export const userStore = defineStore('userStore', () => {
  // 使用 usePersist 自动持久化到 localStorage
  const userId = usePersist<string | number>('userStore-userId', '')
  const token = usePersist<string>('userStore-token', '')

  // getters
  const getUserId = () => userId.value
  const getToken = () => token.value

  // actions
  const setUserId = (id: string | number) => {
    userId.value = String(id)
  }

  const setToken = (t: string) => {
    token.value = t
  }

  return {
    userId,
    token,
    getUserId,
    getToken,
    setUserId,
    setToken,
  }
})