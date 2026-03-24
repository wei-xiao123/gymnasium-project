<template>
  <div class="not-found-container">
    <div class="not-found-content">
      <h1 class="error-code">404</h1>
      <h2 class="error-title">页面不存在或无权限访问</h2>
      <p class="error-description">抱歉，您访问的页面不存在或您没有权限访问此页面。</p>
      <button class="back-button" @click="goBack">返回首页</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router"

const router = useRouter()

const goBack = () => {
  router.push("/dashboard")
}
</script>

<style scoped>
.not-found-container {
  width: 100%;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.not-found-content {
  text-align: center;
  color: white;
}

.error-code {
  font-size: 120px;
  font-weight: bold;
  margin: 0;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3);
}

.error-title {
  font-size: 32px;
  margin: 20px 0 10px 0;
}

.error-description {
  font-size: 18px;
  margin-bottom: 40px;
  opacity: 0.9;
}

.back-button {
  padding: 12px 32px;
  font-size: 16px;
  background-color: white;
  color: #667eea;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.back-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}
</style>
