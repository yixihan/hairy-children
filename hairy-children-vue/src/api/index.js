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

// 新建文章
export function createArticle(data) {
  return Axios({
    url: '/title/createTitle',
    method: 'post',
    data
  })
}

// 上传图片
export function uploadImg(titleId, data) {
  return Axios({
    url: `/title/updateImg/${titleId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    data
  })
}

// 更新文章
export function updateArticle(data) {
  return Axios({
    url: '/title/updateTitle',
    method: 'post',
    data
  })
}

// 获取文章
export function getArticle(data) {
  return Axios({
    url: '/title/getTitle',
    method: 'post',
    data
  })
}

// 获取文章所有评论
export function getArticleComments(data) {
  return Axios({
    url: '/comment/getAllTitleComment',
    method: 'post',
    data
  })
}

// 添加父评论
export function addRootComment(data) {
  return Axios({
    url: '/comment/addRootComment',
    method: 'post',
    data
  })
}

// 添加子评论
export function addSonComment(titleId, data) {
  return Axios({
    url: `/comment/addSonComment/${titleId}`,
    method: 'post',
    data
  })
}
