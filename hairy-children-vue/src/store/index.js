import Vue from "vue";
import Vuex from 'vuex';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: localStorage.getItem("token"), //方法一 localStorage.getItem("token") 
    //反序列化获取session会话中的 userInfo对象
    userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
    userId: localStorage.getItem("userId"),
    url: 'http://175.24.229.41:9421/'
  },
  mutations: {
    //相当于实体类的set
    SET_TOKEN: (state, token) => {
      state.token = token//将传入的token赋值 给state的token
      //同时可以存入浏览器的localStorage里面
      localStorage.setItem("token", token)
      // sessionStorage.setItem("token", JSON.stringify(token))
    },
    SET_USERID: (state, userId) => {
      state.userId = userId//将传入的token赋值 给state的token
      //同时可以存入浏览器的localStorage里面
      localStorage.setItem("userId", userId)
      // sessionStorage.setItem("token", JSON.stringify(token))
    },
    SET_USERINFO: (state, userInfo) => {
      state.userInfo = userInfo//将传入的tuserInfo赋值 给state的userInfo
      //同时可以存入会话的sessionStorage里面 sessionStorage中只能存字符串 不能存入对象所以我们存入序列化 jons串
      // localStorage.setItem("userInfo", JSON.stringify(userInfo))
      sessionStorage.setItem("userInfo", JSON.stringify(userInfo))
    },
    //删除token及userInfo
    REMOVE_INFO: (state) => {
      console.log("正在注销登录");
      state.token = '';
      state.userInfo = {};
      localStorage.setItem("token", '')
      localStorage.setItem("userId", '')
      // localStorage.setItem("userInfo", JSON.stringify(''))
      // localStorage.setItem("userInfo", JSON.stringify(''))
      sessionStorage.setItem("token", JSON.stringify(''))
      // sessionStorage.setItem("userInfo", JSON.stringify(''))
    }
  },
  getters: {
    //相当于get
    //配置一个getUser可以直接获取已经反序列化对象的一个userInfo
    getUser: state => {
      return state.userInfo;
    },
    getUserId: state => {
      return state.userId
    },
    getToken: state => {
      return state.token;
    },
    getUrl: state => {
      return state.url;
    }
  },
  actions: {

  },
  modules: {

  }
})