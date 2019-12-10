<template>
    <div style="">
        <nav_top :flag_fixed="true" :flag_scroll="true" />
        <div style="position: fixed;width: 100%;height: 110px;padding-top: 50px; background: white;z-index: 100;border-bottom: 1px solid #DADADA">
          <img  src="../assets/tmall.jpg" style="float: left">
          <div class="top_box">
            <span id="title">— 卖家中心 —</span>
          </div>
        </div>


      <div class="buttom">
        <div class="left">
            <div @click="change_options(0)" :class="{optionsBox:options==0,not_optionsBox:options!=0}"><span>店铺概况</span></div>
            <div @click="change_options(1)" :class="{optionsBox:options==1,not_optionsBox:options!=1}"><span>商品管理</span></div>
            <div @click="change_options(2)" :class="{optionsBox:options==2,not_optionsBox:options!=2}"><span>订单管理</span></div>
        </div>
        <div class="right">
          <component :is="optionsComponents" style=""/>
        </div>
      </div>




    </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    import StoreCommodityManage from "@/components/store_center/StoreCommodityManage";
    import StoreHome from "@/components/store_center/StoreHome";
    import StoreOrderManage from "@/components/store_center/StoreOrderManage";
    import Hint_popup from "@/components/hint_popup";
    export default {
        name: "store_center",
      components: {Hint_popup, StoreHome, StoreCommodityManage, StoreOrderManage, Nav_top},
      computed:{
        options(){
          return this.$store.state.store_center_left_options;
        },
        optionsComponents(){
          switch (this.options) {
            case 0: return  'StoreHome';
            case 1: return  'StoreCommodityManage';
            case 2: return  'StoreOrderManage';
            default :return 'StoreHome';
          }
        }
      },
      methods:{
          change_options(options){
            this.$store.state.store_center_left_options = options;
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


  .optionsBox{
    background: #292929;
    border-left: 2px solid #F28328;

  }
  .not_optionsBox{
    border-left: 2px solid #333333;

  }


  .right{
    width: 100%;
    position: absolute;
    padding-left: 280px;
    padding-top: 110px;
    background: #F8F8F8;
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
