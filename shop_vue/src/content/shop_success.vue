<template>
  <div>
    <div>
      <div class="smallImgBox">
        <img src="../assets/success.jpg">
      </div>
      <div class="returnBox" @click="to_return">
        <img src="../assets/shop_success_return.jpg">
      </div>
    </div>
  </div>
</template>

<script>
    export default {
        name: "shop_success"
      ,methods:{
          to_return(){
            this.$router.go(-1);
          }
      }
    }
</script>

<style scoped>


  .smallImgBox{
    position: absolute;
    top: 12%;
    left: 48%;
    transform: translate(-50%);
  }
  .smallImgBox img{
      height: 260px;
  }

  .returnBox{
    position: absolute;
    top: 43%;
    left: 46%;
    transform: translate(-50%);
    cursor: pointer;
  }
  .returnBox img{
    height: 120px;
  }
</style>
