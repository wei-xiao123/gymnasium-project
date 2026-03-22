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
        <el-form-item prop="title" label="标题">
          <el-input v-model="addModel.title" />
        </el-form-item>
        <el-form-item prop="content" label="内容">
          <el-input v-model="addModel.content" type="textarea" />
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { nextTick, reactive, ref } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import type { SuggestType } from '@/api/suggest/SuggestModel'
import { addApi, editApi } from '@/api/suggest/index'
import SysDialog from '@/components/SysDialog.vue'
import useDialog from '@/hooks/useDialog'
import useInstance from '@/hooks/useInstance'
import { EditType, Title } from '@/type/BaseEnum'

const { global } = useInstance()

// 表单 ref
const addFormRef = ref<FormInstance>()

// 弹框属性
const { dialog, onClose, onShow } = useDialog()

// 表单数据对象
const addModel = reactive<SuggestType>({
  type: '',
  id: '',
  title: '',
  content: ''
})

// 表单验证规则
const rules = reactive({
  title: [
    {
      required: true,
      trigger: 'blur',
      message: '请输入标题'
    }
  ],
  content: [
    {
      required: true,
      trigger: 'blur',
      message: '请输入内容'
    }
  ]
})

// 定义事件
const emits = defineEmits(['reFresh'])

// 显示弹框
const show = (type: string, row?: SuggestType) => {
  dialog.height = 160
  dialog.title = type === EditType.ADD ? Title.ADD : Title.EDIT

  // 编辑时回显数据
  if (type === EditType.EDIT) {
    nextTick(() => {
      global.$objCopy(row, addModel)
    })
  }

  onShow()
  addFormRef.value?.resetFields()
  addModel.type = type
}

// 对外暴露
defineExpose({
  show
})

// 表单提交
const commit = () => {
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      let res = null
      if (addModel.type === EditType.ADD) {
        res = await addApi(addModel)
      } else {
        res = await editApi(addModel)
      }

      if (res && res.code === 200) {
        ElMessage.success(res.msg)
        emits('reFresh')
        onClose()
      }
    }
  })
}
</script>

<style scoped></style>