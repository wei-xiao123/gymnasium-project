import http from "@/http"
import type { MemberParam, MemberType,ApplyCard,Recharge} from"./MemberModel"
        //新增
    export const addApi = (param:MemberType)=>{
        return http.post("/api/member",param)
    }
        //列表
    export const getListApi = (param:MemberParam)=>{
        return http.get("/api/member/list",param)
    }
        //查询会员卡列表
    export const getCardListApi = ()=>{
        return http.get("/api/member/getCardList")
    }
        //编辑
    export const editApi = (param:MemberType)=>{
        return http.put("/api/member",param)
    }
        //删除
    export const deleteApi = (memberId:string)=>{
        return http.delete(`/api/member/${memberId}`)
    }
        //根据会员id查询角色id
    export const getRoleByMemberIdApi = (memberId:string)=>{
        return http.get("/api/member/getRoleByMemberId",{memberId:memberId})
    }
    //办卡
    export const applySaveApi = (param:ApplyCard)=>{
        return http.post("/api/member/joinApply",param)
    }
    //充值
    export const rechargeApi = (param:Recharge)=>{
        return http.post("/api/member/recharge",param)
    }