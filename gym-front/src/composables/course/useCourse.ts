import type { CourseType } from "@/api/course/CourseModel"
import { EditType } from "@/type/BaseEnum"
import { ref } from "vue"
import { deleteApi,} from "@/api/course"
import { ElMessage } from "element-plus"
import type { FuncList } from "@/type/BaseType"
import useInstance from "@/hooks/useInstance"
import { userStore } from '@/store/user'

export default function useCourse(getList:FuncList){

    const store = userStore()

    const {global} = useInstance()
    const addRef = ref<{ show: (type: string, row?: CourseType) => void }>()

    //新增
    const addBtn = ()=>{
        addRef.value?.show(EditType.ADD)
    }

    //编辑
    const editBtn = (row:CourseType)=>{
        addRef.value?.show(EditType.EDIT,row)
    }

    //删除
    const deleteBtn = async (row:CourseType)=>{
        let confirm = await global.$myconfirm('确定需要删除该数据吗?')
        if(confirm){
            let res = await deleteApi(row.courseId)
            if(res && res.code == 200){
                ElMessage.success(res.msg)
                getList()
            }
        }
    }


    return {
        addBtn,
        editBtn,
        deleteBtn,
        addRef,
    }
}
