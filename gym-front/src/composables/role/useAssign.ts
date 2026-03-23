import { getMenuTreeApi } from "@/api/role"
import type { AssignParam } from "@/api/role/RoleModel"
import { reactive } from "vue"
import { ElMessage } from "element-plus"

export default function useAssign() {
  // 树形菜单的属性配置
  const defaultProps = reactive({
    children: "children",
    label: "title",
  })

  // 权限树数据
  const assignTreeData = reactive({
    list: [],
    assignTreeChecked: [] as number[], // 原来分配的权限ID集合
  })

  /**
   * 获取权限树数据
   * @param param 参数
   */
  const getMenuTree = async (param: AssignParam) => {
    try {
      const res = await getMenuTreeApi(param)
      if (res && res.code === 200) {
        // 设置权限树数据
        assignTreeData.list = res.data.listmenu || []
        // 设置角色原来的权限ID
        assignTreeData.assignTreeChecked = res.data.checkList || []

        // 数据回显，判断角色原来是否已经分配过权限，如果有，回显
        if (assignTreeData.assignTreeChecked.length > 0) {
          const newArr: number[] = []
          assignTreeData.assignTreeChecked.forEach((item) => {
            checked(item, assignTreeData.list, newArr)
          })
          assignTreeData.assignTreeChecked = newArr
        }
      } else {
        ElMessage.error(res?.msg || "获取权限树数据失败")
      }
    } catch (error) {
      console.error("获取权限树数据错误:", error)
      ElMessage.error("获取权限树数据异常")
    }
  }

  /**
   * 递归检查权限ID，取出叶子节点权限ID
   * @param id 菜单ID
   * @param data 菜单树数据
   * @param newArr 结果数组
   */
  const checked = (id: number, data: any[], newArr: number[]): void => {
    data.forEach((item: any) => {
      if (item.menuId === id) {
        // 是叶子节点才添加
        if (!item.children || item.children.length === 0) {
          newArr.push(item.menuId)
        }
      } else {
        // 继续递归
        if (item.children && item.children.length > 0) {
          checked(id, item.children, newArr)
        }
      }
    })
  }

  return {
    defaultProps,
    assignTreeData,
    getMenuTree,
  }
}