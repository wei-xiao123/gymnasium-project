import { ref, watch, type Ref } from 'vue'

/**
 * 创建一个自动持久化的 ref 对象
 * 用法: const state = usePersist('storeName', { key: '', value: 0 })
 */
export function usePersist<T>(storeName: string, initialValue: T) {
  // 尝试从 localStorage 恢复数据
  const savedData = localStorage.getItem(`pinia-${storeName}`)
  const state = ref<T>(savedData ? JSON.parse(savedData) : initialValue)

  // 监听状态变化，自动保存到 localStorage
  watch(
    state,
    (newVal) => {
      localStorage.setItem(`pinia-${storeName}`, JSON.stringify(newVal))
    },
    { deep: true }
  )

  return state
}

/**
 * 为整个 store 创建持久化功能
 * 用法: 在 store 中调用 persistStore('storeName', storeState)
 */
export function persistStore(storeName: string, storeObject: any) {
  const key = `pinia-${storeName}`

  // 从 localStorage 恢复
  const saved = localStorage.getItem(key)
  if (saved) {
    const parsedData = JSON.parse(saved)
    Object.assign(storeObject, parsedData)
  }

  // 监听所有属性变化并保存
  watch(
    () => storeObject,
    () => {
      localStorage.setItem(key, JSON.stringify(storeObject))
    },
    { deep: true }
  )
}

/**
 * 清除指定 store 的持久化数据
 */
export function clearPersist(storeName: string) {
  localStorage.removeItem(`pinia-${storeName}`)
}

/**
 * 清除所有 pinia 相关的持久化数据
 */
export function clearAllPersist() {
  const keys = Object.keys(localStorage)
  keys.forEach((key) => {
    if (key.startsWith('pinia-')) {
      localStorage.removeItem(key)
    }
  })
}
