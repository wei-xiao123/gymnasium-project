<template>
  <SysDialog
    :title="dialog.title"
    :height="dialog.height"
    :width="dialog.width"
    :visible="dialog.visible"
    @onClose="onClose"
    @onConfirm="commit"
  >
    <template v-slot:content>
      <el-form
        :model="addModel"
        ref="addFormRef"
        :rules="rules"
        label-width="80px"
        size="default"
      >
        <el-row>
          <el-col :span="12">
            <el-form-item prop="nickName" label="姓名">
              <el-input v-model="addModel.nickName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="sex" label="性别">
              <el-radio-group v-model="addModel.sex">
                <el-radio :label="'0'">男</el-radio>
                <el-radio :label="'1'">女</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="phone" label="电话">
              <el-input v-model="addModel.phone"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="email" label="邮箱">
              <el-input v-model="addModel.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="salary" label="薪水">
              <el-input v-model="addModel.salary"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="userType" label="类型">
              <el-radio-group v-model="addModel.userType">
                <el-radio :label="'1'">员工</el-radio>
                <el-radio :label="'2'">教练</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="roleId" label="角色">
              <el-select
                v-model="addModel.roleId"
                placeholder="请选择角色"
                size="default"
              >
                <el-option
                  v-for="item in roleData.list"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="status" label="状态">
              <el-radio-group v-model="addModel.status">
                <el-radio :label="'0'">停用</el-radio>
                <el-radio :label="'1'">启用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item prop="username" label="账户">
              <el-input v-model="addModel.username"></el-input>
            </el-form-item>
          </el-col>
          <el-col v-if="addModel.type === EditType.ADD" :span="12">
            <el-form-item prop="password" label="密码">
              <el-input v-model="addModel.password" type="password"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
  </SysDialog>
</template>

<script setup lang="ts">
import type { AddUserModel } from "@/api/user/UserModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import { reactive, ref } from "vue";
import useSelectRole from "@/composables/user/useSelectRole";
import { ElMessage } from "element-plus";
import type { FormInstance } from "element-plus";
import { addApi, editApi } from "@/api/user/index";
import { EditType, Title } from "@/type/BaseEnum";

// 表单 ref 属性
const addFormRef = ref<FormInstance>();

// 角色
const { roleData, listRole, roleId, getRole } = useSelectRole();

// 弹框属性
const { dialog, onClose, onShow } = useDialog();


// 表单绑定的对象
const addModel = reactive<AddUserModel>({
  userId: "",
  type: "",
  roleId: "",
  username: "",
  password: "",
  phone: "",
  email: "",
  sex: "",
  userType: "",
  status: "",
  salary: "",
  nickName: ""
});

// 表单验证规则
const rules = reactive({
  nickName: [
    {
      required: true,
      trigger: "change",
      message: "请输入姓名"
    }
  ],
  phone: [
    {
      required: true,
      trigger: "change",
      message: "请输入电话"
    }
  ],
  sex: [
    {
      required: true,
      trigger: "change",
      message: "请选择性别"
    }
  ],
  userType: [
    {
      required: true,
      trigger: "change",
      message: "请选择类型"
    }
  ],
  status: [
    {
      required: true,
      trigger: "change",
      message: "请选择状态"
    }
  ],
  salary: [
    {
      required: true,
      trigger: "change",
      message: "请输入薪水"
    }
  ],
  username: [
    {
      required: true,
      trigger: "change",
      message: "请输入账户"
    }
  ],
  password: [
    {
      required: true,
      trigger: "change",
      message: "请输入密码"
    }
  ],
  roleId: [
    {
      required: true,
      trigger: "change",
      message: "请选择角色"
    }
  ]
});

// 注册事件
const emits = defineEmits(["refresh"]);

// 显示弹框
const show = async (type: string, row?: AddUserModel) => {
  dialog.height = 270;
  addModel.type = type;

  // 获取角色数据
  await listRole();

  // 设置弹框标题
  dialog.title = type === EditType.ADD ? Title.ADD : Title.EDIT;

  // 编辑时回显数据
  if (type === EditType.EDIT && row) {
    // 复制数据
    Object.keys(row).forEach(key => {
      (addModel as any)[key] = (row as any)[key];
    });
    // 获取用户的角色
    await getRole(row.userId);
    addModel.roleId = roleId.value;
  } else if (type === EditType.ADD) {
    // 新增时清空表单
    roleId.value = "";
    addModel.roleId = "";
    addFormRef.value?.resetFields();
  }

  onShow();
};

// 暴露出去给父组件调用
defineExpose({
  show
});

// 表单提交
const commit = () => {
  addFormRef.value?.validate(async (valid) => {
    if (valid) {
      try {
        let res = null;
        if (addModel.type === EditType.ADD) {
          res = await addApi(addModel);
        } else {
          res = await editApi(addModel);
        }

        if (res && res.code === 200) {
          ElMessage.success(res.msg);
          // 刷新表格
          emits("refresh");
          onClose();
        }
      } catch (error) {
        ElMessage.error("操作失败");
      }
    }
  });
};
</script>

<style scoped></style>