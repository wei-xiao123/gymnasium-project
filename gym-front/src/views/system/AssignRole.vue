<template>
  <sys-dialog
    :title="dialog.title"
    :width="dialog.width"
    :height="dialog.height"
    :visible="dialog.visible"
    @onClose="onClose"
    @onConfirm="commit"
  >
    <template #content>
      <el-tree
        ref="assignTree"
        :data="assignTreeData.list"
        :props="defaultProps"
        :default-checked-keys="assignTreeData.assignTreeChecked"
        node-key="menuId"
        empty-text="暂无数据"
        show-checkbox
        default-expand-all
        highlight-current
      />
    </template>
  </sys-dialog>
</template>

<script setup lang="ts">
import { ref, reactive } from "vue"
import { ElMessage, ElTree } from "element-plus"
import SysDialog from "@/components/SysDialog.vue"
import useDialog from "@/hooks/useDialog"
import useAssign from "@/composables/role/useAssign"
import { userStore } from "@/store/user"
import { saveRoleMenuApi } from "@/api/role/index"
import type { SaveAssignParam } from "@/api/role/RoleModel"

// 树的 ref 属性
const assignTree = ref<InstanceType<typeof ElTree>>()

// 用户信息
const store = userStore()

// 提交参数
const saveParm = reactive<SaveAssignParam>({
  roleId: "",
  list: [],
})

// 权限树相关
const { assignTreeData, getMenuTree, defaultProps } = useAssign()

// 弹框相关
const { dialog, onClose, onShow } = useDialog()

/**
 * 显示分配权限弹框
 * @param roleId 角色ID
 * @param name 角色名称
 */
const show = async (roleId: string, name: string) => {
  try {
    saveParm.roleId = roleId

    const param = {
      roleId,
      userId: String(store.getUserId()),
    }

    // 获取权限树数据
    await getMenuTree(param)

    // 设置弹框属性
    dialog.title = `为【${name}】分配权限`
    dialog.width = 300
    dialog.height = 450

    onShow()
  } catch (error) {
    console.error("获取权限树数据失败:", error)
    ElMessage.error("获取权限树数据失败")
  }
}

defineExpose({
  show,
})

/**
 * 提交保存权限分配
 */
const commit = async () => {
  try {
    // 获取选中的权限ID（全选和半选）
    const checkedIds = (assignTree.value?.getCheckedKeys() as string[]) || []
    const halfCheckedIds = (assignTree.value?.getHalfCheckedKeys() as string[]) || []
    const allIds = [...checkedIds, ...halfCheckedIds]

    // 验证是否选中了权限
    if (allIds.length === 0) {
      ElMessage.warning("请勾选权限信息!")
      return
    }

    // 保存权限分配
    saveParm.list = allIds
    const res = await saveRoleMenuApi(saveParm)

    if (res && res.code === 200) {
      ElMessage.success(res.msg)
      onClose()
    } else {
      ElMessage.error(res?.msg || "保存权限失败")
    }
  } catch (error) {
    console.error("保存权限分配失败:", error)
    ElMessage.error("保存权限分配失败")
  }
}
</script>

<style scoped></style>