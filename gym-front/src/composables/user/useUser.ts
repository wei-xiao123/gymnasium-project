import type { AddUserModel } from "@/api/user/UserModel";
import { EditType } from "@/type/BaseEnum";
import { ref } from "vue";
import { deleteApi } from "@/api/user";
import { ElMessage } from "element-plus";
import type { FuncList } from "@/type/BaseType";
import useInstance from "@/hooks/useInstance";
import { resetPasswordApi } from "@/api/home";

export default function useUser(getList: FuncList) {
  const { global } = useInstance();

  // 新增组件的ref属性
  const addRef = ref<{ show: (type: string, row?: AddUserModel) => Promise<void> }>();

  // 新增
  const addBtn = () => {
    addRef.value?.show(EditType.ADD);
  };

  // 编辑
  const editBtn = (row: AddUserModel) => {
    addRef.value?.show(EditType.EDIT, row);
  };

  // 删除
  const deleteBtn = async (row: AddUserModel) => {
    const confirm = await global.$myconfirm("确定删除该数据吗?");
    if (confirm) {
      const res = await deleteApi(row.userId);
      if (res && res.code === 200) {
        ElMessage.success(res.msg);
        getList();
      }
    }
  };

  // 重置密码
  const resetPasBtn = async (row: AddUserModel) => {
    const confirm = await global.$myconfirm("确定重置密码吗?重置后的密码是[666666]");
    if (confirm) {
      const param = {
        userId: row.userId,
        userType: "2",
      };
      const res = await resetPasswordApi(param);
      if (res && res.code === 200) {
        ElMessage.success(res.msg);
      }
    }
  };

  return {
    addBtn,
    editBtn,
    deleteBtn,
    resetPasBtn,
    addRef,
  };
}