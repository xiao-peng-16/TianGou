<template>
  <div>
    <nav_top :flag_fixed="true" :flag_scroll="true" />
    <div style="position: fixed;width: 100%;height: 70px; background: white;z-index: 100;padding-top: 10px;border-bottom: 1px solid #DADADA">
      <img  src="../assets/tmall.jpg">
      <span id="title">— 用户中心 —</span>
    </div>


    <div class="buttom" style="margin-top: 70px">
      <div class="left">
        <div @click="options=0" :class="{optionsBox:options==0,not_optionsBox:options!=0}"><span>更改头像</span></div>
        <div @click="options=1" :class="{optionsBox:options==1,not_optionsBox:options!=1}"><span>更改密码</span></div>
      </div>
      <div class="right">
        <component :is="optionsComponents" style="z-index:0;padding-left: 280px"/>
      </div>
    </div>




  </div>
</template>

<script>
  import Nav_top from "@/components/nav_top";
  import Hint_popup from "@/components/hint_popup";
  import changeUserPhotoname from "@/components/user_center/changeUserPhotoname";
  import changeUserPassword from "@/components/user_center/changeUserPassword";

  export default {
    name: "user_center",
    components: {Hint_popup,  Nav_top, changeUserPhotoname, changeUserPassword},
    data(){
      return{
        options:0
      }
    },
    computed:{
      optionsComponents(){
        switch (this.options) {
          case 0: return  'changeUserPhotoname';
          case 1: return  'changeUserPassword';
          default :return 'changeUserPhotoname';
        }
      }
    },
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
