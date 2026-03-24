import { defineStore } from "pinia"
import { getInfoApi } from "@/api/login"
import { usePersist } from "@/composables/usePersist"

// 定义 store
export const userStore = defineStore("userStore", () => {
  // 使用 usePersist 创建持久化状态
  const userId = usePersist("userId", "")
  const token = usePersist("token", "")
  const userType = usePersist("userType", "")
  const codeList = usePersist("codeList", [])

  /**
   * 设置用户 ID
   */
  const setUserId = (id: string) => {
    userId.value = id
  }

  /**
   * 设置 token
   */
  const setToken = (t: string) => {
    token.value = t
  }

  /**
   * 设置用户类型
   */
  const setUserType = (type: string) => {
    userType.value = type
  }

  /**
   * 获取用户 ID
   */
  const getUserId = () => userId.value

  /**
   * 获取 token
   */
  const getToken = () => token.value

  /**
   * 获取用户类型
   */
  const getUserType = () => userType.value

  /**
   * 获取用户信息和权限
   */
  const getInfo = () => {
    return new Promise((resolve, reject) => {
      getInfoApi({ userType: userType.value, userId: userId.value })
        .then((res: any) => {
          if (res && res.code === 200) {
            codeList.value = res.data.permissons
          }
          resolve(codeList.value)
        })
        .catch((error: any) => {
          reject(error)
        })
    })
  }

  return {
    userId,
    token,
    userType,
    codeList,
    setUserId,
    setToken,
    setUserType,
    getUserId,
    getToken,
    getUserType,
    getInfo,
  }
})