import type { DirectiveBinding } from 'vue'
import { userStore } from '@/store/user'

/**
 * 权限指令 - 根据用户权限显示或隐藏元素
 * 使用方式: v-permission="['sys:user:add', 'sys:user:edit']"
 * 如果用户拥有列表中的任何一个权限，则显示该元素
 */
export default {
  mounted(el: HTMLElement, binding: DirectiveBinding<string[]>) {
    const { value } = binding

    // 如果没有传入权限值，则显示元素
    if (!value || !Array.isArray(value) || value.length === 0) {
      return
    }

    // 获取当前用户权限信息（来自后端返回的权限列表）
    // 注意：这里假设权限信息存储在 localStorage 中或从后端获取
    // 由于目前还没有权限管理功能，所以默认显示所有有 v-permission 的元素
    // 如需启用权限控制，需要在登录时从后端获取用户权限并存储到 userStore

    const store = userStore()
    const userPermissions = store.userId // 临时使用 userId 作为占位符
    
    // TODO: 实现权限检查逻辑
    // const hasPermission = value.some(permission => userPermissions.includes(permission))
    // if (!hasPermission) {
    //   el.parentNode?.removeChild(el)
    // }

    // 现阶段允许所有具有 v-permission 标记的元素显示
  },
}
