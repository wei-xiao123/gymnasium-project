<template>
	<el-main>
		<!-- 搜索栏 -->
		<el-form :inline="true" :model="listParam" size="default">
			<el-form-item>
				<el-input v-model="listParam.name" label="请输入商品名称" />
			</el-form-item>

			<el-form-item>
				<el-button :icon="Search" @click="searchBtn">搜索</el-button>
				<el-button :icon="Close" plain type="danger" @click="resetBtn">重置</el-button>
				<el-button
					v-permission="['sys:goodsList:add']"
					:icon="Plus"
					type="primary"
					@click="addBtn"
				>
					新增
				</el-button>
			</el-form-item>
		</el-form>

		<!-- 表格 -->
		<el-table :data="tableData.list" :height="tableHeight" border stripe>
			<el-table-column align="center" label="商品图片" prop="image" width="90">
				<template #default="scope">
					<el-image
						:src="scope.row.image"
						style="width: 60px; height: 60px; border-radius: 50%"
					/>
				</template>
			</el-table-column>
			<el-table-column label="商品名称" prop="name" />
			<el-table-column label="商品价格" prop="price" />
			<el-table-column label="商品库存" prop="store" />
			<el-table-column label="单位" prop="unit" />
			<el-table-column label="规格" prop="specs" />
			<el-table-column align="center" label="操作" width="220">
				<template #default="scope">
					<el-button
						v-permission="['sys:goodsList:edit']"
						:icon="Edit"
						size="default"
						type="primary"
						@click="editBtn(scope.row)"
					>
						编辑
					</el-button>
					<el-button
						v-permission="['sys:goodsList:delete']"
						:icon="Delete"
						size="default"
						type="danger"
						@click="deleteBtn(scope.row)"
					>
						删除
					</el-button>
				</template>
			</el-table-column>
		</el-table>

		<!-- 分页 -->
		<el-pagination
			:current-page.sync="listParam.currentPage"
			:page-size="listParam.pageSize"
			:page-sizes="[10, 20, 40, 80, 100]"
			:total="listParam.total"
			layout="total, sizes, prev, pager, next, jumper"
			background
			@size-change="sizeChange"
			@current-change="currentChange"
		/>

		<!-- 新增弹框 -->
		<AddGoods ref="addRef" @reFresh="reFresh" />
	</el-main>
</template>

<script setup lang="ts">
import { Plus, Edit, Delete, Search, Close } from "@element-plus/icons-vue";
import AddGoods from "./AddGoods.vue";
import useGoods from "@/composables/goods/useGoods";
import useTable from "@/composables/goods/useTable";

const {
	tableHeight,
	listParam,
	searchBtn,
	resetBtn,
	getList,
	tableData,
	sizeChange,
	currentChange,
	reFresh,
} = useTable();

const { addBtn, editBtn, deleteBtn, addRef } = useGoods(getList);
</script>

<style scoped></style>