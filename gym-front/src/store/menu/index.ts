import { defineStore } from "pinia"
import { getMenuListApi } from "@/api/login"
import type { RouteRecordRaw } from "vue-router"
import { usePersist } from "@/composables/usePersist"
import Layout from "@/layout/Index.vue"
import center from "@/layout/center/center.vue"
import type { InfoParam } from "@/api/login/LoginModel"

// 获取 views 下面的所有页面
const modules = import.meta.glob("../../views/**/*.vue")

// 定义 store
export const menuStore = defineStore("menuStore", () => {
  // 使用 usePersist 创建持久化状态
  const menuList = usePersist<any[]>("menuList", [])
  // 追踪动态添加的路由名称，用于后续清理
  const dynamicRouteNames: string[] = []

  /**
   * 获取菜单列表
   */
  const getMenu = (router: any, param: InfoParam) => {
    return new Promise((resolve, reject) => {
      getMenuListApi(param)
        .then((res) => {
          if (res && res.code === 200) {
            // 先清除旧的动态路由
            clearRoutes(router)
            
            // 动态生成路由
            const accessRoute = generateRoutes(res.data, router, dynamicRouteNames)
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
                children: [],
              },
            ] as any
            menuList.value = desk.concat(accessRoute)
          }
          resolve(menuList.value)
        })
        .catch((error) => {
          reject(error)
        })
    })
  }

  /**
   * 获取菜单列表值
   */
  const getMenuList = () => {
    return menuList.value || []
  }

  /**
   * 清除所有动态路由
   */
  const clearRoutes = (router: any) => {
    // 移除所有动态添加的路由
    dynamicRouteNames.forEach((routeName) => {
      router.removeRoute(routeName)
    })
    // 清空追踪列表
    dynamicRouteNames.length = 0
  }

  return {
    menuList,
    getMenuList,
    getMenu,
    clearRoutes,
  }
})

/**
 * 动态生成路由
 */
export function generateRoutes(routes: RouteRecordRaw[], router: any, dynamicRouteNames: string[]) {
  // 定义接收生成的菜单
  const res: Array<RouteRecordRaw> = []
  routes.forEach((route: any) => {
    // 把 route 里面数据放到新的 tmp 里面
    const tmp = { ...route }
    const component = tmp.component

    // 确保 path 以 / 开头（如果不是父菜单）
    if (tmp.path && !tmp.path.startsWith("/") && tmp.component !== "Layout") {
      tmp.path = "/" + tmp.path
    }

    if (route.component) {
      if (component === "Layout") {
        tmp.component = Layout
      } else {
        tmp.component = modules[`../../views${component}.vue`]
      }
    }

    // 如果存在下级
    if (tmp.children && tmp.children.length > 0) {
      if (route.component !== "Layout") {
        tmp.component = center
      }
      // 递归调用
      tmp.children = generateRoutes(tmp.children, router, dynamicRouteNames)
    }

    // 为路由生成唯一名称（如果没有的话）
    if (!tmp.name) {
      tmp.name = `route_${tmp.path}_${Math.random().toString(36).substr(2, 9)}`
    }

    // 追踪动态路由名称
    dynamicRouteNames.push(tmp.name)

    // 动态添加路由
    router.addRoute(tmp)
    res.push(tmp)
  })

  return res
}