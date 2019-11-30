<template>
    <div>
      <nav_top id="top"/>
      <div class="smallImgBox"></div>
    </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    export default {
        name: "after_sale",
      components: {Nav_top}
    }
</script>

<style scoped>
  .smallImgBox{
    z-index: -1;
    position: relative;
    bottom: 10px;
    height: 768px;
    background-image: url("../assets/jiao_fu.jpg");
    background-repeat:no-repeat ;
    background-size:100% 100%;
  }
</style>
