import { ElMessage } from "element-plus";
import { ref } from "vue";
import { deleteApi } from "@/api/goods";
import type { GoodsType } from "@/api/goods/GoodsModel";
import useInstance from "@/hooks/useInstance";
import type { FuncList } from "@/type/BaseType";
import { EditType } from "@/type/BaseEnum";

export default function useGoods(getList: FuncList) {
	const { global } = useInstance();
	const addRef = ref<{ show: (type: string, row?: GoodsType) => void }>();

	const addBtn = () => {
		addRef.value?.show(EditType.ADD);
	};

	const editBtn = (row: GoodsType) => {
		addRef.value?.show(EditType.EDIT, row);
	};

	const deleteBtn = async (row: GoodsType) => {
		const confirm = await global.$myconfirm("确定删除该数据吗?");
		if (confirm) {
			const res = await deleteApi(row.goodsId);
			if (res && res.code == 200) {
				ElMessage.success(res.msg);
				getList();
			}
		}
	};

	return {
		addBtn,
		editBtn,
		deleteBtn,
		addRef,
	};
}