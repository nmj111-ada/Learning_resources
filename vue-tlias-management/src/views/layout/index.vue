<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getLoginUser } from '@/views/utils/auth'
import { updatePasswordApi } from '@/views/api/emp'
const router = useRouter()
const loginName = ref(getLoginUser()?.name || '')
const passwordVisible = ref(false)
const passwordFormRef = ref()
const saving = ref(false)
const passwordForm = ref({oldPassword:'',newPassword:'',confirmPassword:''})
// 改密表单校验，确认密码只在前端使用。
const passwordRules = {
  oldPassword:[{required:true,message:'请输入原密码',trigger:'blur'}],
  newPassword:[{required:true,message:'请输入新密码',trigger:'blur'},{min:6,max:20,message:'新密码长度为6-20位',trigger:'blur'}],
  confirmPassword:[{required:true,message:'请再次输入新密码',trigger:'blur'},{validator:(_rule,value,callback)=>callback(value===passwordForm.value.newPassword?undefined:new Error('两次新密码不一致')),trigger:'blur'}]
}
// 退出时清除本地令牌，并用 replace 避免后退到业务页面。
const logout = () => { localStorage.removeItem('loginUser'); router.replace('/login') }
const openPassword = () => { passwordForm.value={oldPassword:'',newPassword:'',confirmPassword:''}; passwordVisible.value=true }
// 原密码验证成功后更新当前账号，要求重新登录。
const savePassword = async () => {
  if(saving.value || !await passwordFormRef.value.validate().catch(()=>false)) return
  saving.value=true
  try {
    const {oldPassword,newPassword}=passwordForm.value
    const result=await updatePasswordApi({oldPassword,newPassword})
    if(result.code===1) { ElMessage.success('密码修改成功，请重新登录'); passwordVisible.value=false; logout() } else ElMessage.error(result.msg)
  } catch { /* 请求错误已统一提示。 */ } finally { saving.value=false }
}
</script>

<template>
  <div class="common-layout">
    <!-- 当前账号修改密码，不允许指定其他员工 ID。 -->
    <el-dialog v-model="passwordVisible" title="修改密码" width="460px" destroy-on-close>
      <el-form ref="passwordFormRef" :model="passwordForm" :rules="passwordRules" label-width="90px">
        <el-form-item label="原密码" prop="oldPassword"><el-input v-model="passwordForm.oldPassword" type="password" show-password /></el-form-item>
        <el-form-item label="新密码" prop="newPassword"><el-input v-model="passwordForm.newPassword" type="password" show-password /></el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword"><el-input v-model="passwordForm.confirmPassword" type="password" show-password @keyup.enter="savePassword" /></el-form-item>
      </el-form>
      <template #footer><el-button @click="passwordVisible=false">取消</el-button><el-button type="primary" :loading="saving" @click="savePassword">保存</el-button></template>
    </el-dialog>
    <el-container>
      <!-- Header 区域 -->
      <el-header class="header">
        <span class="title">Tlias智能学习辅助系统</span>
        <span class="right_tool">
          <a href="#" @click.prevent="openPassword">
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
