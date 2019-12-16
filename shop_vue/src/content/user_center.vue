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
      <div class="left">
        <div  v-for="(item, index) in left_list">
          <div class="left_itemBox"  @click="click_left_options(index)"  :class="{optionsBox:left_options==index,not_optionsBox:left_options!=index}">
            <span>{{item.title}}</span>
            <div class="left_item_parent_arrows" :class="{left_item_parent_arrows_right:!item.flag_show_son_list}" v-show="undefined!=item.son_list">
              <span style="font-size: 15px;" class="iconfont">&#xe60f;</span>
            </div>
          </div>
          <transition name="fade"  v-for="(son_item,index) in item.son_list">
            <div  class="left_itemBox_son"  v-show="undefined!=item.son_list && item.flag_show_son_list" @click="click_left_son_options(index)"  :class="{optionsBox:left_son_options==index,not_optionsBox:left_son_options!=index}">
              <span>{{son_item.title}}</span>
            </div>
          </transition>
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
  import userOrderManage from "@/components/user_center/userOrderManage";

  export default {
    name: "user_center",
    components: {userOrderManage, Hint_popup,  Nav_top, changeUserPhoto, changeUserPassword},
    data(){
      return{
        account_list:[
          {
            title:'更换头像',
            components:'changeUserPhoto'
          },
          {
            title:'修改密码',
            components:'changeUserPassword'
          },
        ],
        options_list_list:[
          {
            title:'我的订单',
            // components:'userOrderManage',
            flag_show_son_list:false,
            son_list:[
              {
                title:'所有订单',
                components:'userOrderManage'
              },
              {
                title:'待付款',
                components:'userOrderManage'
              },
              {
                title:'待发货',
                components:'userOrderManage'
              },{
                title:'待收货',
                components:'userOrderManage'
              },
              {
                title:'待评价',
                components:'userOrderManage'
              },
            ]
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
      }
    },
    computed:{
      top_options(){
        //默认选项
        if (this.$store.state.user_center_top_options == 0 && this.left_options == 0 && undefined == this.$store.state.user_center_left_son_options)
          this.$store.state.user_center_left_son_options = 0;
        else if (this.$store.state.user_center_top_options == 1)
          this.$store.state.user_center_left_son_options = undefined;

        return this.$store.state.user_center_top_options;
      },
      left_options(){
        return this.$store.state.user_center_left_options;
      },
      left_son_options(){
        var user_center_left_son_options = this.$store.state.user_center_left_son_options;
        if (undefined != user_center_left_son_options)
          this.left_list[this.left_options ].flag_show_son_list = true;
        return user_center_left_son_options;
      },
      left_list(){
        return this.top_options == 0 ? this.options_list_list : this.account_list;
      },
      optionsComponents(){
        if (undefined != this.left_son_options){
          return this.left_list[this.left_options].son_list[this.left_son_options].components;
        }

        return  this.left_list[this.left_options].components;
      },
      flag_background(){
        return this.top_options == 0;
      }
    },
    watch:{
      top_options(){
        this.$store.state.user_center_left_options = 0;
        this.$store.state.user_center_left_son_options = undefined;
      },
      left_options(){
        this.$store.state.user_center_left_son_options = undefined;
      },
    },
    methods:{
      click_left_options(val){
        if (undefined != this.left_list[val].route){
          this.$router.push(this.left_list[val].route)
        }else if (this.$store.state.user_center_left_options == val && undefined != this.left_list[val].son_list){
          this.left_list[val].flag_show_son_list = !this.left_list[val].flag_show_son_list;
        } else{
          this.$store.state.user_center_left_options = val;
        }
      },
      click_left_son_options(val){
        console.log(this.$store.state.user_center_left_son_options)
        this.$store.state.user_center_left_son_options = val;
      }
    },
    created() {

    }
  }
</script>

<style scoped>

  .flag_background{
    background: #F8F8F8;
  }
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
  .optionsBox{
    background: #292929;
    border-left: 2px solid #F28328;

  }
  .not_optionsBox{
    border-left: 2px solid #333333;

  }
  .left_itemBox{
    padding: 10px 0px 10px 70px;
    color: #FAFAFA;
    letter-spacing:2px;
    font-weight: 400;
    cursor: pointer;
  }

  .left_item_parent_arrows_right{
    transform: rotate(-90deg);
  }
  .left_item_parent_arrows{
    display: inline-block;
    transition:transform 0.3s;
    -moz-transition:transform 0.3s; /* Firefox 4 */
    -webkit-transition:transform 0.3s; /* Safari and Chrome */
    -o-transition:transform 0.3s; /* Opera */
  }
  .left_itemBox_son{
    padding: 10px 0px 10px 105px;
    color: #FAFAFA;
    letter-spacing:2px;
    font-weight: 400;
    cursor: pointer;
  }

  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
    opacity: 0;
  }







</style>
