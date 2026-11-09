<script setup>
import {ref, onMounted} from 'vue'

//当前登录的员工
const loginName = ref('')

//钩子函数
onMounted(() => {
  //获取当前登录员工信息
  const loginUser = JSON.parse(localStorage.getItem('loginUser'))
  if(loginUser) {
    loginName.value = loginUser.name
  }
})

//退出登录
const logout = () => {
  //清除本地存储的登录员工信息
  localStorage.removeItem('loginUser')
  //跳转到登录页面
  window.location.href = '/login'
}
</script>

<template>
  <div class="common-layout">
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
        <span class="title">Tlias智能学习辅助系统</span>
        <span class="right_tool">
          <a href="">
            <el-icon><EditPen /></el-icon> 修改密码 &nbsp;&nbsp;&nbsp; |  &nbsp;&nbsp;&nbsp;
          </a>
          <a href="javascript:void(0)" @click="logout">
            <el-icon><SwitchButton /></el-icon> 退出登录 【{{ loginName }}】
          </a>
        </span>
      </el-header>
      
      <el-container>
        <!-- 左侧菜单 -->
        <el-aside width="200px" class="aside">
          <!-- 根据当前路由高亮菜单，刷新或直接输入地址后也保持一致。 -->
          <el-menu router :default-active="$route.path">
            <el-menu-item index="/index">
              <el-icon><Promotion /></el-icon>首页
            </el-menu-item>
          <el-sub-menu index="/message">
            <template #title>
              <el-icon><HomeFilled /></el-icon>班级学员管理
            </template>
              <el-menu-item index="/clazz"><el-icon><HelpFilled /></el-icon>班级管理</el-menu-item>
              <el-menu-item index="/stu"><el-icon><UserFilled /></el-icon>学员管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="/system">
            <template #title>
              <el-icon><Tools /></el-icon>系统信息管理
            </template>
              <el-menu-item index="/dept"><el-icon><HelpFilled /></el-icon>部门管理</el-menu-item>
              <el-menu-item index="/emp"><el-icon><Avatar /></el-icon>员工管理</el-menu-item>
          </el-sub-menu>
          <el-sub-menu index="/report">
            <template #title>
              <el-icon><Histogram /></el-icon>数据统计管理
            </template>
              <el-menu-item index="/empResult"><el-icon><Avatar /></el-icon>员工信息管理</el-menu-item>
              <el-menu-item index="/stuReport"><el-icon><UserFilled /></el-icon>学员信息管理</el-menu-item>
              <el-menu-item index="/log"><el-icon><UserFilled /></el-icon>日志信息统计</el-menu-item>
          </el-sub-menu>
        </el-menu>
        </el-aside>
        
        <el-main>
          <router-view></router-view>
        </el-main>
      </el-container>
      
    </el-container>
  </div>
</template>

<style scoped>
.header {
  background-image: linear-gradient(to right, #00547d, #007fa4, #00aaa0, #00d072, #a8eb12);
}

.title {
  color: white;
  font-size: 40px;
  font-family: 楷体;
  line-height: 60px;
  font-weight: bolder;
}

.right_tool{
  float: right;
  line-height: 60px;
}

a {
  color: white;
  text-decoration: none;
}

.aside {
  width: 220px;
  border-right: 1px solid #ccc;
  height: 730px;
}
</style>
