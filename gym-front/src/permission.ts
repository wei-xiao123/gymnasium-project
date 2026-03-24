import router from "./router"
import { userStore } from "./store/user"
import { menuStore } from "./store/menu"

// 白名单 不需要权限可以访问
const whiteList = ["/login", "/", "/404"]

// 标志位，防止路由守卫重复加载菜单
let isLoadingMenu = false

/**
 * 检查路由是否在菜单中存在
 */
function hasRouteInMenu(path: string, menuList: any[]): boolean {
  // 遍历菜单查找匹配的路由
  for (const menu of menuList) {
    // 在菜单项中通过 path 查找
    if (menu.path === path || menu.path === `/${path}`) {
      return true
    }
    // 在子菜单中查找
    if (menu.children && menu.children.length > 0) {
      if (hasRouteInMenu(path, menu.children)) {
        return true
      }
    }
  }
  return false
}

// 全局守卫路由
router.beforeEach(async (to, _from, next) => {
  // 获取用户的 store
  const ustore = userStore()
  // 获取菜单的 store
  const mstore = menuStore()
  // 获取 token
  const token = ustore.getToken()

  // 判断 token 是否存在
  if (token) {
    // token 存在
    if (to.path === "/login") {
      // 已登录，不能访问登录页
      next({ path: "/dashboard" })
    } else {
      const menuList = mstore.getMenuList()
      if (menuList.length > 0) {
        // 菜单已加载，验证路由权限
        if (hasRouteInMenu(to.path, menuList)) {
          // 路由在菜单中存在，允许访问
          next()
        } else {
          // 路由不在菜单中，无权限访问
          next("/404")
        }
      } else if (isLoadingMenu) {
        // 正在加载菜单，防止重复加载
        next()
      } else if (to.path === "/dashboard") {
        // 第一次访问，需要加载菜单
        try {
          isLoadingMenu = true
          // 获取用户信息
          await ustore.getInfo()
          // 获取菜单信息，动态生成路由
          await mstore.getMenu(router, {
            userId: ustore.getUserId(),
            userType: ustore.getUserType(),
          })
          isLoadingMenu = false
          // 菜单加载完成，继续放行
          next()
        } catch (error) {
          isLoadingMenu = false
          console.error("菜单加载失败:", error)
          // 菜单加载失败，允许访问
          next()
        }
      } else {
        // 其他路由且菜单未加载，先加载菜单
        try {
          isLoadingMenu = true
          await ustore.getInfo()
          await mstore.getMenu(router, {
            userId: ustore.getUserId(),
            userType: ustore.getUserType(),
          })
          isLoadingMenu = false
          // 菜单加载完成，重新导航到目标路由
          next({ ...to, replace: true })
        } catch (error) {
          isLoadingMenu = false
          console.error("菜单加载失败:", error)
          // 菜单加载失败，重定向到 404
          next("/404")
        }
      }
    }
  } else {
    // token 不存在
    isLoadingMenu = false
    if (whiteList.indexOf(to.path) !== -1) {
      // 存在白名单，直接放行
      next()
    } else {
      // 去登录
      next({ path: "/login" })
    }
  }
})