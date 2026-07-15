import axios from 'axios'

const request = axios.create()

// 自动带 Token
request.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = token
  }
  return config
})

// 统一解析 Result<T> 的 data
request.interceptors.response.use(res => {
  const body = res.data
  // body: { code, msg, data }
  if (body.code === 200) {
    return body.data  // 直接返回 data，调用处不用再 .data
  }
  // 业务错误（400/401/403/404/500）
  if (body.code === 401) {
    localStorage.removeItem('token')
    localStorage.removeItem('username')
    window.location.href = '/'
    return Promise.reject(new Error(body.msg))
  }
  return Promise.reject(new Error(body.msg || '请求失败'))
})

export default request
