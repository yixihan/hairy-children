import axios from 'axios'
import { getData } from './tools'

const Axios = axios.create({
  baseURL: 'http://175.24.229.41:9421',
  timeout: 5000
})

// 不需要认证的页面
const noAuthUrls = ['/v/login', '/title/getAllTitles']

Axios.interceptors.request.use(
  (config) => {
    // Do something before request is sent
    console.log(config.url)
    if (!noAuthUrls.includes(config.url)) {
      const token = getData('token')
      config.headers['jwt-token'] = token.token
    }
    return config
  },
  (error) => {
    // Do something with request error
    return Promise.reject(error)
  }
)

export default Axios
