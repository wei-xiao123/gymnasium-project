import type { SuggestParam } from "@/api/suggest/SuggestModel";
import { nextTick, onMounted, reactive, ref } from "vue";
import { listApi } from "@/api/suggest";

export default function useTable() {
  // 表格高度
  const tableHeight = ref(0);

  // 表格数据
  const tableData = reactive({
    list: [],
  });

  // 列表查询的参数
  const listParam = reactive<SuggestParam>({
    currentPage: 1,
    pageSize: 10,
    title: "",
    total: 0,
  });

  // 搜索
  const searchBtn = () => {
    getList();
  };

  // 重置
  const resetBtn = () => {
    listParam.title = "";
    getList();
  };

  // 列表
  const getList = async () => {
    const res = await listApi(listParam);
    if (res && res.code === 200) {
      tableData.list = res.data.records;
      listParam.total = res.data.total;
    }
  };

  // 页容量改变时触发
  const sizeChange = (size: number) => {
    listParam.pageSize = size;
    getList();
  };

  // 页数改变时触发
  const currentChange = (page: number) => {
    listParam.currentPage = page;
    getList();
  };

  // 刷新
  const reFresh = () => {
    getList();
  };

  onMounted(() => {
    nextTick(() => {
      tableHeight.value = window.innerHeight - 230;
    });
    getList();
  });

  return {
    listParam,
    searchBtn,
    resetBtn,
    getList,
    tableData,
    sizeChange,
    currentChange,
    tableHeight,
    reFresh,
  };
}