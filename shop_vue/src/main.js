// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
// import Vue from 'vue'
import {store} from './store/store'
import App from './App'
import router from './router'
import axios from 'axios';
import globalVariable from '@/components/global_variable'
import globalFunc from '@/components/global_func'
// import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

// require styles

Vue.use(globalFunc/* { default global options } */)
// Vue.use(ElementUI);
Vue.prototype.$axios = axios;
axios.defaults.baseURL = "http://127.0.0.1:7000";
// axios.defaults.baseURL = "http://47.101.147.15:7000";
axios.defaults.headers.post['Content-Type'] = 'application/json';
axios.defaults.withCredentials = true;
//   重要   vue 的拦截器请求头设置token  和  zuul过滤器获取token 并验证得到userId
axios.interceptors.request.use(
  config => {
    config.headers.token = localStorage.getItem("token");
    return config;
  }
);



Vue.prototype.GLOBAL=globalVariable;

Vue.config.productionTip = false



require('promise.prototype.finally').shim();

require('video.js/dist/video-js.css')
require('vue-video-player/src/custom-theme.css')


Vue.directive('myFocus',{
  inserted:function (el) {
    el.focus();
  }
})

/* eslint-disable no-new */
new Vue({
  store:store,//使用store
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
