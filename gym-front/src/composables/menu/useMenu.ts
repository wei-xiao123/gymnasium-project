import type { MenuType } from "@/api/menu/MenuModel";
import { EditType } from "@/type/BaseEnum";
import type { FuncList } from "@/type/BaseType";
import type { Ref } from "vue";
import useInstance from "@/hooks/useInstance";
import { deleteApi } from "@/api/menu";
import { ElMessage } from "element-plus";

interface UseMenuOptions {
  addRef?: Ref<any>;
}

export default function useMenu(getList: FuncList, options?: UseMenuOptions) {
  const { global } = useInstance();
  const addRef = options?.addRef;

  // 新增
  const addBtn = () => {
    addRef?.value?.show(EditType.ADD);
  };

  // 编辑
  const editBtn = (row: MenuType) => {
    addRef?.value?.show(EditType.EDIT, row);
  };

  // 删除
  const deleteBtn = async (row: MenuType) => {
    const confirm = await global.$myconfirm("确定删除该数据吗？");
    if (confirm) {
      const res = await deleteApi(row.menuId);
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
  };
}