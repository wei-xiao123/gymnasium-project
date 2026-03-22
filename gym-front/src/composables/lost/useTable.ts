import { nextTick, onMounted, reactive, ref } from 'vue'
import type { LostParam } from '@/api/lost/LostModel'
import { getListApi } from '@/api/lost'

export default function useTable() {
  // 表格高度
  const tableHeight = ref(0)

  // 表格数据
  const tableData = reactive({
    list: []
  })

  // 列表参数
  const listParam = reactive<LostParam>({
    currentPage: 1,
    pageSize: 10,
    lostName: '',
    total: 0
  })

  // 重置搜索
  const resetBtn = () => {
    listParam.lostName = ''
    listParam.currentPage = 1
    getList()
  }

  // 搜索
  const searchBtn = () => {
    getList()
  }

  // 获取列表数据
  const getList = async () => {
    const res = await getListApi(listParam)
    if (res && res.code === 200) {
      tableData.list = res.data.records
      listParam.total = res.data.total
    }
  }

  // 页容量改变时触发
  const sizeChange = (size: number) => {
    listParam.pageSize = size
    getList()
  }

  // 页数改变时触发
  const currentChange = (page: number) => {
    listParam.currentPage = page
    getList()
  }

  // 刷新表格
  const reFresh = () => {
    getList()
  }

  // 组件挂载时初始化
  onMounted(() => {
    nextTick(() => {
      tableHeight.value = window.innerHeight - 230
    })
    getList()
  })

  return {
    listParam,
    tableData,
    tableHeight,
    resetBtn,
    searchBtn,
    getList,
    sizeChange,
    currentChange,
    reFresh
  }
}