import { getTeacherApi } from "@/api/course/index";
import { reactive } from "vue";
import type { SelectRole } from "@/api/user/UserModel";

export default function useSelectTeacher() {
  // 定义教练列表数据
  const teacherData = reactive<SelectRole>({
    list: []
  });

  // 获取数据
  const listTeacher = async () => {
    const res = await getTeacherApi();
    if (res && res.code == 200) {
      teacherData.list = res.data;
    }
  };

  return {
    teacherData,
    listTeacher
  };
}