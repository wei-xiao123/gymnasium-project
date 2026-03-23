import { getMenuTreeApi } from "@/api/role"
import { getListApi } from "@/api/menu"
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
  const assignTreeData = reactive<{
    list: any[]
    assignTreeChecked: number[]
  }>({
    list: [],
    assignTreeChecked: [],
  })

  /**
   * 获取权限树数据
   * @param param 参数
   */
  const getMenuTree = async (param: AssignParam) => {
    try {
      // 获取菜单树和已分配的权限
      const [menuRes, assignRes] = await Promise.all([
        getListApi(),           // 获取完整菜单树
        getMenuTreeApi(param)   // 获取已分配权限ID
      ])
      
      console.log("菜单树API返回:", menuRes)
      console.log("已分配权限API返回:", assignRes)
      
      if (menuRes && menuRes.code === 200) {
        // 处理菜单树数据
        const menuData = Array.isArray(menuRes.data) ? menuRes.data : (menuRes.data?.list || [])
        console.log("提取的菜单树数据:", menuData)
        
        assignTreeData.list = menuData
        
        // 获取已分配的权限
        if (assignRes && assignRes.code === 200) {
          const checkList = assignRes.data?.checkList || []
          console.log("已分配权限:", checkList)
          
          assignTreeData.assignTreeChecked = checkList
          
          // 数据回显，判断角色原来是否已经分配过权限，如果有，回显
          if (checkList.length > 0 && menuData.length > 0) {
            const newArr: number[] = []
            checkList.forEach((item: number) => {
              checked(item, menuData, newArr)
            })
            assignTreeData.assignTreeChecked = newArr
          }
        }
        
        console.log("最终树数据:", assignTreeData.list)
        console.log("最终已分配权限:", assignTreeData.assignTreeChecked)
      } else {
        ElMessage.error(menuRes?.msg || "获取菜单数据失败")
        assignTreeData.list = []
      }
    } catch (error) {
      console.error("获取权限树数据错误:", error)
      ElMessage.error("获取权限树数据异常")
      assignTreeData.list = []
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