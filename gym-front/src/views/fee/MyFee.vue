<template>
  <el-main>
    <el-table :height="tableHeight" :data="tableData.list" border stripe :row-key="(row, index) => index">
      <el-table-column label="会员卡号" prop="username" />
      <el-table-column label="会员姓名" prop="name" />
      <el-table-column label="充值金额" prop="money" />
      <el-table-column label="充值时间" prop="createTime" />
      <el-table-column label="操作人" prop="createUser" />
    </el-table>
    <el-pagination
      :current-page="listParam.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]"
      :page-size="listParam.pageSize"
      :total="listParam.total"
      layout="total, sizes, prev, pager, next, jumper"
      background
      @size-change="sizeChange"
      @current-change="currentChange"
    />
  </el-main>
</template>

<script setup lang="ts">
import { nextTick, onMounted, reactive, ref } from "vue"
import type { MemberRecharge } from "@/api/member/MemberModel"
import { getMyRechargeApi } from "@/api/member"
import { userStore } from "@/store/user"

const store = userStore()

// 表格高度
const tableHeight = ref(0)

// 表格查询的参数
const listParam = reactive<MemberRecharge>({
  currentPage: 1,
  pageSize: 10,
  total: 0,
  memberId: "",
  userType: "",
})

// 表格数据
const tableData = reactive({
  list: [],
})

/**
 * 查询表格数据
 */
const getList = async () => {
  listParam.memberId = store.getUserId()
  listParam.userType = store.getUserType()
  
  const res = await getMyRechargeApi(listParam)
  if (res && res.code === 200) {
    tableData.list = res.data.records
    listParam.total = res.data.total
  }
}

/**
 * 页容量改变时触发
 */
const sizeChange = (size: number) => {
  listParam.pageSize = size
  getList()
}

/**
 * 页数改变时触发
 */
const currentChange = (page: number) => {
  listParam.currentPage = page
  getList()
}

onMounted(() => {
  nextTick(() => {
    tableHeight.value = window.innerHeight - 230
  })
  getList()
})
</script>

<style scoped></style>