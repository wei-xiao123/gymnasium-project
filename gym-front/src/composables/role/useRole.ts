import type { AddRoleModel } from '@/api/role/RoleModel'
import { EditType } from '@/type/BaseEnum'
import type { FuncList } from '@/type/BaseType'
import type { Ref } from 'vue'
import { deleteApi } from '@/api/role'
import { ElMessage } from 'element-plus'
import useInstance from '@/hooks/useInstance'

interface UseRoleOptions {
  addRef?: Ref<any>
  assignRoleRef?: Ref<any>
}

export default function useRole(getList: FuncList, options?: UseRoleOptions) {
  const { global } = useInstance()

  // 使用外部提供的 refs 或创建内部 refs
  const addRef = options?.addRef
  const assignRoleRef = options?.assignRoleRef

  // 新增
  const addBtn = () => {
    addRef?.value?.show(EditType.ADD)
  }

  // 编辑
  const editBtn = (row: AddRoleModel) => {
    addRef?.value?.show(EditType.EDIT, row)
  }

  // 删除
  const deleteBtn = async (row: AddRoleModel) => {
    let confirm = await global.$myconfirm('确定删除该数据吗?')
    if (confirm) {
      let res = await deleteApi(row.roleId)
      if (res && res.code == 200) {
        ElMessage.success(res.msg)
        getList()
      }
    }
  }

  //分配权限按钮
  const assignBtn = (row: AddRoleModel) => {
      assignRoleRef?.value?.show(row.roleId, row.roleName)
  }

  return {
    addBtn,
    editBtn,
    deleteBtn,
    assignBtn,
  }

}
