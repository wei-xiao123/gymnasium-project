//登录参数的数据类型
export type LoginParam = {
    username:string,
    password:string,
    code:string,
    userType:string
}

export type InfoParam = {
    userId:string,
    userType:string
}

export type RegisterParam = {
    username:string,
    password:string,
    confirmPassword:string,
    name:string,
    phone:string,
    code:string
}