//引入vue和Vuex
import Vue from 'vue'
import Vuex from 'vuex'
//引入之后，对vuex进行引用
Vue.use(Vuex)

export const store = new Vuex.Store({
    state:{
      status:0,
      shopCarNumber:0,
      search_word:'',
      userName:undefined,
      userPhotoname:undefined
    },
  getters:{
      getResultDispose:function(state,getters){
        //返回匿名函数
        return function (res) {
          state.status = res.data.status;
          return res.data.success;
        }
      },
  },
  mutations:{
    user_Leave(state){
      state.userName = undefined;
      state.userPhotoname = undefined;
      sessionStorage.clear();
      localStorage.clear();
    }
  }
})