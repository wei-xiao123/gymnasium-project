<template>
	<SysDialog
		:title="dialog.title"
		:width="dialog.width"
		:height="dialog.height"
		:visible="dialog.visible"
		@onClose="onClose"
		@onConfirm="commit"
	>
		<template #content>
			<el-form
				ref="addFormRef"
				:model="addModel"
				:rules="rules"
				label-width="80px"
				size="default"
			>
				<el-row>
					<el-col :offset="0" :span="12">
						<el-form-item label="商品名称" prop="name">
							<el-input v-model="addModel.name" />
						</el-form-item>
					</el-col>
					<el-col :offset="0" :span="12">
						<el-form-item label="商品价格" prop="price">
							<el-input v-model="addModel.price" />
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :offset="0" :span="12">
						<el-form-item label="单位" prop="unit">
							<el-input v-model="addModel.unit" />
						</el-form-item>
					</el-col>
					<el-col :offset="0" :span="12">
						<el-form-item label="规格" prop="specs">
							<el-input v-model="addModel.specs" />
						</el-form-item>
					</el-col>
				</el-row>

				<el-row>
					<el-col :offset="0" :span="12">
						<el-form-item label="库存" prop="store">
							<el-input v-model="addModel.store" />
						</el-form-item>
					</el-col>
				</el-row>

				<el-form-item label="商品图片" prop="image">
					<el-upload
						ref="uploadRef"
						action="#"
						:auto-upload="false"
						:file-list="fileList"
						:limit="1"
						list-type="picture-card"
						:on-change="uploadFile"
					>
						<el-icon><Plus /></el-icon>
						<template #file="{ file }">
							<div>
								<img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
								<span class="el-upload-list__item-actions">
									<span class="el-upload-list__item-preview" @click="handlePictureCardPreview(file)">
										<el-icon><zoom-in /></el-icon>
									</span>
									<span
										v-if="!disabled"
										class="el-upload-list__item-delete"
										@click="handleRemove(file)"
									>
										<el-icon><Delete /></el-icon>
									</span>
								</span>
							</div>
						</template>
					</el-upload>
					<el-dialog v-model="dialogVisible">
						<img w-full :src="dialogImageUrl" alt="Preview Image" />
					</el-dialog>
				</el-form-item>

				<el-form-item label="商品详情" prop="details">
					<div style="border: 1px solid #ccc; position: relative; z-index: 1;">
						<Toolbar
							:defaultConfig="toolbarConfig"
							:editor="editorRef"
							:mode="mode"
							style="border-bottom: 1px solid #ccc"
						/>
						<Editor
							v-model="valueHtml"
							:defaultConfig="editorConfig"
							:mode="mode"
							style="height: 300px; overflow-y: hidden;"
							@onCreated="handleCreated"
						/>
					</div>
				</el-form-item>
			</el-form>
		</template>
	</SysDialog>
</template>

<script setup lang="ts">
import "@wangeditor/editor/dist/css/style.css";
// @ts-ignore
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { ElMessage, type FormInstance, type UploadFile } from "element-plus";
import { nextTick, reactive, ref } from "vue";
import { addApi, editApi } from "@/api/goods/index";
import type { GoodsType } from "@/api/goods/GoodsModel";
import useEditor from "@/composables/course/useEditor";
import useUpload from "@/composables/course/useUpload";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import useInstance from "@/hooks/useInstance";
import { EditType, Title } from "@/type/BaseEnum";

const { global } = useInstance();

// 图片上传
const {
	dialogImageUrl,
	dialogVisible,
	disabled,
	handleRemove,
	handlePictureCardPreview,
	uploadFile,
	uploadRef,
	imgurl,
	fileList,
} = useUpload();

// 文本编辑器
const { editorRef, handleCreated, mode, editorConfig, valueHtml, toolbarConfig } = useEditor();

// 表单 ref
const addFormRef = ref<FormInstance>();

// 弹框属性
const { dialog, onClose, onShow } = useDialog();

