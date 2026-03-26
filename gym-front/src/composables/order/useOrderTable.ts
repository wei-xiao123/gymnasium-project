import { nextTick, onMounted, reactive, ref } from "vue"
import type { OrderListParam } from "@/api/order/OrderModel"
import { listApi } from "@/api/order"

export default function useOrderTable() {
  // 表格高度
  const orderTableHeight = ref(0)

  // 表格数据
  const tableData = reactive({
    list: []
  })

  // 列表查询的参数
  const listParam = reactive<OrderListParam>({
    name: "",
    currentPage: 1,
    pageSize: 20,
    total: 0,
    controlName: ""
  })

  // 搜索按钮
  const searchBtn = () => {
    getList()
  }

  // 重置按钮
  const resetBtn = () => {
    listParam.name = ""
    getList()
  }

  // 页容量改变时触发
  const orderSizeChange = (size: number) => {
    listParam.pageSize = size
    getList()
  }

  // 页数改变时触发
  const orderCurrentChange = (page: number) => {
    listParam.currentPage = page
    getList()
  }

  // 获取列表
  const getList = async () => {
    const res = await listApi(listParam)
    if (res && res.code == 200) {
      tableData.list = res.data.records
      listParam.total = res.data.total
    }
  }

  onMounted(() => {
    getList()
    nextTick(() => {
      orderTableHeight.value = window.innerHeight - 230
    })
  })

  return {
    listParam,
    searchBtn,
    resetBtn,
    tableData,
    getList,
    orderSizeChange,
    orderCurrentChange,
    orderTableHeight
  }
}