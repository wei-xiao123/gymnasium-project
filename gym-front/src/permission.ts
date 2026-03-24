import router from "./router"
import { userStore } from "./store/user"
import { menuStore } from "./store/menu"

// 白名单 不需要权限可以访问
const whiteList = ["/login", "/"]

// 标志位，防止路由守卫重复加载菜单
let isLoadingMenu = false
// 菜单加载完成的Promise
let menuLoadPromise: Promise<any> | null = null

/**
 * 检查用户是否有权限访问路由
 */
function hasRoutePermission(path: string, menuList: any[], userPermissions: string[]): boolean {
  // 规范化路径
  const normalizedPath = path.startsWith("/") ? path : `/${path}`
  
  // 递归遍历菜单查找匹配的路由
  function searchMenu(menus: any[]): boolean {
    for (const menu of menus) {
      // 获取菜单路径（可能是 path 或其他字段）
      const menuPath = menu.path || menu.url || ""
      
      // 规范化菜单路径
      const normalizedMenuPath = menuPath.startsWith("/") ? menuPath : `/${menuPath}`
      
      // 检查当前菜单项是否匹配
      if (normalizedMenuPath === normalizedPath) {
        // 检查用户是否有权限访问此菜单
        if (menu.meta && menu.meta.roles && Array.isArray(menu.meta.roles)) {
          // 如果菜单有角色要求，检查用户是否有任何一个角色
          return menu.meta.roles.some((role: string) => userPermissions.includes(role))
        }
        // 如果菜单没有角色要求，则允许访问
        return true
      }
      
      // 在子菜单中递归查找
      if (menu.children && Array.isArray(menu.children)) {
        if (searchMenu(menu.children)) {
          return true
        }
      }
    }
    return false
  }
  
  return searchMenu(menuList)
}

// 全局守卫路由
router.beforeEach(async (to, _from, next) => {
  console.log(`[路由守卫] 导航到: ${to.path}`)
  
  // 白名单中的路由直接放行
  if (whiteList.includes(to.path)) {
    console.log(`[路由守卫] ${to.path} 在白名单中，直接放行`)
    return next()
  }

  // 获取用户的 store
  const ustore = userStore()
  // 获取菜单的 store
  const mstore = menuStore()
  // 获取 token
  const token = ustore.getToken()

  // 判断 token 是否存在
  if (token) {
    console.log(`[路由守卫] Token 存在，检查菜单权限`)
    
    // 等待菜单加载完成（如果正在加载中）
    if (isLoadingMenu && menuLoadPromise) {
      console.log(`[路由守卫] 菜单正在加载中，等待加载完成...`)
      await menuLoadPromise
    }
    
    const menuList = mstore.getMenuList()
    console.log(`[路由守卫] 菜单列表长度: ${menuList.length}`)
    
    if (menuList.length > 0) {
      // 菜单已加载，验证路由权限
      console.log(`[路由守卫] 菜单已加载，验证路由权限`)
      
      // 首页和控制面板无条件允许访问
      if (to.path === "/" || to.path === "/dashboard") {
        console.log(`[路由守卫] ${to.path} 无条件允许`)
        return next()
      }
      
      const userPermissions = ustore.codeList || []
      if (hasRoutePermission(to.path, menuList, userPermissions)) {
        // 路由在菜单中存在且有权限，允许访问
        console.log(`[路由守卫] ${to.path} 有权限访问，放行`)
        return next()
      } else {
        // 路由不在菜单中或无权限访问，重定向到首页
        console.warn(`[路由守卫] ${to.path} 无权限访问，重定向到 /dashboard`)
        return next("/dashboard")
      }
    } else {
      // 菜单未加载，需要先加载菜单
      console.log(`[路由守卫] 菜单未加载，开始加载菜单...`)
      
      if (!isLoadingMenu) {
        isLoadingMenu = true
        
        // 创建菜单加载Promise
        menuLoadPromise = (async () => {
          try {
            console.log(`[路由守卫] 获取用户信息...`)
            // 获取用户信息和权限
            await ustore.getInfo()
            console.log(`[路由守卫] 用户信息获取成功，权限: ${ustore.codeList?.length || 0}个`)

            console.log(`[路由守卫] 获取菜单信息...`)
            // 获取菜单信息，动态生成路由
            await mstore.getMenu(router, {
              userId: ustore.getUserId(),
              userType: ustore.getUserType(),
            })
            console.log(`[路由守卫] 菜单信息获取成功`)

            return true
          } catch (error) {
            console.error("[路由守卫] 菜单/权限加载失败:", error)
            return false
          } finally {
            isLoadingMenu = false
            menuLoadPromise = null
          }
        })()

        const loadSuccess = await menuLoadPromise
        
        if (!loadSuccess) {
          // 加载失败，允许访问 dashboard
          console.log(`[路由守卫] 菜单加载失败，重定向到 /dashboard`)
          return to.path === "/dashboard" ? next() : next("/dashboard")
        }
      } else if (menuLoadPromise) {
        // 等待菜单加载完成
        await menuLoadPromise
      }

      // 菜单加载完成后，重新验证当前路由的权限
      console.log(`[路由守卫] 菜单加载完成，重新验证路由权限`)
      
      // 首页和控制面板无条件允许
      if (to.path === "/" || to.path === "/dashboard") {
        console.log(`[路由守卫] ${to.path} 无条件允许`)
        return next({ ...to, replace: true })
      }
      
      const updatedMenuList = mstore.getMenuList()
      const userPermissions = ustore.codeList || []
      
      if (updatedMenuList.length === 0) {
        console.warn(`[路由守卫] 菜单列表仍为空，无法验证权限`)
        return next("/dashboard")
      }
      
      if (hasRoutePermission(to.path, updatedMenuList, userPermissions)) {
        // 有权限，继续导航
        console.log(`[路由守卫] ${to.path} 有权限访问，继续导航...`)
        return next({ ...to, replace: true })
      } else {
        // 无权限，重定向到首页
        console.warn(`[路由守卫] ${to.path} 无权限访问，重定向到 /dashboard`)
        return next("/dashboard")
      }
    }
  } else {
    // token 不存在，去登录
    console.log(`[路由守卫] Token 不存在，重定向到登录页`)
    return next({ path: "/login" })
  }
})