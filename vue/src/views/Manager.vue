<template>
  <div>
    <!--头部区域开始-->
<!--    <div style="height: 60px; border-bottom: 1px solid #eee; background-color: #2c82ff; display: flex; align-items: center">-->
    <div style="height: 60px; display: flex;">
      <div style="width: 240px; display: flex; align-items: center; padding-left: 10px; background-color: #3a456b">
        <img style="width: 40px; height: 40px; border-radius: 50%" src="@/assets/imgs/logo.png" alt="">
        <span style="font-size: 24px; font-weight: bold; color: #f1f1f1; margin-left: 5px">后台管理系统</span>
      </div>
      <div style="flex: 1; border-bottom: 1px solid #ddd; display: flex; align-items: center; padding-left: 20px">
        首页/数据分析
      </div>
      <div style="width: fit-content; display: flex; align-items: center; padding-right: 20px; border-bottom: 1px solid #ddd">
        <el-dropdown>
          <div style="display: flex; align-items: center;">
            <img style="width: 40px; height: 40px; border-radius: 50%" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
            <span style="margin-left: 5px">{{ userInfo.name || userInfo.username}}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item>个人信息</el-dropdown-item>
              <el-dropdown-item>修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登陆</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <!--头部区域结束-->

    <!--下方区域开始-->
    <div style="display: flex">
      <!--菜单区域开始-->
      <div style="width: 240px">
        <el-menu router :default-openeds="['1']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px)">
          <el-menu-item index="/manager/home">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><location /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/manager/admin">管理员信息</el-menu-item>
            <el-menu-item index="/manager/user">用户信息</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </div>
      <!--菜单区域结束-->

      <!--数据渲染区域开始-->
      <div style="flex: 1; width: 0; padding: 10px; background-color: #f2f4ff">
        <RouterView />
      </div>
      <!--数据渲染区域结束-->

    </div>
    <!--下方区域结束-->

  </div>
</template>

<script setup>

import router from "@/router/index.js";
import {ref} from 'vue'

const userInfo = ref({})
let userStr = localStorage.getItem('code_user');
if (userStr) {
  let user = JSON.parse(userStr);
  if (!user.id) {
    location.href = "/login"
  } else {
    userInfo.value = user;
  }
} else {
  location.href = '/login'
}

const logout = () => {
  localStorage.removeItem('code_user')
  location.href = '/login'
}
</script>

<style>
.el-menu {
  background-color: #3a456b;
  border: none;
}
.el-sub-menu__title {
  color: #ddd;
}
.el-menu-item {
  height: 50px;
  color: #ddd;
}
.el-menu .is-active {
  background-color: #537bee;
  color: #fff;
}
.el-sub-menu__title:hover {
  background-color: #3a456b;
}
.el-menu-item:not(.is-active):hover {
  background-color: #537bee;
  color: #333;
}
.el-dropdown {
  cursor: pointer;
}
.el-tooltip__trigger {
  outline: none;
}
.el-menu--inline .el-menu-item {
  padding-left: 48px !important;
}
</style>