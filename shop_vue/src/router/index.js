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
      path:'/shop_car',
      name:'shop_car',
      component: resolve => require(['../content/shop_car'], resolve)
    },
    {
      path:'/favorite',
      name:'favorite',
      component: resolve => require(['../content/favorite'], resolve)
    },
    {
      path:'/tou_su',
      name:'tou_su',
      component: resolve => require(['../content/tou_su'], resolve)
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








  ]
})
