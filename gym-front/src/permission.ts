import type { Directive, DirectiveBinding } from "vue";
import router from "./router";
import { userStore } from "./store/user";
import { menuStore } from "./store/menu";

// 白名单 不需要权限可以访问
const whiteList = ["/login"];

// 全局守卫路由
router.beforeEach(async (to, _from, next) => {
  // 获取用户的store
  const ustore = userStore();
  // 获取菜单的store
  const mstore = menuStore();
  // 获取token
  const token = ustore.getToken;
  // 判断token是否存在
  if (token) {
    // 存在
    if (to.path === "/login") {
      next({ path: "/" });
    } else {
      const menuList = mstore.getMenuList;
      if (menuList.length > 0) {
        next();
      } else {
        try {
          // 获取用户信息
          await ustore.getInfo();
          // 获取菜单信息,动态生成路由
          await mstore.getMenu(router, {
            userId: ustore.getUserId,
            userType: ustore.getUserType,
          });
          // 等待路由全部挂载
          next({ ...to, replace: true });
        } catch (error) {
          localStorage.clear();
          next({ path: "/login" });
        }
      }
    }
  } else {
    // 不存在
    if (whiteList.indexOf(to.path) !== -1) {
      // 存在白名单，直接放行
      next();
    } else {
      localStorage.clear();
      // 去登录
      next({ path: "/login" });
    }
  }
});

// 权限指令
export const permissionDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding<string[]>) {
    const { value } = binding;
    const mstore = menuStore();
    const menuList = mstore.getMenuList;

    if (value && value instanceof Array) {
      const hasPermission = value.some((permission: string) => {
        return menuList.some((menu: any) => menu.permission === permission);
      });

      if (!hasPermission) {
        el.style.display = "none";
      }
    }
  },
};