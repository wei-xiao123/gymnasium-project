import http from "@/http";
import type { MaterialType, ListParam } from "./MaterialModel";

// 新增
export const addApi = (param: MaterialType) => {
  return http.post("/api/material", param);
};

// 列表
export const getListApi = (parm: ListParam) => {
  return http.get("/api/material/list", parm);
};

// 编辑
export const editApi = (param: MaterialType) => {
  return http.put("/api/material", param);
};

// 删除
export const deleteApi = (id: string) => {
  return http.delete(`/api/material/${id}`);
};