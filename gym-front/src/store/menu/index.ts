import { defineStore } from "pinia";
import { getMenuListApi } from "@/api/login";
import {type RouteRecordRaw } from "vue-router";
import Layout from '@/layout/Index.vue'
import center from '@/layout/center/center.vue'
import {type InfoParam } from "@/api/login/LoginModel";

//获取views下面的所有页面
const modules = import.meta.glob('../../views/**/*.vue')

const resolveViewComponent = (componentPath: string) => {
    if (!componentPath) {
        return null
    }
    const normalize = (p: string) => p.replace(/\\/g, '/').replace(/^\//, '').toLowerCase()
    const target = normalize(componentPath)

    // 精确匹配
    const exactKey = `../../views/${componentPath.replace(/^\//, '')}.vue`
    if (modules[exactKey]) {
        return modules[exactKey]
    }

    // 忽略大小写匹配
    const foundKey = Object.keys(modules).find((key) => normalize(key.replace('../../views/', '').replace('.vue', '')) === target)
    if (foundKey) {
        return modules[foundKey]
    }

    // 历史菜单兼容: /courseOrder 或 /course/Order -> /mycourse/MyCourse
    if ((target === 'courseorder' || target === 'course/order') && modules['../../views/mycourse/MyCourse.vue']) {
        return modules['../../views/mycourse/MyCourse.vue']
    }

    return null
}
//定义store
export const menuStore = defineStore('menuStore', {
    state: () => {
        return {
            menuList: []
        }
    },
    getters: {
        getMenuList(state) {
            return state.menuList
        }
    },
    actions: {
        clearMenu() {
            this.menuList = []
        },
        getMenu(router: any, parm: InfoParam) {
            return new Promise((resolve, reject) => {
                getMenuListApi(parm).then((res) => {
                    let accessRoute;
                    if (res && res.code == 200) {
                        //动态生成路由
                        accessRoute = generateRoutes(res.data, router)
                        const desk = [
                            {
                                path: "/dashboard",
                                component: "Layout",
                                name: "dashboard",
                                meta: {
                                    title: "首页",
                                    icon: "HomeFilled",
                                    roles: ["sys:dashboard"],
                                },
                                children: []
                            }
                        ] as any
                        this.menuList = desk.concat(accessRoute)
                    }
                    resolve(this.menuList)
                }).catch((error) => {
                    reject(error)
                })
            })
        }
    },
    /*  persist: {
         enabled: true, //开启持久化操作,默认全部字段存储，存储在sessionSotrage里面
         strategies: [
             { storage: localStorage, paths: ['menuList'] },
         ],
     } */
})
//动态生成路由
export function generateRoutes(routes: RouteRecordRaw[], router: any) {
    //定义接收生成的菜单
    const res: Array<RouteRecordRaw> = [];
    routes.forEach((route: any) => {
        //把route里面数据放到新的tmp里面
        const tmp = { ...route }
        const component = tmp.component;
        if (route.component) {
            if (component == 'Layout') {
                tmp.component = Layout;
            } else {
                const viewComponent = resolveViewComponent(component as string)
                if (viewComponent) {
                    tmp.component = viewComponent
                } else {
                    // 避免把字符串组件直接注入路由导致 Invalid route component
                    delete tmp.component
                }
            }
        }
        //如果存在下级
        if (tmp.children && tmp.children.length > 0) {
            if (route.component != 'Layout') {
                tmp.component = center;
            }
            //递归调用
            tmp.children = generateRoutes(tmp.children, router)
        }

        // 叶子路由必须有可用组件，否则跳过，避免导航时报错
        if ((!tmp.children || tmp.children.length === 0) && !tmp.component) {
            return
        }
        //动态添加路由
        router.addRoute(tmp)
        res.push(tmp)
    })
    return res;
}