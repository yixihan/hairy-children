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

// 获取文章所有线索
export function getArticleClues(data) {
  return Axios({
    url: '/clue/getAllTitleClues',
    method: 'post',
    data
  })
}

// 获取文章所有领养申请
export function getArticleAdoptions(data) {
  return Axios({
    url: '/adopt/getAllTitleAdopts',
    method: 'post',
    data
  })
}

// 新建领养申请
export function createAdoption(data) {
  return Axios({
    url: '/adopt/creatAdopt',
    method: 'post',
    data
  })
}

// 更新领养申请
export function updateAdoption(data) {
  return Axios({
    url: '/adopt/updateAdopt',
    method: 'post',
    data
  })
}

// 删除领养申请
export function deleteAdoption(data) {
  return Axios({
    url: '/adopt/deleteAdopt',
    method: 'post',
    data
  })
}

// 上传领养申请图片
export function uploadAdoptionImg(adoptId, data) {
  return Axios({
    url: `/adopt/updateImg/${adoptId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    data
  })
}
