<template>
    <div >
      <div ref="nav_top">
        <nav_top></nav_top>
      </div>

      <div class="about_box">
          <img src="http://communications-1067227.pic42.websiteonline.cn/upload/je6v.jpg">



        <transition name="transition_title">
          <div v-if="show_title" class="about_title"><span>仿天猫分布式电商网站</span></div>
        </transition>



        <transition name="transition_content">
          <div v-if="show_content" class="about_content">

            <div>作者 : 蔡晓鹏</div>
            <div>GitHub : <a href="https://github.com/cxp-Git/TianGou" target="_blank">https://github.com/cxp-Git/TianGou</a></div>

            <div>前端 ：Vue + Element</div>
            <div>后台 : SpringCloud + Maven + Mybatis + Redis</div>
          </div>
        </transition>


      </div>
    </div>
</template>

<script>
  import Nav_top from "@/components/nav_top";
    export default {
        name: "about",
      components: {Nav_top},
      data(){
          return{
            show_title:false,
            show_content:false,
          }
      },
      mounted() {
        document.querySelector('.about_box').style.height = window.innerHeight - this.$refs.nav_top.offsetHeight + 'px';
        this.show_title = true;
        this.show_content = true;
      }
    }
</script>

<style scoped>
  .about_box{
    height: 100px;
    width: 100%;
    background: #138496;
    position: relative;
    color: white;
    font-family: ublox,Arial,Helvetica Neue,Helvetica,sans-serif;

  }
  .about_box img{
    width: 100%;
    height: 100%;
  }
  .about_title{
    position: absolute;
    top: 20%;
    left: 13%;
    font-size: 40px;
    font-weight: 800;
    letter-spacing: 5px;
  }
  .about_content{
    position: absolute;
    top: 35%;
    left: 13%;
    font-size: 22px;
    font-weight: 700;
  }


  a{
    text-decoration: none;
  }



  .transition_title-enter-active, .transition_title-leave-active {
    transition: opacity 2s;
  }
  .transition_title-enter, .transition_title-leave-to /* .transition_title-leave-active below version 2.1.8 */ {
    opacity: 0;
  }

  .transition_content-enter-active, .transition_content-leave-active {
    transition: opacity 4.5s;
  }
  .transition_content-enter, .transition_content-leave-to /* .transition_title-leave-active below version 2.1.8 */ {
    opacity: 0;
  }
</style>
