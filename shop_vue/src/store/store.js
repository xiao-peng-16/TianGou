import defaultHead from '@/assets/defaultHead.jpg';

//引入vue和Vuex
import Vue from 'vue'
import Vuex from 'vuex'
//引入之后，对vuex进行引用
Vue.use(Vuex)

export const store = new Vuex.Store({
    state:{
      status:0,
      search_word:'',
      user:{
        userId: undefined,
        userName: undefined,
        userPhoto: undefined,
        userMoney: undefined,
        shopCarNumber: undefined
      },
      flag_userOpenStore:false,
      flag_userOpenStore_success:false

    },
  getters:{
      getResultDispose:function(state,getters){
        //返回匿名函数
        return function (res) {
          // 当 res.data.success 不为空 并且为false 时 代表错误
          if (undefined == res.data.success || res.data.success)
            return true;
          state.status = res.data.status;
          return false;
        }
      },
      getUserPhotoURL:function(state,getters){
        //返回匿名函数
        return function () {
          var user = state.user;
          if (undefined == user || undefined == user.userPhoto || '' == user.userPhoto)
            return defaultHead;
          else
            return user.userPhoto;
        }
      },

  },
  mutations:{
    user_Leave(state){
      state.user={
          userId: undefined,
          userName: undefined,
          userPhoto: undefined,
          userMoney: undefined,
          shopCarNumber: undefined
      },
      sessionStorage.clear();
      localStorage.clear();
    }
  }
})
