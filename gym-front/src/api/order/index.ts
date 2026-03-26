import http from "@/http";
import {type OrderListParam } from '@/api/order/OrderModel'
//下单
export const dowmApi = (param:any)=>{
    return http.post("/api/order/down",param)
}
//列表
export const listApi = (param:OrderListParam)=>{
    return http.get("/api/order/list",param)
}