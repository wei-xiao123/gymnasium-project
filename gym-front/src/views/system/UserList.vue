<template>
  <el-main>
    <!-- 搜索栏 -->
    <el-form :model="listParam" :inline="true" size="default">
      <el-form-item>
        <el-input v-model="listParam.phone" placeholder="请输入电话"></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="listParam.nickName" placeholder="请输入姓名"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="searchBtn" :icon="Search">搜索</el-button>
        <el-button @click="resetBtn" type="danger" plain :icon="Close">重置</el-button>
        <el-button v-permission="['sys:user:add']" type="primary" @click="addBtn":icon="Plus">新增</el-button>
      </el-form-item>
    </el-form>
    <!-- 表格 -->
    <el-table :height="tableHeight" :data="tableList" border stripe>
      <el-table-column prop="nickName" label="姓名"></el-table-column>
      <el-table-column prop="username" label="登录账户"></el-table-column>
      <el-table-column prop="sex" label="性别">
        <template #default="scope">
          <el-tag v-if="scope.row.sex == '0'" type="success" size="default">男</el-tag>
          <el-tag v-if="scope.row.sex == '1'" type="danger" size="default">女</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="userType" label="类型">
        <template #default="scope">
          <el-tag v-if="scope.row.userType == '1'" type="success" size="default">员工</el-tag>
          <el-tag v-if="scope.row.userType == '2'" type="danger" size="default">教练</el-tag>
        </template>
      </el-table-column>
      <!-- <el-table-column prop="roleName" label="角色"></el-table-column> -->
      <el-table-column prop="salary" label="薪水"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template #default="scope">
          <el-tag v-if="scope.row.status == '1'" type="success" size="default">启用</el-tag>
          <el-tag v-if="scope.row.status == '0'" type="danger" size="default">停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="320">
        <template #default="scope">
          <el-button v-permission="['sys:user:edit']" type="primary" :icon="Edit"
            size="default" @click="editBtn(scope.row)">编辑</el-button>
          <el-button v-permission="['sys:user:delete']" type="danger" :icon="Delete"
            size="default" @click="deleteBtn(scope.row)">删除</el-button>
          <el-button v-permission="['sys:user:resetPassword']" type="warning"
            :icon="Edit" size="default" @click="resetPasBtn(scope.row)">重置密码</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <el-pagination
      @size-change="sizeChange"
      @current-change="currentChange"
      v-model:current-page="listParam.currentPage"
      :page-sizes="[10, 20, 40, 80, 100]"
      v-model:page-size="listParam.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="listParam.total"
      background>
    </el-pagination>
    <!-- 新增、编辑弹框 -->
    <add-user ref="addRef" @refresh="refresh"></add-user>
  </el-main>
</template>

<script setup lang="ts">
import AddUser from "./AddUser.vue";
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import { ElMessage, ElMessageBox } from "element-plus";
import useTable from "@/composables/user/useTable";
import useUser from "@/composables/user/useUser";
import { resetPasswordApi } from "@/api/user";

// 表格相关
const {
  listParam,
  getList,
  searchBtn,
  resetBtn,
  tableList,
  sizeChange,
  currentChange,
  tableHeight,
  refresh
} = useTable();

// 新增、编辑、删除相关
const { addBtn, editBtn, deleteBtn, addRef } = useUser(getList);

// 重置密码
const resetPasBtn = async (row: any) => {
  try {
    await ElMessageBox.confirm(
      `确定重置用户 "${row.nickName}" 的密码为电话号码吗？`,
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    );
    
    // 重置密码为电话号码
    const res = await resetPasswordApi(row.userId, row.phone);
    
    if (res && res.code === 200) {
      ElMessage.success('重置密码成功');
      refresh();
    } else {
      ElMessage.error(res?.msg || '重置密码失败');
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('重置密码失败');
    }
  }
};
</script>

<style scoped></style>