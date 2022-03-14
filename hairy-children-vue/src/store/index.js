import Vue from "vue";
import Vuex from 'vuex';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    token: JSON.parse(sessionStorage.getItem("token")), //方法一 localStorage.getItem("token") 
    //反序列化获取session会话中的 data对象
    data: JSON.parse(sessionStorage.getItem("data"))
  },
  mutations: {
    //相当于实体类的set
    SET_TOKEN: (state, token) => {
      state.token = token//将传入的token赋值 给state的token
      //同时可以存入浏览器的localStorage里面
      localStorage.setItem("token", token)
    },
    SET_data: (state, data) => {
      state.data = data//将传入的tdata赋值 给state的data
      //同时可以存入会话的sessionStorage里面 sessionStorage中只能存字符串 不能存入对象所以我们存入序列化 jons串
      sessionStorage.setItem("data", JSON.stringify(data))
    },
    //删除token及data
    REMOVE_INFO: (state) => {
      state.token = '';
      state.data = {};
      localStorage.setItem("token", '')
      sessionStorage.setItem("data", JSON.stringify(''))
    }
  },
  getters: {
    //相当于get
    //配置一个getUser可以直接获取已经反序列化对象的一个data
    getUser: state => {
      return state.data;
    }, getToken: state => {
      return state.token;
    }
  },
  actions: {

  },
  modules: {

  }
})