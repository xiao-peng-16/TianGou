import Vue from 'vue'
import Router from 'vue-router'
import home from '@/content/home'
import login from '@/content/login'
import register from '@/content/register'
import shop_car from '@/content/shop_car'
import favorite from '@/content/favorite'
import tou_su from '@/content/tou_su'
import drop_password from '@/content/drop_password'
import searchPage from '@/content/searchPage'
import  commodityPage from '@/content/commodityPage'
import  shop_success from '@/content/shop_success'
import  store_center from '@/content/store_center'
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
      component:home
    },
    {
      path:'/login',
      name:'login',
      component:login
    },
    {
      path:'/register',
      name:'register',
      component:register
    },
    {
      path:'/shop_car',
      name:'shop_car',
      component:shop_car
    },
    {
      path:'/favorite',
      name:'favorite',
      component:favorite
    },
    {
      path:'/tou_su',
      name:'tou_su',
      component:tou_su
    },
    {
      path:'/drop_password',
      name:'drop_password',
      component:drop_password
    },
    {
      path:'/searchPage',
      name:'searchPage',
      component:searchPage
    },
    {
      path:'/commodityPage',
      name:'commodityPage',
      component:commodityPage
    },
    {
      path:'/shop_success',
      name:'shop_success',
      component:shop_success
    },
    {
      path:'/store_center',
      name:'store_center',
      component:store_center
    }








  ]
})
