import axios from 'axios'

const Axios = axios.create({
  baseURL: 'http://175.24.229.41:9421',
  timeout: 5000
})

// 不需要认证的页面
const noAuthUrls = ['/login']

Axios.interceptors.request.use(
  (config) => {
    // Do something before request is sent
    if (!noAuthUrls.includes(config.url)) {
      const token = localStorage.getItem('jwt-token')
      config.headers['jwt-token'] = token
    }
    return config
  },
  (error) => {
    // Do something with request error
    return Promise.reject(error)
  }
)

export default Axios
