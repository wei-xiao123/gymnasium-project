import { onMounted, reactive, ref } from "vue";
import type { GoodsParam, GoodsType, AddCar } from "@/api/goods/GoodsModel";
import { listApi } from "@/api/goods";
import { ElMessage } from "element-plus";

export default function useChildDrawer() {
  const innerDrawer = ref(false);

  const innerCloseClick = () => {
    innerDrawer.value = false;
  };

  // 表格高度
  const goodsHeight = ref(0);

  // 列表参数
  const goodsParam = reactive<GoodsParam>({
    currentPage: 1,
    pageSize: 20,
    name: "",
    total: 0,
  });

  // 表格数据
  const tableList = reactive({
    list: [],
  });

  // 获取表格数据
  const getList = async () => {
    const res = await listApi(goodsParam);
    if (res && res.code === 200) {
      tableList.list = res.data.records;
      goodsParam.total = res.data.total;
    }
  };

  const innerDrawerShow = () => {
    getList();
    innerDrawer.value = true;
  };

  // 页容量改变时触发
  const goodsSizeChange = (size: number) => {
    goodsParam.pageSize = size;
    getList();
  };

  // 页数改变时触发
  const goodsCurrentChange = (page: number) => {
    goodsParam.currentPage = page;
    getList();
  };

  // 搜索按钮
  const searchGoodsBtn = () => {
    getList();
  };

  // 重置
  const resetGoodsBtn = () => {
    goodsParam.name = "";
    getList();
  };

  // 选择的商品数据
  const selectGoods = reactive<AddCar>({
    list: [],
  });

  // 加入购物车按钮
  const addCarBtn = (row: GoodsType) => {
    row.num = 1;
    // 判断商品是否已经加入购物车
    const flag = selectGoods.list.some((item) => item.goodsId === row.goodsId);
    if (flag) {
      ElMessage.warning("该商品已经添加到购物车!");
      return;
    }
    selectGoods.list.push(row);
    ElMessage.success("加入成功!");
  };

  onMounted(() => {
    goodsHeight.value = window.innerHeight - 180;
  });

  return {
    innerDrawer,
    innerCloseClick,
    goodsParam,
    getList,
    tableList,
    innerDrawerShow,
    goodsSizeChange,
    goodsCurrentChange,
    searchGoodsBtn,
    resetGoodsBtn,
    goodsHeight,
    addCarBtn,
    selectGoods,
  };
}