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
      <div>请选择会员卡类型</div>
      <el-divider style="margin: 10px 0px" />
      <el-select
        ref="selectRef"
        style="margin-top: 10px; width: 100%"
        v-model="param.cardId"
        class="m-2"
        placeholder="请选择会员卡类型"
        size="large"
      >
        <el-option
          v-for="item in cardList"
          :key="item.cardId"
          :label="item.title"
          :value="item.cardId"
        />
      </el-select>
    </template>
  </SysDialog>
</template>
<script setup lang="ts">
import type { MemberType, ApplyCard } from "@/api/member/MemberModel";
import SysDialog from "@/components/SysDialog.vue";
import useDialog from "@/hooks/useDialog";
import useJoin from "@/composables/member/useJoin";
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { applySaveApi } from "@/api/member/index";

// 选择的卡类型
const param = reactive<ApplyCard>({
  cardId: "",
  memberId: "",
});

// select 组件实例
const selectRef = ref();

// 弹框属性
const { dialog, onClose, onConfirm, onShow } = useDialog();

// 会员卡数据
const { cardList } = useJoin();

// 显示弹框
const show = (row: MemberType) => {
  // 完全重置所有表单数据
  param.cardId = "";
  param.memberId = row.memberId;
  // 清空 select 的选中值
  selectRef.value && (selectRef.value.modelValue = "");
  dialog.title = "为【" + row.name + "】办卡";
  onShow();
};

// 暴露给父组件调用
defineExpose({
  show,
});

// 注册事件
const emits = defineEmits<{
  refresh: [];
}>();

// 提交办卡
const commit = async () => {
  if (!param.cardId) {
    ElMessage.warning("请选择会员卡类型!");
    return;
  }
  const res = await applySaveApi(param);
  if (res && res.code == 200) {
    ElMessage.success(res.msg);
    emits("refresh");
    onClose();
  }
};
</script>
<style scoped></style>