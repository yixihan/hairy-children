import Axios from '../utils/axios'

// 用户名登录
export function login(data) {
  return Axios({
    url: '/v/login',
    method: 'post',
    data
  })
}

// 邮箱登录
export function loginByEmail(data) {
  return Axios({
    url: '/v/loginByEmail',
    method: 'post',
    data
  })
}

// 手机号登录
export function loginByPhone(data) {
  return Axios({
    url: '/v/loginByPhone',
    method: 'post',
    data
  })
}

// 注册
export function register(data) {
  return Axios({
    url: '/v/register',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo(data) {
  return Axios({
    url: '/user/getUserInfo',
    method: 'post',
    data
  })
}

// 获取文章列表
export function getArticleList(data) {
  return Axios({
    url: '/title/getAllTitles',
    method: 'post',
    data
  })
}
