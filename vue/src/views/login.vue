<template>
  <div style="display: flex;align-items: center;justify-content: center;background-color: aquamarine;height: 100vh;">
    <div style="display: flex;width: 50%;background-color: white;border-radius: 5px;overflow: hidden;">
      <div style="flex: 1;">
        <img src="@/assets/imgs/login.png" alt="" style="width: 100%;">
      </div>
      <div style="flex: 1;display: flex;align-items: center;justify-content: center;">
        <el-form ref="formRef" :model="data.form" style="width: 80%" :rules="data.rules">
          <div style="font-weight: bold; font-size: 20px;margin-bottom: 20px;text-align: center;">
            欢 迎 登 陆
          </div>
          <el-form-item prop="username">
            <el-input placeholder="请输入用户名" v-model="data.form.username" :prefix-icon="User"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input placeholder="请输入密码" v-model="data.form.password" show-password></el-input>
          </el-form-item>

          <el-form-item prop="role">
            <el-select style="width: 100%" v-model="data.form.role">
              <el-option label="管理员" value="ADMIN"></el-option>
              <el-option label="用户" value="USER"></el-option>
            </el-select>
          </el-form-item>
<!--          <el-form-item prop="vaildCode">-->
<!--            <div style="display: flex;">-->
<!--              <el-input></el-input>-->
<!--              <div style="flex: 1;">1313</div>-->
<!--            </div>-->
<!--          </el-form-item>-->
          <el-form-item>
            <el-button type="primary" style="width: 100%;" @click="login">登录</el-button>
          </el-form-item>
          <div style="display: flex;">
            <div style="flex: 1;">还没有账号？去<a style="color: aquamarine" href="/register">注册</a> </div>
            <div style="flex: 1;text-align: right;cursor: pointer;color: aquamarine;">忘记密码</div>
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

const formRef = ref()

const data = reactive({
  form: {
    role: 'ADMIN'
  },
  rules: {
    username: [
      {required: true, message: '请输入账号', trigger: 'blur'},
      {min: 3, message: '账号最少3位', trigger: 'blur'}
    ],
    password: [
      {required: true, message: '请输入密码', trigger: 'blur'},
    ]
  }
})

const login = () => {
  formRef.value.validate((valid) => {
    if (valid) {
      request.post('/login', data.form).then(res => {
        if (res.code === '200') {
          // 存储用户信息
          localStorage.setItem("code_user", JSON.stringify(res.data || {}))
          ElMessage.success("登陆成功")
          router.push("/")
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