// 对外暴露
defineExpose({
	show,
});

type GoodsForm = Omit<GoodsType, "price" | "store"> & { price: number | null; store: number | null };

// 表单绑定对象
const addModel = reactive<GoodsForm>({
	type: "",
	goodsId: "",
	name: "",
	image: "",
	details: "",
	unit: "",
	specs: "",
	price: null,
	store: null,
});

const validatePrice = (_rule: any, value: any, callback: any) => {
	if (value == null || value <= 0) {
		callback(new Error("请填写商品价格"));
	} else {
		callback();
	}
};

const validateStore = (_rule: any, value: any, callback: any) => {
	if (value == null || value <= 0) {
		callback(new Error("请填写商品库存"));
	} else {
		callback();
	}
};

// 表单验证规则
const rules = reactive({
	name: [
		{
			required: true,
			trigger: "blur",
			message: "请输入商品名称",
		},
	],
	image: [
		{
			required: true,
			trigger: "blur",
			message: "请上传商品图片",
		},
	],
	unit: [
		{
			required: true,
			trigger: "blur",
			message: "请填写单位",
		},
	],
	details: [
		{
			required: true,
			trigger: "blur",
			message: "请填写商品详情",
		},
	],
	specs: [
		{
			required: true,
			trigger: "blur",
			message: "请填写商品规格",
		},
	],
	price: [
		{
			required: true,
			validator: validatePrice,
			trigger: "blur",
		},
	],
	store: [
		{
			required: true,
			validator: validateStore,
			trigger: "blur",
		},
	],
});

// 注册事件
const emits = defineEmits(["reFresh"]);

// 对外方法
const resetAddModel = () => {
	addModel.type = "";
	addModel.goodsId = "";
	addModel.name = "";
	addModel.image = "";
	addModel.details = "";
	addModel.unit = "";
	addModel.specs = "";
	addModel.price = null;
	addModel.store = null;
};

function show(type: string, row?: GoodsType) {
	fileList.value = [];
	imgurl.value = "";

	if (type === EditType.ADD) {
		dialog.title = Title.ADD;
	} else {
		dialog.title = Title.EDIT;
	}
	dialog.width = 900;
	dialog.height = 500;

	const upload = uploadRef.value;
	if (upload) {
		upload.clearFiles();
	}

	if (type === EditType.ADD) {
		resetAddModel();
		valueHtml.value = "";
		const editor = editorRef.value;
		if (editor) {
			editor.clear();
		}
	}

	if (type === EditType.EDIT) {
		nextTick(() => {
			global.$objCopy(row, addModel);

			if (row?.image) {
				const img: UploadFile = {
					name: "image",
					url: row.image,
					status: "success",
					uid: Date.now(),
				};
				imgurl.value = row.image;
				fileList.value.push(img);
			}

			// 用 setTimeout 确保编辑器完全渲染后再设置内容
			setTimeout(() => {
				if (row && row.details) {
					valueHtml.value = row.details;
				}
				const editor = editorRef.value;
				if (editor && typeof editor.focus === 'function') {
					editor.focus();
				}
			}, 100);
		});
	}

	onShow();
	addFormRef.value?.clearValidate();
	addModel.type = type;
}

// 提交表单
const commit = () => {
	addModel.image = imgurl.value;
	addModel.details = valueHtml.value;

	addFormRef.value?.validate(async (valid) => {
		if (valid) {
			let res = null;
			// 确保类型转换，price 和 store 转为 number
			const submitData = {
				...addModel,
				price: Number(addModel.price),
				store: Number(addModel.store)
			};
			if (addModel.type === EditType.ADD) {
				res = await addApi(submitData as GoodsType);
			} else {
				res = await editApi(submitData as GoodsType);
			}

			if (res && res.code === 200) {
				ElMessage.success(res.msg);
				emits("reFresh");
				onClose();
			}
		}
	});
};
</script>

<style scoped></style>