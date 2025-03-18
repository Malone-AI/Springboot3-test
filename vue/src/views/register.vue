<template>
  <div style="display: flex;align-items: center;justify-content: center;background-color: aquamarine;height: 100vh;">
    <div style="display: flex;width: 50%;background-color: white;border-radius: 5px;overflow: hidden;">
      <div style="flex: 1;">
        <img src="@/assets/imgs/register.png" alt="" style="width: 100%;">
      </div>
      <div style="flex: 1;display: flex;align-items: center;justify-content: center;">
        <el-form status-icon ref="formRef" :model="data.form" style="width: 80%" :rules="data.rules">
          <div style="font-weight: bold; font-size: 20px;margin-bottom: 20px;text-align: center;">
            欢 迎 注 册
          </div>
          <el-form-item prop="username">
            <el-input placeholder="请输入用户名" v-model="data.form.username" :prefix-icon="User"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input placeholder="请输入密码" v-model="data.form.password" show-password></el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input placeholder="请再次输入密码" v-model="data.form.confirmPassword" show-password></el-input>
          </el-form-item>
<!--          <el-form-item prop="vaildCode">-->
<!--            <div style="display: flex;">-->
<!--              <el-input></el-input>-->
<!--              <div style="flex: 1;">1313</div>-->
<!--            </div>-->
<!--          </el-form-item>-->
          <el-form-item>
            <el-button type="success" style="width: 100%;" @click="register">注册</el-button>
          </el-form-item>
          <div style="display: flex;">
            <div style="flex: 1;">已有账号？请<a href="/login" style="color: #248243;cursor: pointer;">登陆</a></div>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import {User} from '@element-plus/icons-vue'
import {reactive, ref} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import request from "@/utils/request.js";

const validatePass = (rule, value, callback) => {
  // value表示用户输入的confirmPassword
  if (value !== data.form.password) {
    callback(new Error('两次输入的密码不匹配'))
  } else {
    callback()
  }
}

const formRef = ref()

const data = reactive({
  form: {
    username: '',
    password: '',
    validCode: ''
  },
  rules: {
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
      {min: 3, message: '账号最少3位', trigger: 'blur'}
    ],
    password: [
      {required: true, message: '请输入密码', trigger: 'blur'},
    ],
    confirmPassword: [
      {required: true, message: '请再次输入密码', trigger: 'blur'},
      {validator: validatePass, trigger: 'blur'},
    ]
  }
})

const register = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/register', data.form).then(res => {
        if (res.code === '200') {
          ElMessage.success("注册成功")
          router.push("/login")
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

</script>

<style scoped>

</style>