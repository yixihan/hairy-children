import Axios from '../utils/axios'

// 登录
export function login(data) {
  return Axios({
    url: '/v/login',
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

// 获取文章列表
export function getArticleList(data) {
  return Axios({
    url: '/title/getAllTitles',
    method: 'post',
    data
  })
}
