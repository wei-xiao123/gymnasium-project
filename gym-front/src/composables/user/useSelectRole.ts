import type { SelectRole } from "@/api/user/UserModel";
import { reactive, ref } from "vue";
import { getSelectApi, getRoleApi } from "@/api/user";

export default function useSelectRole() {
  // 用户选中的角色ID
  const roleId = ref('');

  // 定义角色列表数据
  const roleData = reactive<SelectRole>({
    list: []
  });

  // 获取角色列表
  const listRole = async () => {
    try {
      const res = await getSelectApi();
      if (res && res.code === 200) {
        roleData.list = res.data || [];
      }
    } catch (error) {
      console.error("获取角色列表失败:", error);
    }
  };

  // 根据用户ID查询用户的角色
  const getRole = async (userId: string) => {
    if (!userId) {
      roleId.value = '';
      return;
    }
    try {
      const res = await getRoleApi(userId);
      if (res && res.code === 200 && res.data) {
        roleId.value = res.data.roleId || '';
      }
    } catch (error) {
      console.error("获取用户角色失败:", error);
    }
  };

  return {
    roleData,
    listRole,
    getRole,
    roleId
  };
}