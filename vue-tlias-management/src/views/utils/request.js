import axios from 'axios'
import { ElMessage } from 'element-plus'
import router from '@/router/index.js'
import { getLoginUser } from './auth.js'

//创建axios实例，开发时由Vite将/api转发到后端。
const request = axios.create({ baseURL: '/api', timeout: 15000 })

//请求拦截器：每次请求读取最新的token，不在控制台打印令牌。
request.interceptors.request.use((config) => {
  const loginUser = getLoginUser()
  if (loginUser?.token) config.headers.token = loginUser.token
  return config
})

//响应拦截器：统一返回业务数据；网络错误和401分别处理。
request.interceptors.response.use(
  (response) => response.data,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('loginUser')
      if (router.currentRoute.value.path !== '/login') {
        ElMessage.error('登录已失效，请重新登录')
        router.replace('/login')
      }
    } else {
      ElMessage.error(error.response?.data?.msg || '请求失败，请检查网络或后端服务')
    }
    return Promise.reject(error)
  }
)

export default request
