import type { MaterialType } from "@/api/material/MaterialModel";
import useInstance from "@/hooks/useInstance";
import { ElMessage } from "element-plus";
import type { FuncList } from "@/type/BaseType";
import { deleteApi } from "@/api/material";

export default function useMaterial(getList: FuncList) {
  const { global } = useInstance();

  // 删除
  const deleteBtn = async (row: MaterialType) => {
    const confirm = await global.$myconfirm("确定删除该数据吗?");
    if (confirm) {
      const res = await deleteApi(row.id);
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        getList();
      }
    }
  };

  return {
    deleteBtn
  };
}