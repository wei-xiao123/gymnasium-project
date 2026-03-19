import type { ListParam } from "@/api/member_card/MemberModel";
import { nextTick, onMounted, reactive, ref } from "vue";
import { getListApi } from "@/api/member_card";

export default function useMemberTable() {
  // 表格高度
  const tableHeight = ref(0);

  // 表格数据
  const tableList = reactive({
    list: [],
  });

  // 查询参数
  const listParam = reactive<ListParam>({
    title: "",
    pageSize: 10,
    currentPage: 1,
    total: 0,
  });

  // 查询列表
  const getList = async () => {
    const res = await getListApi(listParam);
    if (res && res.code == 200) {
      console.log(res);
      tableList.list = res.data.records;
      listParam.total = res.data.total;
    }
  };

  // 搜索
  const searchBtn = () => {
    getList();
  };

  // 重置
  const resetBtn = () => {
    listParam.title = "";
    getList();
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

  // 刷新表格
  const refresh = () => {
    getList();
  };

  onMounted(() => {
    getList();
    nextTick(() => {
      tableHeight.value = window.innerHeight - 230;
    });
  });

  return {
    listParam,
    getList,
    searchBtn,
    resetBtn,
    tableList,
    sizeChange,
    currentChange,
    tableHeight,
    refresh,
  };
}