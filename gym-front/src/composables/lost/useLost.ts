import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import type { LostType } from '@/api/lost/LostModel'
import { deleteApi } from '@/api/lost'
import useInstance from '@/hooks/useInstance'
import { EditType } from '@/type/BaseEnum'
import type { FuncList } from '@/type/BaseType'

export default function useLost(getList: FuncList) {
  const { global } = useInstance()

  // 新增表单 ref
  const addRef = ref<{
    show: (type: string, row?: LostType) => void
  }>()

  // 认领详情 ref
  const lostPersonRef = ref<{
    show: (row: LostType) => void
  }>()

  // 新增
  const addBtn = () => {
    addRef.value?.show(EditType.ADD)
  }

  // 编辑
  const editBtn = (row: LostType) => {
    addRef.value?.show(EditType.EDIT, row)
  }

  // 删除
  const deleteBtn = async (row: LostType) => {
    const confirm = await global.$myconfirm('确定删除该数据吗?')
    if (confirm) {
      const res = await deleteApi(row.lostId)
      if (res && res.code === 200) {
        ElMessage.success(res.msg)
        getList()
      }
    }
  }

  // 认领物品
  const lostBtn = (row: LostType) => {
    if (row.status === '1') {
      ElMessage.warning('该物品已经被认领!')
      return
    }
    lostPersonRef.value?.show(row)
  }

  return {
    addBtn,
    editBtn,
    deleteBtn,
    lostBtn,
    addRef,
    lostPersonRef
  }
}