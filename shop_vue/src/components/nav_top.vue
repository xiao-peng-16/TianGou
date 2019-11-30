<template>
    <div style="width: 100%;overflow: hidden">
      <div class="nav_top" ref="nav_top" :class="{flag_fixed:flag_fixed}">

        <div class="container">

          <div class="left box">


            <ul v-if="flag_enter" >
              <router-link :to="{name:'home'}" tag="li"   id="TianGou">
                <span class="iconfont" style="color: #FF0036">&#xe867;</span>
                <span>天狗首页</span>
              </router-link>

              <li><span>Hi, {{this.user.userName}}</span></li>
              <li><span>余额{{(this.user.userMoney).toFixed(2)}}</span></li>
              <li><span @click="outenter">退出</span></li>
            </ul>
            <ul v-else>
              <router-link :to="{name:'home'}" tag="li">
                <span class="iconfont" style="color: #FF0036">&#xe867;</span>
                <span>天狗首页</span>
              </router-link>

              <li><span>汪，欢迎来到天狗</span></li>
              <router-link :to="{name:'login'}" tag="li"><span>请登录</span></router-link>
              <router-link :to="{name:'register'}" tag="li"><span>免费注册</span></router-link>
            </ul>

          </div>

          <div >
            <ul class="right box">
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
              <router-link :to="{name:'store_center'}" tag="li"><span>卖家中心</span></router-link>

              <!--              <router-link :to="{name:'service'}" tag="li"><span>联系客服</span></router-link>-->
              <!--              <li><span>联系客服</span></li>-->

              <transition leave-active-class="animated hinge">
                <li v-show="flag_tou_su" @click="flag_tou_su=false" ><span>投诉</span></li>
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
          var shopCarNumber = this.$store.state.shopCarNumber;
          if (0<shopCarNumber)
            return shopCarNumber;
          else return '';
        },
        innerWidth(){
          var innerWidth = this.$store.state.innerWidth;
          if (undefined != innerWidth && undefined != this.$refs.nav_top)
            this.$refs.nav_top.style.width = innerWidth  +"px";
          return innerWidth;
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
          outenter(){
            this.$axios.post('/user/outLoginByToken')
              .then(res=>{
                  if (res.data.success){
                    // this.user_Leave();
                    this.$store.commit('user_Leave');
                    this.$store.state.shopCarNumber =0;
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
                  this.$store.state.userName = res.data.data.userName;
                  this.$store.state.userPhotoname = res.data.data.userPhotoname;
                  this.$axios.post('/car/selShopCarNumberByUserId')
                    .then(res=>{
                      this.$store.state.shopCarNumber = res.data.data;
                    });
                }

            });
        },
      mounted() {
          if (undefined != this.flag_scroll && this.flag_scroll) {
              //这样 滚动条的出现和消失 不会影响 总宽度  影响布局
              this.$refs.nav_top.style.width = window.innerWidth +"px";
              window.onresize = () => {
                return (() => {
                  this.$refs.nav_top.style.width = window.innerWidth +"px";
                })()
            }
          }

      }
    }
</script>

<style scoped>

  .flag_fixed{
    position: fixed;
  }

  .nav_top{
    height: 35px;
    width: 100%;
    z-index: 999;
    background-color: #f5f5f5;
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
  ul{
    position: absolute;
    bottom: 8px;
  }

  .left{
    float: left;
  }
  .left li{
    margin-right: 20px;
  }
  .right{
    float: right;
    position: absolute;
    right: 0px;
  }
  .right li{
    margin-left: 20px;
  }

ul,li{
  margin: 0px;
  padding: 0px;
  list-style: none;
  display: inline-block;

}
  span{
    font-style: normal;
    cursor: pointer;
  }
</style>
