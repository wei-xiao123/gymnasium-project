<template>
  <div class="header-container">
    <Collapse />
    <BreadCrumb />
  </div>
</template>

<script setup lang="ts">
import BreadCrumb from './BreadCrumb.vue'
import Collapse from './Collapse.vue'
</script>

<style scoped lang="scss">
.header-container {
  display: flex;
  align-items: center;
  gap: 20px;
  width: 100%;
  height: 100%;
}
</style>