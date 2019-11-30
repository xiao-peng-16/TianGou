<template>
    <div>
      <nav_top :flag_fixed="true" :flag_scroll="true"/><br/>
      <div style="position: fixed;width: 100%;height: 70px; background: white;z-index: 100;padding-top: 10px;border-bottom: 1px solid #DADADA">
<!--      <div style="width: 100%;height: 70px; background: white;z-index: 100;padding-top: 10px;border-bottom: 1px solid #DADADA">-->
        <img  src="../assets/tmall.jpg">
        <span id="title">— 卖家中心 —</span>
      </div>


      <div class="buttom" style="margin-top: 70px">
        <div class="left">
            <div @click="options=1" :class="{optionsBox:options==1,not_optionsBox:options!=1}"><span>店铺概况</span></div>
            <div @click="options=2" :class="{optionsBox:options==2,not_optionsBox:options!=2}"><span>商品管理</span></div>
            <div @click="options=3" :class="{optionsBox:options==3,not_optionsBox:options!=3}"><span>订单管理</span></div>
        </div>
        <div class="right">
          <component :is="optionsComponents" style="z-index:0;padding-left: 280px"/>
        </div>
      </div>




    </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    import StoreCommodityManage from "@/components/StoreCommodityManage";
    import StoreHome from "@/components/StoreHome";
    import StoreOrderManage from "@/components/StoreOrderManage";
    import Hint_popup from "@/components/hint_popup";
    export default {
        name: "store_center",
      components: {Hint_popup, StoreHome, StoreCommodityManage, StoreOrderManage, Nav_top},
      data(){
          return{
            options:1,
            optionsComponents:'StoreHome',
            background:'while'
          }
        },
        watch:{
          options(val){
            switch (val) {
              case 1: this.optionsComponents='StoreHome'; this.background = 'while'; break;
              case 2: this.optionsComponents='StoreCommodityManage'; this.background = '#FCFCFC'; break;
              case 3: this.optionsComponents='StoreOrderManage'; this.background = '#FCFCFC'; break;
            }
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
