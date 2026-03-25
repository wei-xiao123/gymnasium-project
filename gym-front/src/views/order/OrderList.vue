<template>
  <div class="order-container">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>订单管理</span>
          <el-button type="primary">新增订单</el-button>
        </div>
      </template>
      <el-table :data="orderList" row-key="orderId" style="width: 100%">
        <el-table-column prop="orderId" label="订单ID" width="120" />
        <el-table-column prop="userId" label="用户ID" width="120" />
        <el-table-column prop="orderNumber" label="订单号" width="150" />
        <el-table-column prop="totalPrice" label="总价" width="100" />
        <el-table-column prop="status" label="状态" width="100" />
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="120" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue"

const orderList = ref<any[]>([
  {
    orderId: 1,
    userId: 1,
    orderNumber: "ORD-001",
    totalPrice: 99.99,
    status: "已完成",
    createTime: "2024-01-01 10:00:00",
  },
  {
    orderId: 2,
    userId: 2,
    orderNumber: "ORD-002",
    totalPrice: 199.99,
    status: "待发货",
    createTime: "2024-01-02 11:00:00",
  },
])

const handleEdit = (row: any) => {
  console.log("编辑订单:", row)
}

const handleDelete = (row: any) => {
  console.log("删除订单:", row)
}
</script>

<style scoped>
.order-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
