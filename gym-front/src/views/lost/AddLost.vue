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
          <el-col :span="12">
            <el-form-item prop="lostName" label="物品名称">
              <el-input v-model="addModel.lostName" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="foundTime" label="捡到时间">
              <el-date-picker
                v-model="addModel.foundTime"
                type="date"
                placeholder="请选择捡到时间"
                value-format="YYYY-MM-DD"
                format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="foundAddres" label="捡到地址">
              <el-input v-model="addModel.foundAddres" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="foundPerson" label="捡到人">
              <el-input v-model="addModel.foundPerson" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="foundPhone" label="联系电话">
              <el-input v-model="addModel.foundPhone" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="status" label="状态">
              <el-radio-group v-model="addModel.status">
                <el-radio :label="'0'">未认领</el-radio>
                <el-radio :label="'1'">已认领</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import { nextTick, reactive, ref } from 'vue'
import { ElMessage, type FormInstance } from 'element-plus'
import { addApi, editApi } from '@/api/lost/index'
import type { LostType } from '@/api/lost/LostModel'
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
const addModel = reactive<LostType>({
  type: '',
  lostId: '',
  lostName: '',
  foundTime: '',
  foundAddres: '',
  foundPerson: '',
  foundPhone: '',
  status: '',
  lostPerson: ''
})

// 表单验证规则
const rules = reactive({
  lostName: [
    {
      required: true,
      trigger: 'blur',
      message: '请填写物品名称'
    }
  ],
  foundTime: [
    {
      required: true,
      trigger: 'blur',
      message: '请填写捡到时间'
    }
  ],
  foundAddres: [
    {
      required: true,
      trigger: 'blur',
      message: '请填写捡到地址'
    }
  ],
  foundPerson: [
    {
      required: true,
      trigger: 'blur',
      message: '请填写捡到人'
    }
  ],
  foundPhone: [
    {
      required: true,
      trigger: 'blur',
      message: '请填写捡到人电话'
    }
  ],
  status: [
    {
      required: true,
      trigger: 'blur',
      message: '请选择认领状态'
    }
  ]
})

// 定义事件
const emits = defineEmits(['reFresh'])

// 显示弹框
const show = (type: string, row?: LostType) => {
  // 设置弹框标题
  dialog.title = type === EditType.ADD ? Title.ADD : Title.EDIT
  dialog.height = 180

  // 编辑时回显数据
  if (type === EditType.EDIT) {
    nextTick(() => {
      global.$objCopy(row, addModel)
    })
  }

  onShow()
  addModel.type = type
  addFormRef.value?.resetFields()
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