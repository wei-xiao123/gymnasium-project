// 角色数据类型
export type AddRoleModel = {
  type: string
  roleId: string
  roleName: string
  remark: string
  types: string
}
//列表查询参数类型
export type ListParam = {
    roleName:string,
    currentPage:number,
    pageSize:number,
    total:number
}