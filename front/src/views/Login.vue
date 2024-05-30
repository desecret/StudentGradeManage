<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="login-title">登录</h1>
      <el-form :model="data.form" ref="formRef" :rules="rules">
        <el-form-item prop="account">
          <el-input prefix-icon="el-icon-user" v-model="data.form.account" placeholder="请输入账号"/>
        </el-form-item>
        <el-form-item prop="password">
          <el-input prefix-icon="el-icon-lock" v-model="data.form.password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-button" @click="login">登录</el-button>
        </el-form-item>
      </el-form>
      <div class="role-selector">
        <el-radio-group v-model="radio">
          <el-radio :value="1" class="role-radio">学生</el-radio>
          <el-radio :value="2" class="role-radio">教师</el-radio>
          <el-radio :value="3" class="role-radio">管理员</el-radio>
        </el-radio-group>
      </div>
    </div>
  </div>
</template>

<script setup>
  import request from '@/utils/request.js';
  import {ElMessage} from "element-plus";

  const data = reactive({
    form: {}
  })

  //选择角色
  const radio = ref(0)

  // 表单校验
  const rules = reactive({
    account: [
      { required: true, message: '请输入账号', trigger: 'blur' }
    ],
    password: [
      { required: true, message: '请输入密码', trigger: 'blur' }
    ]
  })
  const formRef = ref()
  //登录校验
  const login = () => {
    formRef.value.validate((valid) => {
      //valid的值是true or false，表示点击登录的时候输入框中有没有值
      if(valid){
        //根据选择的角色判断发送的请求地址
        switch (radio.value){
          case 0 :
            ElMessage.error('请先选择角色')
            break
          //学生
          case 1 :
            request.post('http://localhost:8080/student/login',data.form).then(res => {
              console.log(res)
              if(res !== 201){
                console.log(typeof res)
                console.log(res)
                localStorage.setItem('student-user-id',JSON.stringify(data.form.account)) //保存登录时输入的id，用来获得对应学生的课程成绩
                localStorage.setItem('student-user',JSON.stringify(res))  //保存姓名
                ElMessage.success('登陆成功')
                location.href = '/studentHome'
              } else {
                ElMessage.error('用户名或密码错误')
              }
            })
            break

          //老师
          case 2 :
            request.post('http://localhost:8080/teacher/login',data.form).then(res => {
              if(res !== 201){
                console.log(typeof res)
                console.log(res)
                localStorage.setItem('teacher-user-id',JSON.stringify(data.form.account)) //保存登录时输入的id，用来获得对应教师的课程成绩
                localStorage.setItem('teacher-user',JSON.stringify(res))  //保存姓名
                ElMessage.success('登陆成功')
                location.href = '/teacherHome'
              } else {
                ElMessage.error('用户名或密码错误')
              }
            })
            break

          //管理员
          case 3 :
            request.post('http://localhost:8080/administration/login',data.form).then(res => {
              if(res !== 201){
                console.log(typeof res)
                console.log(res)
                localStorage.setItem('admin-user',JSON.stringify(res))
                ElMessage.success('登陆成功')
                location.href = '/adminHome'
              } else {
                ElMessage.error('用户名或密码错误')
              }
            })
        }

      }
    })
  }
</script>

<style>
body {
  background-image: url("../assets/imgs/login.png");
  background-size: cover;
  background-position: center;
  font-family: 'Arial', sans-serif;
}

.login-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  margin: 0;
}

.login-box {
  width: 350px;
  padding: 40px;
  background: rgba(255, 255, 255, 0.1);
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.5);
  border-radius: 8px;
  backdrop-filter: blur(10px);
  color: #fff;
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 28px;
  color: #fff;
}

.login-button, .register-button {
  width: 100%;
  margin-top: 10px;
  padding: 10px;
  border-radius: 5px;
  transition: background-color 0.3s;
}

.login-button:hover, .register-button:hover {
  background-color: #5cb85c;
}

.role-selector {
  margin-top: 20px;
}

.role-radio {
  display: block;
  margin-bottom: 10px;
}
</style>