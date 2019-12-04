<template>
    <div>
      <nav_top :flag_fixed="true" :flag_scroll="true" />
      <div style="position: fixed;width: 100%;height: 70px; background: white;z-index: 100;padding-top: 10px;border-bottom: 1px solid #DADADA">
        <img  src="../assets/tmall.jpg">
        <span id="title">— 卖家中心 —</span>
      </div>


      <div class="buttom" style="margin-top: 70px">
        <div class="left">
            <div @click="change_options(0)" :class="{optionsBox:options==0,not_optionsBox:options!=0}"><span>店铺概况</span></div>
            <div @click="change_options(1)" :class="{optionsBox:options==1,not_optionsBox:options!=1}"><span>商品管理</span></div>
            <div @click="change_options(2)" :class="{optionsBox:options==2,not_optionsBox:options!=2}"><span>订单管理</span></div>
        </div>
        <div class="right">
          <component :is="optionsComponents" style="z-index:0;padding-left: 280px"/>
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
          return this.$store.state.store_center_options;
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
            this.$store.state.store_center_options = options;
          }
      }
    }
</script>

<style scoped>

  #title{
    position: relative;
    top: 7px;
    font-size: 16px;
    font-weight: 700;
    margin-left: 480px;
  }

  .buttom{
    margin-top: 10px;
  }



  .optionsBox{
    background: #292929;
    border-left: 2px solid #F28328;

  }
  .not_optionsBox{
    border-left: 2px solid #333333;

  }
  .left{
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


  .right{
    width: 100%;
    position: absolute;
  }


</style>
