<template>
  <el-main style="padding-top: 10px">
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input
          v-model="listParam.roleName"
          placeholder="请输入角色名称"
          clearable
        />
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button :icon="Close" type="danger" plain @click="resetBtn">重置</el-button>
        <el-button :icon="Plus" type="primary" @click="addBtn">新增</el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :data="tableList.list" :height="tableHeight" border stripe row-key="roleId">
      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="remark" label="角色描述" />
      <el-table-column label="操作" width="320" align="center">
        <template #default="scope">
          <el-button :icon="Edit" type="primary" size="default" @click="editBtn(scope.row)">
            编辑
          </el-button>
          <el-button :icon="Edit" type="success" size="default" @click="assignBtn(scope.row)">
            分配权限
          </el-button>
          <el-button :icon="Delete" type="danger" size="default" @click="deleteBtn(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="listParam.currentPage"
      :page-size="listParam.pageSize"
      :page-sizes="[10, 20, 40, 80, 100]"
      :total="listParam.total"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />

    <!-- 新增、编辑弹框 -->
    <role-add ref="addRef" @refresh="refresh" />

    <!-- 分配权限弹框 -->
    <assign-role ref="assignRoleRef" />
  </el-main>
</template>

<script setup lang="ts">
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue"
import { ref } from "vue"
import RoleAdd from "./RoleAdd.vue"
import AssignRole from "./AssignRole.vue"
import useTable from "@/composables/role/useTable"
import useRole from "@/composables/role/useRole"

// 定义 refs
const addRef = ref<InstanceType<typeof RoleAdd>>()
const assignRoleRef = ref<InstanceType<typeof AssignRole>>()

// 表格相关业务逻辑
const {
  listParam,
  getList,
  searchBtn,
  resetBtn,
  tableList,
  sizeChange,
  currentChange,
  tableHeight,
  refresh,
} = useTable()

// 新增、编辑、删除、分配权限业务逻辑 - 传入 refs
const { addBtn: composableAddBtn, editBtn: composableEditBtn, deleteBtn, assignBtn: composableAssignBtn } = useRole(getList, {
  addRef,
  assignRoleRef
})

// 重新定义按钮方法以使用本地ref
const addBtn = () => composableAddBtn()
const editBtn = (row: any) => composableEditBtn(row)
const assignBtn = (row: any) => composableAssignBtn(row)
</script>

<style scoped></style>