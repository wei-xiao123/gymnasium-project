import type { CourseType } from "@/api/course/CourseModel";
import { EditType } from "@/type/BaseEnum";
import { ref } from "vue";
import { deleteApi } from "@/api/course";
import { ElMessage } from "element-plus";
import type { FuncList } from "@/type/BaseType";
import useInstance from "@/hooks/useInstance";

export default function useCourse(getList: FuncList) {
  const { global } = useInstance();
  
  const addRef = ref<{ show: (type: string, row?: CourseType) => void }>();

  // 新增
  const addBtn = () => {
    addRef.value?.show(EditType.ADD);
  };

  // 编辑
  const editBtn = (row: CourseType) => {
    addRef.value?.show(EditType.EDIT, row);
  };

  // 删除
  const deleteBtn = async (row: CourseType) => {
    const confirm = await global.$myconfirm("确定删除该数据吗?");
    if (confirm) {
      const res = await deleteApi(row.courseId);
      if (res && res.code == 200) {
        ElMessage.success(res.msg);
        getList();
      }
    }
  };

  // 选课
  const joinBtn = async (row: CourseType) => {
    // TODO: 实现选课功能
  };

  return {
    addBtn,
    editBtn,
    deleteBtn,
    addRef,
    joinBtn
  };
}