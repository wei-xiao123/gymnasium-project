import { defineStore } from "pinia"
import { getMenuListApi } from "@/api/login"
import type { RouteRecordRaw } from "vue-router"
import { usePersist } from "@/composables/usePersist"
import Layout from "@/layout/Index.vue"
import type { InfoParam } from "@/api/login/LoginModel"
import { userStore } from "@/store/user"

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
            
            // 获取用户权限
            const store = userStore()
            const userPermissions = store.codeList || []
            
            // 打印用户权限和菜单数据用于调试
            console.log("用户权限码:", userPermissions)
            console.log("后端返回的菜单数据:", JSON.stringify(res.data, null, 2))
            
            // 动态生成路由 - 传递用户权限进行权限过滤
            const accessRoute = generateRoutes(res.data, router, dynamicRouteNames, userPermissions)
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
            
            console.log("生成的菜单列表:", JSON.stringify(menuList.value, null, 2))
          }
          resolve(menuList.value)
        })
        .catch((error) => {
          console.error("获取菜单列表失败:", error)
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
 * 检查用户是否有权限访问菜单项
 */
function hasPermission(roles: string[], userPermissions: string[]): boolean {
  // 如果菜单没有权限要求，则允许访问
  if (!roles || roles.length === 0) {
    return true
  }
  
  // 如果用户权限为空，可能是权限还未加载，暂时允许访问所有菜单
  // （这样全权限用户在权限加载完成前也能看到菜单）
  if (!userPermissions || userPermissions.length === 0) {
    console.warn("用户权限未加载，允许访问所有菜单")
    return true
  }
  
  // 检查用户是否拥有菜单要求的任何一个权限码
  return roles.some((role: string) => userPermissions.includes(role))
}

/**
 * 尝试多种路径模式查找组件
 */
function findComponentModule(componentPath: string): any {
  // 策略1: 直接拼接路径
  let componentKey = `../../views${componentPath}.vue`
  if (modules[componentKey]) {
    return modules[componentKey]
  }

  // 策略2: 尝试去掉最后一层子目录（例如 /system/user/UserList -> /system/UserList）
  const parts = componentPath.split("/").filter(p => p) // 去掉空字符串
  if (parts.length >= 3) {
    // 去掉倒数第二个部分，保留最后的文件名
    const simplified = "/" + [parts[0], parts[parts.length - 1]].join("/")
    componentKey = `../../views${simplified}.vue`
    if (modules[componentKey]) {
      console.log(`✓ 使用简化路径: ${simplified} (原始: ${componentPath})`)
      return modules[componentKey]
    }
  }

  // 策略3: 尝试去掉所有中间目录，只保留最后的文件名
  if (parts.length > 1) {
    const fileName = parts[parts.length - 1]
    const category = parts[0]
    const simplified = `/${category}/${fileName}`
    componentKey = `../../views${simplified}.vue`
    if (modules[componentKey]) {
      console.log(`✓ 使用简化路径: ${simplified} (原始: ${componentPath})`)
      return modules[componentKey]
    }
  }

  // 所有策略都失败
  return null
}

/**
 * 动态生成路由 - 过滤没有权限的菜单项
 */
export function generateRoutes(
  routes: RouteRecordRaw[],
  router: any,
  dynamicRouteNames: string[],
  userPermissions: string[] = []
) {
  const res: Array<RouteRecordRaw> = []
  
  routes.forEach((route: any) => {
    // 检查用户是否有权限访问此菜单
    if (route.meta && route.meta.roles && !hasPermission(route.meta.roles, userPermissions)) {
      console.log(`用户无权限访问: ${route.path}，所需权限: ${route.meta.roles.join(",")}，用户权限: ${userPermissions.join(",")}`)
      return
    }

    const tmp = { ...route }
    const component = tmp.component

    // 确保 path 以 / 开头（如果不是父菜单）
    if (tmp.path && !tmp.path.startsWith("/") && component !== "Layout") {
      tmp.path = "/" + tmp.path
    }

    // 处理 component 字段
    if (component) {
      if (component === "Layout") {
        tmp.component = Layout
      } else {
        // 动态导入组件 - 支持多种路径模式
        const foundComponent = findComponentModule(component)
        if (foundComponent) {
          tmp.component = foundComponent
        } else {
          console.warn(`未找到组件: ${component} (已尝试多种路径)`)
          // 如果没有子菜单，则跳过此项
          if (!tmp.children || tmp.children.length === 0) {
            return
          }
        }
      }
    } else if (tmp.children && tmp.children.length > 0) {
      // 如果没有 component 但有子菜单，使用 Layout
      tmp.component = Layout
    } else {
      // 如果既没有 component 也没有 children，则跳过此项
      console.warn(`路由 ${tmp.path} 缺少 component 或 children，已跳过`)
      return
    }

    // 如果存在下级菜单，递归处理并过滤
    if (tmp.children && tmp.children.length > 0) {
      tmp.children = generateRoutes(tmp.children, router, dynamicRouteNames, userPermissions)
      // 如果子菜单全部被过滤，则跳过此父菜单（无论是否是 Layout）
      if (tmp.children.length === 0) {
        console.log(`菜单 ${tmp.path} 的子菜单全部被过滤，已跳过`)
        return
      }
    }

    // 为路由生成唯一名称
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