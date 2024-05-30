<template>
  <div>
    <div class="container">
      <div class="greeting-card">
        <h2 class="greeting-title">
          欢迎您，
          <span class="username" :style="{ color: usernameColor }">
          {{ username || '访客' }}
        </span>
          ，祝您今天过得开心！
        </h2>
      </div>
    </div>
  </div>

</template>

<script setup>
import { computed, ref, onMounted } from 'vue';

const adminUser = ref(JSON.parse(localStorage.getItem('admin-user') || '{}'));

const username = computed(() => adminUser.value|| '访客');
const usernameColor = computed(() => adminUser.value ? 'dodgerblue' : 'gray'); // 如果没有用户名，使用灰色

</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh; /* 视口高度，可根据需要调整 */
  text-align: center;
  background-color: #f5f5f5; /* 淡灰色背景 */
}

.greeting-card {
  display: inline-block;
  text-align: left; /* 文本左对齐 */
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  background-color: #fff;
  max-width: 300px; /* 限制卡片最大宽度 */
}

.greeting-title {
  margin: 0 0 10px; /* 上下外边距 */
  line-height: 1.5; /* 行高 */
}

.username {
  font-weight: bold; /* 用户名加粗 */
}
</style>