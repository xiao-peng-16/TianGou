<template>
    <div class="nav_top_box"  :class="{flag_fixed:flag_fixed}">
      <div class="nav_top" ref="nav_top">

        <div class="container">



            <ul class="left" v-if="flag_enter" >
<!--              <router-link :to="{name:'home'}" tag="li"   id="TianGou" v-if=" 'home'!= this.$route.name ">-->
              <router-link :to="{name:'home'}" tag="li"   id="TianGou">
                  <span class="iconfont" style="color: #FF0036">&#xe867;</span>
                  <span>天狗首页</span>
              </router-link>

              <li class="options_list">
                <div class="options_list_title">
                  <span>Hi, {{this.user.userName}}</span>
                  <div style="display: inline-block;transform: translate(0px,-1px)"><span style="font-size: 5px;" class="iconfont">&#xe60f;</span></div>
                </div>
                <div class="user_box">
                  <img  class="user_Head" :src="show_userPhotoURL" @click="to_user_center_function">
                    <div class="user_right">
                      <span @click="to_user_center_account">账号管理</span>
                      |
                      <span @click="outenter">退出</span>
                    </div>
                    <div><span>{{this.user.userName}}</span></div>
                    <div><span>普通用户</span></div>
                    <div class="user_bottom" @click="to_user_center_function"><span>查看全部功能</span></div>
                </div>
              </li>


              <li id="userMoney"><span>余额{{(this.user.userMoney).toFixed(2)}}</span></li>
<!--              <li><span @click="outenter">退出</span></li>-->
            </ul>
            <ul class="left" v-else>
              <router-link :to="{name:'home'}" tag="li">
                  <span class="iconfont" style="color: #FF0036">&#xe867;</span>
                  <span>天狗首页</span>
              </router-link>

              <li><span>汪，欢迎来到天狗</span></li>
              <router-link :to="{name:'login'}" tag="li"><span>请登录</span></router-link>
              <router-link :to="{name:'register'}" tag="li"><span>免费注册</span></router-link>
            </ul>


          <div >
            <ul class="right">
              <router-link :to="{name:'shop_car'}" tag="li">
                  <span class="iconfont" style="font-size: 12px; color:#FF4400" >&#xe63a;</span>
                  <span>购物车</span>
                  <span style="color: #FF4400;font-weight: 700; color:#FF4400 ">
                  {{shopCarNumber}}
                </span>
              </router-link>

              <router-link :to="{name:'favorite'}" tag="li">
                  <span class="iconfont" style="font-size: 12px;">&#xe636;</span>
                  <span>收藏夹</span>
              </router-link>


              <li class="options_list">
                <div class="options_list_title" @click="to_user_order(0)">
                  <span>我的订单</span>
                  <div style="display: inline-block;transform: translate(0px,-1px)"><span style="font-size: 5px;" class="iconfont">&#xe60f;</span></div>
                </div>
                <div class="options_list_content">
                  <ul>
                    <li @click="to_user_order(0)"><span>所有订单</span></li>
                    <li @click="to_user_order(1)"><span>待付款</span></li>
                    <li @click="to_user_order(2)"><span>待发货</span></li>
                    <li @click="to_user_order(3)"><span>待收货</span></li>
                    <li @click="to_user_order(4)"><span>待评价</span></li>
                  </ul>
                </div>
              </li>

              <li class="options_list">
                <div class="options_list_title" @click="to_store_center(0)">
                  <span>卖家中心</span>
                  <div style="display: inline-block;transform: translate(0px,-1px);"><span style="font-size: 5px;" class="iconfont">&#xe60f;</span></div>
                </div>
                <div class="options_list_content">
                  <ul>
                    <li @click="to_store_center(0)"><span>店铺概况</span></li>
                    <li @click="to_store_center(1)"><span>商品管理</span></li>
                    <li @click="to_store_center(2)"><span>订单管理</span></li>
                  </ul>
                </div>
              </li>



              <transition leave-active-class="animated hinge">
                <li v-show="flag_tou_su" @click="flag_tou_su=false" style="margin-left: 15px" ><span>投诉</span></li>
              </transition>
            </ul>







          </div>

        </div>



      </div>
    </div>
</template>

