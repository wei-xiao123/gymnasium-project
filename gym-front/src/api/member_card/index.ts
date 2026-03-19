import http from "@/http";
import type { CardType, ListParam } from "./MemberModel";
//新增会员卡
export const addApi = (param:CardType)=>{
    return http.post("/api/memberCard",param)
}
//查询会员卡列表
export const getListApi = (param:ListParam)=>{
    return http.get("/api/memberCard/list",param)
}
//编辑会员卡
export const editApi = (param:CardType)=>{
    return http.put("/api/memberCard",param)
}
//删除会员卡
export const deleteApi = (cardId:string)=>{
    return http.delete(`/api/memberCard/${cardId}`)
}