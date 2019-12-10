<template>
  <div>
    <nav_top :flag_fixed="true" :flag_scroll="true" />
    <div style="position: fixed;width: 100%;height: 110px;padding-top: 50px; background: white;z-index: 100;border-bottom: 1px solid #DADADA">
      <img  src="../assets/tmall.jpg" style="float: left">
      <div class="top_box">
        <span id="title">— 用户中心 —</span>
        <div class="top_options">
          <span @click="$store.state.user_center_top_options=0">全部功能</span>
          <span @click="$store.state.user_center_top_options=1">账号管理</span>
        </div>
      </div>
    </div>


    <div class="buttom">
      <div class="left" >
        <div v-for="(item, index) in options_list_list[top_options]" @click="click_left_options(index)"  :class="{optionsBox:left_options==index,not_optionsBox:left_options!=index}">
          <span>{{item.title}}</span>
        </div>
      </div>


      <div class="right" :class="{flag_background:flag_background}">
        <component :is="optionsComponents"/>
      </div>
    </div>



  </div>
</template>

<script>
  import Nav_top from "@/components/nav_top";
  import Hint_popup from "@/components/hint_popup";
  import changeUserPhoto from "@/components/user_center/changeUserPhoto";
  import changeUserPassword from "@/components/user_center/changeUserPassword";
  import UserOrderWaitPayment from "@/components/user_center/userOrderWaitPayment";

  export default {
    name: "user_center",
    components: {UserOrderWaitPayment, Hint_popup,  Nav_top, changeUserPhoto, changeUserPassword},
    data(){
      return{
        options_list_list:[
          [
            {
              title:'已付款',
              components:'userOrderWaitPayment'
            },
            {
              title:'待付款',
            },
            {
              title:'我的购物车',
              route:{name:'shop_car'}
            },
            {
              title:'我的收藏夹',
              route:{name:'favorite'}
            },
          ],
          [
            {
              title:'更换头像',
              components:'changeUserPhoto'
            },
            {
              title:'修改密码',
              components:'changeUserPassword'
            },
          ],


        ]
      }
    },
    computed:{
      top_options(){
        return this.$store.state.user_center_top_options;
      },
      left_options(){
        return this.$store.state.user_center_left_options;
      },
      optionsComponents(){
        var item = this.options_list_list[this.top_options][this.left_options];
        if (undefined == item || undefined == item.components)
          return;
        return  item.components;
      },
      flag_background(){
        return this.top_options == 0;

      }
    },
    watch:{
      top_options(){
        this.$store.state.user_center_left_options = 0;
      }
    },
    methods:{
      click_left_options(val){
        if (undefined != this.options_list_list[this.top_options][val].route){
          this.$router.push(this.options_list_list[this.top_options][val].route)
        }else
          this.$store.state.user_center_left_options = val;
      }
    }
  }
</script>

<style scoped>

  .top_box{
    position: relative;
    width: 100%;
    height: 100%;
    margin-left: 260px;
  }
  #title{
    position: absolute;
    top: 45%;
    left: 33%;
    transform: translate(-38%,-50%);
    font-size: 16px;
    font-weight: 700;
  }
  .top_options{
    position: absolute;
    top: 45%;
    left: 20px;
    transform: translate(0px,-50%);
    font-size: 16px;
    font-weight: 700;
  }
  .top_options span{
    margin-right: 20px;
    cursor: pointer;
  }
  .top_options span:hover{
    color: #f40;
  }


  .optionsBox{
    background: #292929;
    border-left: 2px solid #F28328;

  }
  .not_optionsBox{
    border-left: 2px solid #333333;

  }


  .flag_background{
    background: #F8F8F8;
  }

  .right{
    width: 100%;
    position: absolute;
    padding-left: 280px;
    padding-top: 110px;
    height: 100%;
  }

  .left{
    padding-top: 112px;

    width: 260px;
    height: 100%;
    background: #333333;
    position: fixed;
    z-index: 10;
  }
  .left div{
    padding: 10px 70px;
    color: #FAFAFA;
    letter-spacing:2px;
    font-weight: 400;
    cursor: pointer;

  }





</style>
