import { nextTick, onMounted, reactive, ref } from "vue"
import type { MyCourseListParam } from "@/api/course/CourseModel"
import { getMyCourseListApi } from "@/api/course"
import { userStore } from "@/store/user"
import { normalizeImageUrl } from "@/utils/imageUrl"

export default function useMyCourseTable() {
  const store = userStore()

  // 表格高度
  const tableHeight = ref(0)

  // 表格数据
  const tableDate = reactive({
    list: []
  })

  // 列表查询参数
  const listParam = reactive<MyCourseListParam>({
    userId: "",
    userType: "",
    currentPage: 1,
    pageSize: 10,
    total: 0
  })

  // 查询列表
  const getList = async () => {
    let userId: any = store.getUserId
    let userType: any = store.getUserType
    if (!userId || !userType) {
      try {
        const localState = localStorage.getItem("userStore")
        if (localState) {
          const parsed = JSON.parse(localState)
          userId = userId || parsed?.userId || parsed?.state?.userId || ""
          userType = userType || parsed?.userType || parsed?.state?.userType || ""
        }
      } catch (e) {
        userId = userId || ""
        userType = userType || ""
      }
    }
    if (!userId || !userType) {
      tableDate.list = []
      listParam.total = 0
      return
    }
    listParam.userId = String(userId)
    listParam.userType = String(userType)

    // 使用普通对象避免 reactive 对象序列化导致分页参数异常
    const queryParam = {
      userId: String(listParam.userId),
      userType: String(listParam.userType),
      currentPage: Number(listParam.currentPage) || 1,
      pageSize: Number(listParam.pageSize) || 10,
    }
    const res = await getMyCourseListApi(queryParam as any)
    if (res && res.code == 200) {
      const records =
        res.data?.records ||
        res.data?.list ||
        res.data?.data?.records ||
        res.data?.data?.list ||
        res.data?.data ||
        []
      tableDate.list = records.map((item: any) => ({
        ...item,
        image: normalizeImageUrl(item.image),
      }))
      listParam.total =
        res.data?.total ||
        res.data?.data?.total ||
        records.length ||
        0

      const current = Number(res.data?.current || res.data?.currentPage || res.data?.data?.current || queryParam.currentPage)
      if (!Number.isNaN(current) && current > 0) {
        listParam.currentPage = current
      }
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

  onMounted(() => {
    nextTick(() => {
      tableHeight.value = window.innerHeight - 230
    })
    getList()
  })

  return {
    listParam,
    tableDate,
    getList,
    sizeChange,
    currentChange,
    tableHeight
  }
}