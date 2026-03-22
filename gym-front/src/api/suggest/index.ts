import http from "@/http";
import type {SuggestType , SuggestParam } from "./SuggestModel";
//新增
export const addApi = (param:SuggestType)=>{
    return http.post("/api/suggest",param)
}
//列表
export const listApi = (param:SuggestParam)=>{
    return http.get("/api/suggest/list",param)
}
//编辑
export const editApi = (param:SuggestType)=>{
    return http.put('/api/suggest',param)
}
//删除
export const deleteApi = (id:string)=>{
    return http.delete(`/api/suggest/${id}`)
}