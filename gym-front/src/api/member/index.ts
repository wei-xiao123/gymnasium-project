import http from "@/http"
import type { MemberParam, MemberType} from"./MemberModel"
        //新增
    export const addApi = (param:MemberType)=>{
        return http.post("/api/member",param)
    }
        //列表
    export const getListApi = (parm:MemberParam)=>{
        return http.get("/api/member/list",parm)
    }
        //编辑
    export const editApi = (parm:MemberType)=>{
        return http.put("/api/member",parm)
    }
        //删除
    export const deleteApi = (memberId:string)=>{
        return http.delete(`/api/member/${memberId}`)
    }
        //根据会员id查询角色id
    export const getRoleByMemberIdApi = (memberId:string)=>{
        return http.get("/api/member/getRoleByMemberId",{memberId:memberId})
}