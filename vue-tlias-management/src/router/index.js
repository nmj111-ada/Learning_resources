import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/layout/index.vue'
import { getLoginUser } from '@/views/utils/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      // 登录页独立显示，不使用后台的顶部导航和侧边菜单。
      path: '/login',
      name: 'login',
      component: () => import('@/views/login/index.vue')
    },
    {
      // 后台页面共用 Layout，子页面显示在 Layout 的 router-view 中。
      path: '/',
      component: Layout,
      redirect: '/index',
      children: [
        {
          // 首页。
          path: 'index',
          name: 'index',
          component: () => import('@/views/index/index.vue')
        },
        {
          // 班级管理，与左侧菜单 /clazz 对应。
          path: 'clazz',
          name: 'clazz',
          component: () => import('@/views/clazz/index.vue')
        },
        {
          // 学员管理。
          path: 'stu',
          name: 'stu',
          component: () => import('@/views/stu/index.vue')
        },
        {
          // 部门管理。
          path: 'dept',
          name: 'dept',
          component: () => import('@/views/dept/index.vue')
        },
        {
          // 员工管理。
          path: 'emp',
          name: 'emp',
          component: () => import('@/views/emp/index.vue')
        },
        {
          // 员工信息统计，保留当前菜单使用的 /empResult 路径。
          path: 'empResult',
          name: 'empReport',
          component: () => import('@/views/report/emp/index.vue')
        },
        {
          // 学员信息统计。
          path: 'stuReport',
          name: 'stuReport',
          component: () => import('@/views/report/stu/index.vue')
        },
        {
          // 操作日志列表。
          path: 'log',
          name: 'log',
          component: () => import('@/views/log/index.vue')
        }
      ]
    },
    {
      // 输入不存在的地址时回到首页，避免只显示空白内容。
      path: '/:pathMatch(.*)*',
      redirect: '/index'
    }
  ]
})

// 未登录时先进入登录页，登录完成后回到原来的页面。
router.beforeEach(to => {
  if(to.path !== '/login' && !getLoginUser()?.token) return {path:'/login',query:{redirect:to.fullPath}}
})

export default router
