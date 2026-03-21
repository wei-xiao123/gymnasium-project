import http from "@/http";
import type { CourseListParam , CourseType} from './CourseModel.ts'
//新增
export const addApi = (param:CourseType)=>{
    return http.post("/api/course",param)
}
//图片上传
export const uploadImageApi = (param:object)=>{
    return http.upload("/api/upload/uploadImage",param)
}
//获取课程教练
export const getTeacherApi = ()=>{
    return http.get("/api/user/getTeacher")
}
//列表
export const listApi = (param:CourseListParam)=>{
    return http.get("/api/course/list",param)
}
//编辑
export const editApi = (parm:CourseType)=>{
    return http.put("/api/course",parm)
}
//删除
export const deleteApi = (courseId:string)=>{
    return http.delete(`/api/course/${courseId}`)
}