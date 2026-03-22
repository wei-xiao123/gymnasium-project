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
        ref="lostRef"
        :model="lostPerson"
        :rules="rules"
        label-width="80px"
        size="default"
      >
        <el-form-item prop="lostPerson" label="认领人">
          <el-input
            v-model="lostPerson.lostPerson"
            placeholder="请输入认领人"
          />
        </el-form-item>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import type { LostType } from '@/api/lost/LostModel'
import { editApi } from '@/api/lost'
import SysDialog from '@/components/SysDialog.vue'
import useDialog from '@/hooks/useDialog'

// 表单 ref
const lostRef = ref<FormInstance>()

// 表单数据对象
const lostPerson = reactive<LostType>({
  type: '',
  lostId: '',
  lostName: '',
  foundTime: '',
  foundAddres: '',
  foundPerson: '',
  foundPhone: '',
  status: '1',
  lostPerson: ''
})

// 弹框属性
const { dialog, onClose, onShow } = useDialog()

// 表单验证规则
const rules = reactive({
  lostPerson: [
    {
      required: true,
      message: '请录入认领人',
      trigger: 'blur'
    }
  ]
})

// 定义事件
const emits = defineEmits(['reFresh'])

// 显示弹框
const show = (row: LostType) => {
  dialog.title = '认领'
  dialog.height = 150
  lostPerson.lostId = row.lostId
  onShow()
  lostRef.value?.resetFields()
}

// 对外暴露
defineExpose({
  show
})

// 表单提交
const commit = () => {
  lostRef.value?.validate(async (valid) => {
    if (valid) {
      const res = await editApi(lostPerson)
      if (res && res.code === 200) {
        ElMessage.success('认领成功')
        emits('reFresh')
        onClose()
      }
    }
  })
}
</script>

<style scoped></style>
