<template>
  <div>
    <div style="height: 60px; background-color: #fff; display: flex; align-items: center; border-bottom: 1px solid #ddd">
      <div style="flex: 1">
        <div style="padding-left: 20px; display: flex; align-items: center">
          <img src="@/assets/imgs/img.png" alt="" style="width: 40px">
          <div style="font-weight: bold; font-size: 24px; margin-left: 5px">学生成绩管理系统</div>
        </div>
      </div>
      <div style="width: fit-content; padding-right: 10px; display: flex; align-items: center;">
        <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="" style="width: 40px; height: 40px">
        <span style="margin-left: 5px">{{data.user}}</span>
      </div>
    </div>

    <div style="display: flex">
      <div style="width: 200px; height: 680px; border-right: 1px solid #ddd; ">
        <el-menu
            router
            style="border: none"
            :default-active="$route.path"
            :default-openeds="['/studentHome', '2']"
        >
          <el-menu-item index="/studentHome">
            <el-icon><HomeFilled /></el-icon>
            <span>系统首页</span>
          </el-menu-item>

          <el-menu-item index="/studentGrade">
            <el-icon><HomeFilled /></el-icon>
            <span>成绩信息</span>
          </el-menu-item>

          <el-menu-item index="login" @click="logout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出系统</span>
          </el-menu-item>
        </el-menu>
      </div>

      <div style="flex: 1; width: 0; background-color: #f8f8ff; padding: 10px">
        <router-view />
      </div>
    </div>

  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import '../assets/css/global.css'
import {HomeFilled} from "@element-plus/icons-vue";
const $route = useRoute()
console.log($route.path)

const data = reactive({
  user:JSON.parse(localStorage.getItem('student-user') || '{}')
})

const logout = () => {
  localStorage.removeItem('student-user')
  location.href = '/'
}
</script>

<style lang="less" scoped>

.el-menu-item.is-active {
  background-color: #dcede9 !important;
}
.el-menu-item:hover {
  color: #11A983;
}
:deep(th)  {
  color: #333;
}
</style>