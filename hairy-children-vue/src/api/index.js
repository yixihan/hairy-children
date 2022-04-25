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

// 上传头像
export function uploadAvatar(data) {
  return Axios({
    url: '/user-info/uploadAvatar',
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
    data
  })
}

// 更新用户信息
export function updateUserInfo(data) {
  return Axios({
    url: '/user-info/updateUserInfo',
    method: 'post',
    data
  })
}

// 实名认证
export function authentication(data) {
  return Axios({
    url: '/user-info/authentication',
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

// 新建线索
export function createClue(data) {
  return Axios({
    url: '/clue/creatClue',
    method: 'post',
    data
  })
}

// 更新线索
export function updateClue(data) {
  return Axios({
    url: '/clue/updateClue',
    method: 'post',
    data
  })
}

// 删除线索
export function deleteClue(data) {
  return Axios({
    url: '/clue/deleteClue',
    method: 'post',
    data
  })
}

// 上传线索图片
export function uploadClueImg(clueId, data) {
  return Axios({
    url: `/clue/updateImg/${clueId}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    },
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

// 获取用户所有文章
export function getUserArticle(data) {
  return Axios({
    url: '/title/getAllUserTitles',
    method: 'post',
    data
  })
}

// 获取用户所有评论
export function getUserComment(data) {
  return Axios({
    url: '/comment/getAllUserComment',
    method: 'post',
    data
  })
}

// 获取用户所有线索
export function getUserClue(data) {
  return Axios({
    url: '/clue/getAllUserClues',
    method: 'post',
    data
  })
}

// 获取所有领养申请通知
export function getAllAdoptionNotices(data) {
  return Axios({
    url: '/mailbox/getAdoptMessages',
    method: 'post',
    data
  })
}

// 获取用户所有的收藏夹
export function getUserFavorites(data) {
  return Axios({
    url: '/collection/getAllFavorites',
    method: 'post',
    data
  })
}

// 添加收藏夹
export function addFavorite(data) {
  return Axios({
    url: '/collection/createFavorites',
    method: 'post',
    data
  })
}

// 删除收藏夹
export function deleteFavorite(data) {
  return Axios({
    url: '/collection/deleteFavorites',
    method: 'post',
    data
  })
}

// 查看收藏夹内的文章
export function getFavoriteArticle(data) {
  return Axios({
    url: '/collection/getAllCollections',
    method: 'post',
    data
  })
}

// 收藏文章
export function addCollection(data) {
  return Axios({
    url: '/collection/addCollection',
    method: 'post',
    data
  })
}

// 收到的领养申请通知
export function getReceiveAdoptionNotices(data) {
  return Axios({
    url: '/mailbox/getAdoptMessages',
    method: 'post',
    data
  })
}

// 获取所有评论通知
export function getAllCommentNotices(data) {
  return Axios({
    url: '/mailbox/getCommentMessages',
    method: 'post',
    data
  })
}

// 获取评论内容
export function getCommentContent(data) {
  return Axios({
    url: '/mailbox/getCommentMessage',
    method: 'post',
    data
  })
}

// 获取所有回复通知
export function getAllReplyNotices(data) {
  return Axios({
    url: '/mailbox/getReplyMessages',
    method: 'post',
    data
  })
}

// 获取所有线索通知
export function getAllClueNotices(data) {
  return Axios({
    url: '/mailbox/getClueMessages',
    method: 'post',
    data
  })
}
