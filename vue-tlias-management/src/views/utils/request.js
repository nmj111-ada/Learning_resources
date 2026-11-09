import axios from 'axios'
import router from '@/router/index.js'

//创建axios实例对象
const request = axios.create({
  baseURL: '/api',
  timeout: 6000
})

//axios的请求 request 拦截器-获取localStorge中的token,并将token放入请求头中
request.interceptors.request.use(
  (config) => {
    const loginUser = JSON.parse(localStorage.getItem('loginUser'))
    if(loginUser && loginUser.token) {
      config.headers.token = loginUser.token
    } 
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)
//axios的响应 response 拦截器
request.interceptors.response.use(
  (response) => { //成功回调
    return response.data
  },
  (error) => { //失败回调
    if(error.response.satatus === 401) {
      //token过期，清除本地存储的用户信息，跳转到登录页面
      localStorage.removeItem('loginUser')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export default request