<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input
          v-model="listParam.title"
          placeholder="请输入反馈标题"
        />
      </el-form-item>
      <el-form-item>
        <el-button :icon="Search" @click="searchBtn">搜索</el-button>
        <el-button :icon="Close" type="danger" plain @click="resetBtn">重置</el-button>
        <el-button
          :icon="Plus"
          v-permission="['sys:suggestList:add']"
          type="primary"
          @click="addBtn"
        >
          新增
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableData.list" border stripe>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="content" label="内容" />
      <el-table-column label="操作" align="center" width="210">
        <template #default="scope">
          <el-button
            v-permission="['sys:suggestList:edit']"
            :icon="Edit"
            type="primary"
            size="default"
            @click="editBtn(scope.row)"
          >
            编辑
          </el-button>
          <el-button
            v-permission="['sys:suggestList:delete']"
            :icon="Delete"
            type="danger"
            size="default"
            @click="deleteBtn(scope.row)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <el-pagination
      :current-page="listParam.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]"
      :page-size="listParam.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="listParam.total"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />

    <!-- 新增编辑 -->
    <AddSuggest ref="addRef" @reFresh="reFresh" />
  </el-main>
</template>

<script setup lang="ts">
import { Plus, Edit, Delete, Search, Close } from '@element-plus/icons-vue'
import AddSuggest from './AddSuggest.vue'
import useTable from '@/composables/suggest/useTable'
import useSuggest from '@/composables/suggest/useSuggest'

// 表格相关
const {
  listParam,
  tableData,
  tableHeight,
  searchBtn,
  resetBtn,
  getList,
  sizeChange,
  currentChange,
  reFresh
} = useTable()

// 新增编辑相关（addRef 在模板中通过 ref 属性被使用）
const {
  addBtn,
  editBtn,
  deleteBtn,
  // @ts-ignore
  addRef
} = useSuggest(getList)
</script>

<style scoped></style>