<script>
    export default {
        name: "nav_top",
        props:['flag_fixed','flag_scroll',   'maxWidth'],
        data(){
          return{
            store_center_left_options:0,

            flag_tou_su:true,
            flag_enter:false,
            user:{
              userName:"",
              userMoney:""
            },
          }
        },
      computed:{
        shopCarNumber(){
          var shopCarNumber = this.$store.state.user.shopCarNumber;
          if (0<shopCarNumber)
            return shopCarNumber;
          else return '';
        },
        innerWidth(){
          var innerWidth = this.$store.state.innerWidth;
          if (undefined != innerWidth && undefined != this.$refs.nav_top)
            this.$refs.nav_top.style.width = innerWidth  +"px";
          return innerWidth;
        },
        show_userPhotoURL() {
          return this.$store.getters.getUserPhotoURL();
        }
      },
      watch:{
        maxWidth(){
          if (undefined != this.maxWidth){
            var container =  document.querySelector('.container');
            container.style.maxWidth = this.maxWidth + "px"
          }
        }
      },
      methods:{
          to_user_center_function(){
            this.$router.push({name:'user_center',query:{t:'0',l:'0',ls:'0'}});

          },
          to_user_center_account(){
            this.$router.push({name:'user_center',query:{t:'1',l:'0'}});
          },
          to_user_order(user_center_left_son_options){
            this.$store.state.user_center_top_options = 0;
            this.$store.state.user_center_left_options = 0;
            this.$store.state.user_center_left_son_options = user_center_left_son_options;
            this.$router.push({name:'user_center'});
          },
          to_store_center(store_center_left_options){
            this.$router.push({name:'store_center',query:{l:store_center_left_options}});
          },
          outenter(){
            this.$axios.post('/user/outLoginByToken')
              .then(res=>{
                  if (res.data.success){
                    // this.user_Leave();
                    this.$store.commit('user_Leave');
                    this.$store.state.user.shopCarNumber =0;
                    this.$router.push({name:'login'});
                  }
              })
          },
        },
        created() {
            this.$axios.post('/user/selUserByUserId')
              .then(res=>{
                if (res.data.success){
                  this.flag_enter = true;
                  this.user=res.data.data;
                  this.$store.state.user=res.data.data;
                }

            });
        },
      mounted() {
          if (undefined != this.flag_scroll && this.flag_scroll) {
              //这样 滚动条的出现和消失 不会影响 总宽度  影响布局   一直减去滚动条宽度
              this.$refs.nav_top.style.width = window.innerWidth -20   +"px";
              window.onresize = () => {
                return (() => {
                  this.$refs.nav_top.style.width = window.innerWidth -20 +"px";
                })()
            }
          }

      }
    }
</script>

<style scoped>


  .flag_fixed{
    position: fixed;
    z-index: 999;
  }

  .nav_top_box{
    z-index: 999;

    width: 100%;
    background:#f5f5f5;
    font: 12px/1.5 tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;
  }

  .nav_top{
    width: 100%;
    height: 35px;
    background:#f5f5f5;

    border-bottom: 1px solid #eee;
  }


  .container{
    padding: 0px;
    height: 100%;
    max-width: 1400px;
    position: relative;

  }
  .nav_top span{
    color: #6c6c6c;
    font: 12px/1.5 tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;
  }

  ul,li{
    margin: 0px;
    padding: 0px;
    list-style: none;
    display: inline-block;
  }
  ul{
    height: 100%;
  }
  li{
    cursor: pointer;
    height: 100%;
    padding-top: 8px;
  }
  li:not(.options_list):hover span{
    color: #f40;
  }

  #userMoney{
    cursor: default;
  }
  #userMoney:hover span{
    color: #6c6c6c;
  }




  .left{
    float: left;
  }
  .left li{
    margin-right: 20px;
    float: left;/*脱离文档流*/
  }
  .right{
    float: right;
    position: absolute;
    right: 0px;
  }
  .right li{
    margin-left: 20px;
    float: left;/*脱离文档流*/
  }
  span{
    font-style: normal;
  }






  .options_list{
    width: 75px;
  }
  .options_list_title{
    padding: 0px 5px;
    width: 75px;
  }
  .options_list:hover{
    background: white;
  }
  .options_list_content{
    border: 1px solid #f5f5f5;
    border-top: none;

    margin-top: 8px;
    display: none;
  }
  .options_list:hover .options_list_content{
    display: block;
  }
  .options_list .options_list_content{
    background: white;
  }
  .options_list li{
    width: 100%;
    padding: 0px 10px;
    margin: 0px;
    line-height: 28px;
  }
  .options_list li:hover{
    background: #f5f5f5;
  }


  .options_list + li{
    margin-left: 0px;
  }
  
  
  
  
  .user_box{
    cursor: default;
    border: 1px solid #f5f5f5;
    border-top: none;
    margin-top: 9px;
    display: none;
    width: 265px;
    height: 116px;
    background: white;
    
    padding: 8px;

  }
  .user_box span{
    color: black;
  }

  .options_list:hover .user_box{
    display: block;
  }
  .user_Head{
    width: 56px;
    height: 56px;
    border-radius: 50px;
    cursor: pointer;
    margin: 0px 20px;
    float: left;
    
    margin-top: 5px;
    }

  .user_right{
    text-align: right;
  }
  .user_right span{
    cursor: pointer;
  }
  .user_right span:hover{
    color: #f40;
  }

  .user_bottom{
    margin-top: 20px;
    text-align: center;
    border: 1px solid #FFE8DE;
    background: #FFF0E8;
    padding: 4px 0;
    cursor: pointer;
  }
  .user_bottom:hover span{
    color: #f40;
  }


</style>
