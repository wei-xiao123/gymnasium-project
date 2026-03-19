import type { ListParam } from "@/api/user/UserModel";
import { nextTick, onMounted, reactive, ref } from "vue";
import { getListApi } from "@/api/user";

export default function useTable() {
  // 查询参数
  const listParam = reactive<ListParam>({
    phone: '',
    pageSize: 10,
    currentPage: 1,
    nickName: '',
    total: 0
  });

  // 表格列表数据
  const tableList = ref<any[]>([]);

  // 表格高度
  const tableHeight = ref(0);

  // 获取列表数据
  const getList = async () => {
    try {
      const res = await getListApi(listParam);
      if (res && res.code === 200) {
        // 设置表格的数据
        tableList.value = res.data?.records || [];
        // 设置分页的总条数
        listParam.total = res.data?.total || 0;
      }
    } catch (error) {
      console.error("获取列表失败:", error);
      tableList.value = [];
    }
  };

  // 搜索
  const searchBtn = () => {
    listParam.currentPage = 1;
    getList();
  };

  // 重置
  const resetBtn = () => {
    listParam.phone = '';
    listParam.nickName = '';
    listParam.currentPage = 1;
    getList();
  };

  // 页容量改变时触发
  const sizeChange = (size: number) => {
    listParam.pageSize = size;
    listParam.currentPage = 1;
    getList();
  };

  // 页数改变时触发
  const currentChange = (page: number) => {
    listParam.currentPage = page;
    getList();
  };

  // 刷新列表
  const refresh = () => {
    listParam.currentPage = 1;
    getList();
  };

  onMounted(() => {
    getList();
    nextTick(() => {
      // 设置表格高度
      tableHeight.value = window.innerHeight - 230;
    });
  });

  return {
    tableList,
    listParam,
    getList,
    searchBtn,
    resetBtn,
    sizeChange,
    currentChange,
    tableHeight,
    refresh
  };
}