<template>
    <div v-show="hint_popup_flag">
      <!-- 遮盖层 -->
      <div  class="background" @click="aa()"></div>
      <div  class="pupop">
        <div class="pupop_title"><span>{{hint_popup_msg}}</span></div>
        <div class="pupop_button" @click="close"><span>好哒 我知道了</span></div>
      </div>
    </div>
</template>
<script>
    export default {
        name: "hint_popup",
      data(){
          return{
            pre_status:0
          }
      },
      computed:{
        current_status(){
          return this.$store.state.status;
        },
        hint_popup_flag(){
          if (this.pre_status == 0)
            return false;
          if (this.pre_status == this.GLOBAL.ResultStatus.USER_PASSWORD_CHANGE_ERROR)
            return false;
          return true;
        },
        hint_popup_msg(){
          switch (this.pre_status) {
            case 0 : return '';
            //user 2xx
            case this.GLOBAL.ResultStatus.USER_ID_LOGIN_OVERDUE:
              return "亲，请先登录";
            case this.GLOBAL.ResultStatus.USER_MONEY_INSUFFICIENT:
              return "亲，您用户的余额不足";
            case this.GLOBAL.ResultStatus.USER_CHANGE_PASSWORD_ERROR:
              return "亲，您的原密码不对";
            //店铺 3xx
            case this.GLOBAL.ResultStatus.STORE_NOT_REGISTER:
              return "亲，您还没开通店铺功能";
            case this.GLOBAL.ResultStatus.STORE_EQUAL_USER_ERROR:
              return "亲，您不能购买自己店铺的商品哦";
            //商品 4xx
            case this.GLOBAL.ResultStatus.COMMODITY_STOCK_INSUFFICIENT:
              return "亲，您选购的商品的库存不足";
            case this.GLOBAL.ResultStatus.COMMODITY_NOT_FOUND:
              return "商品已下架 搜索不到商品";
            //this.$store.state.status 也可以是字符串
            default:
              return  this.pre_status;
          }
        }

      },
      watch:{
        current_status(val){
          if (0 == this.pre_status)
            this.pre_status = val;
        },
        pre_status(val){
          if (0 == val)
            this.$store.state.status = 0;
        }
      },
      methods:{
        close(){
          var status = this.pre_status;
          this.pre_status = 0;
          if (status == this.GLOBAL.ResultStatus.USER_ID_LOGIN_OVERDUE)
            this.$router.push({name:'login'});
          else if (status == this.GLOBAL.ResultStatus.STORE_NOT_REGISTER){
            this.$store.state.flag_userOpenStore = true;
          }else if (status == this.GLOBAL.ResultStatus.COMMODITY_NOT_FOUND){
            this.$router.push({name:'searchPage'});
          }
        }
      },
    }
</script>

<style scoped>

  span{
    font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";
  }
  .background {
    width: 100%;
    height: 100%;
    position: fixed;
    top: 0px;
    left: 0px;
    z-index: 99999;
    background: rgba(0, 0, 0, .3);
  }

  .pupop {
    width: 512px;
    height: 210px;
    position: fixed;
    border: #ebebeb solid 1px;
    left: 50%;
    top: 45%;
    background: #ffffff;
    border-radius: 5px;
    box-shadow: 0px 0px 20px #ddd;
    z-index: 99999999;
    transform: translate(-50%, -50%);
  }

  .pupop_title {
    width: 100%;
    margin: 20px 0px 0px 0px;
    text-align: center;
    height: 40px;
  }

  .pupop_title span {
    font-size: 20px;
    line-height: 40px;
    background: #ffffff;
    width: 40px;
    height: 40px;
  }

  .pupop_button {
    width: 50%;
    margin: 30px auto 0px auto;
    line-height: 40px;
    font-size: 16px;
    border: #ebebeb 1.1px solid;
    box-shadow: 0px 0px 4px #ebebeb;
    text-align: center;
    border-radius: 2px;
    cursor: pointer;
  }






</style>
