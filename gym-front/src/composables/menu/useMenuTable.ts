import { onMounted, reactive } from "vue";
import { getListApi } from "@/api/menu";
import { ElMessage } from "element-plus";

export default function useMenuTable() {
  // 定义表格数据
  const tableList = reactive({
    list: [],
  });

  // 获取表格数据
  const getList = async () => {
    try {
      const res = await getListApi();
      if (res && res.code === 200) {
        tableList.list = res.data || [];
      } else {
        ElMessage.error(res?.msg || "加载菜单列表失败");
        tableList.list = [];
      }
    } catch (error) {
      console.error("加载菜单列表错误:", error);
      ElMessage.error("加载菜单列表失败");
      tableList.list = [];
    }
  };

  // 刷新列表
  const refresh = () => {
    getList();
  };

  onMounted(() => {
    getList();
  });

  return {
    tableList,
    getList,
    refresh,
  };
}