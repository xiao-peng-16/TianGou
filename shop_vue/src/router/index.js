import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect:'home'
    },
    {
      path:'/home',
      name:'home',
      component: resolve => require(['../content/home'], resolve)
    },
    {
      path:'/login',
      name:'login',
      component: resolve => require(['../content/login'], resolve)
    },
    {
      path:'/register',
      name:'register',
      component: resolve => require(['../content/register'], resolve)
    },
    {
      path:'/cart',
      name:'cart',
      component: resolve => require(['../content/cart'], resolve)
    },
    {
      path:'/favorite',
      name:'favorite',
      component: resolve => require(['../content/favorite'], resolve)
    },

    {
      path:'/drop_password',
      name:'drop_password',
      component: resolve => require(['../content/drop_password'], resolve)
    },
    {
      path:'/searchPage',
      name:'searchPage',
      component: resolve => require(['../content/searchPage'], resolve)
    },
    {
      path:'/commodityPage',
      name:'commodityPage',
      component: resolve => require(['../content/commodityPage'], resolve)
    },
    {
      path:'/shop_success',
      name:'shop_success',
      component: resolve => require(['../content/shop_success'], resolve)
    },
    {
      path:'/store_center',
      name:'store_center',
      component: resolve => require(['../content/store_center'], resolve)
    },
    {
      path:'/user_center',
      name:'user_center',
      component: resolve => require(['../content/user_center'], resolve)
    },
    {
      path:'/about',
      name:'about',
      component: resolve => require(['../content/about'], resolve)
    },








  ]
})
