<template>
  <div>
    <nav_top :flag_fixed="true" :flag_scroll="true" />
    <div style="position: fixed;width: 100%;height: 110px;padding-top: 50px; background: white;z-index: 100;border-bottom: 1px solid #DADADA">
      <img  src="../assets/tmall.jpg" style="float: left;width: 243px;height: 45px">
      <div class="top_box">
        <span id="title">— 用户中心 —</span>
        <div class="top_options">
          <span @click="click_top_options(0)">全部功能</span>
          <span @click="click_top_options(1)">账号管理</span>
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
          <transition name="fade"  v-for="(son_item,son_index) in item.son_list">
            <div  class="left_itemBox_son"  v-show="undefined!=item.son_list && item.flag_show_son_list" @click="click_left_son_options(son_index)"  :class="{optionsBox:is_shine_target(index,son_index),not_optionsBox:!is_shine_target(index,son_index)}">
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

  export default {
    name: "user_center",
    components: {Hint_popup,  Nav_top,
      userOrderManage:resolve => {
        require(['../components/user_center/userOrderManage'],resolve)
      },
      changeUserPhoto:resolve => {
        require(['../components/user_center/changeUserPhoto'],resolve)
      },
      changeUserPassword:resolve => {
        require(['../components/user_center/changeUserPassword'],resolve)
      },
    },
    data(){
      return{

        t:undefined,
        l:undefined,
        ls:undefined,

        optionsComponents:undefined,
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
      query(){
        return this.$route.query;
      },
      top_options(){
        return parseInt(this.t);
      },
      left_options(){
        for (var i = 0;i<this.left_list.length;i++){
          if (undefined != this.left_list[i].son_list){
            if (i == this.l)
              this.left_list[i].flag_show_son_list = true;
            else
              this.left_list[i].flag_show_son_list = false;
          }
        }
        return this.l;
      },
      left_son_options(){
        return parseInt(this.ls);
      },
      left_list(){
        return this.top_options == 0 ? this.options_list_list : this.account_list;
      },

      flag_background(){
        return this.top_options == 0;
      }
    },
    watch:{
      query(){
        var query  = this.$route.query;
        this.t = parseInt(query.t);
        this.l = parseInt(query.l);
        this.ls = parseInt(query.ls);
        this.setOptionsComponents();
      }
    },
    methods:{
      is_shine_target(l,ls){
          var query  = this.$route.query;
          return l == query.l && ls == query.ls;
      },
      setOptionsComponents(){
        if (undefined != this.left_list[this.left_options].components){
          this.optionsComponents = this.left_list[this.left_options].components;
        } else if (undefined != this.left_son_options){
          this.optionsComponents = this.left_list[this.left_options].son_list[this.left_son_options].components;
        }else
          return false;
        return true;
      },
      setQuery(){
        if (this.setOptionsComponents){
          this.$router.push({name:'user_center',query:{
              t:this.t,
              l:this.l,
              ls:this.ls
            }});
        }
      },

      click_top_options(val){

        if (this.top_options == val)
          return;
        this.t = val;
        this.l = 0;
        this.ls = 0 == val ? 0 : undefined;
        this.setQuery();
      },
      click_left_options(val){
        if (undefined != this.left_list[val].route){
          this.$router.push(this.left_list[val].route)
        } else {
          if (this.l == val){
            this.left_list[val].flag_show_son_list = !this.left_list[val].flag_show_son_list;
          } else {
            this.l = val;
            this.ls = undefined;
            if (undefined == this.left_list[this.left_options].son_list)
              this.setQuery();
          }
        }
      },
      click_left_son_options(val){
        this.ls = val;
        this.setQuery();
      }
    },
    created() {
      //拷贝 this.query 无法修改
      var query  = JSON.parse(JSON.stringify(this.$route.query));
      this.t = parseInt(query.t);
      this.l = parseInt(query.l);
      this.ls = parseInt(query.ls);


      if (this.t != 1 || undefined == this.t)
        this.t = 0;
      if (0>this.l || this.left_list.length-1<this.l || undefined == this.l)
      this.l = 0;

      // 默认选项
      if (0 == this.t && this.l == 0  && undefined == this.ls){
        this.ls = 0;
      }

      this.setOptionsComponents();
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
