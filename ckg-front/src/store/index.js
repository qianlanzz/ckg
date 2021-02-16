import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    token: ''
  },
  mutations: {
    SET_TOKEN: (state,token) => {
      state.token = token;
      localStorage.setItem("token",token)
    },
    REMOVE_INFO: (state=>{
      state.token = '';
      localStorage.setItem("token",'')
    })
  },
  actions: {},
  modules: {}
});
