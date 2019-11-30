<template>
  <div id="app" style="width: 100%;overflow: hidden">
    <div ref="box">
      <hint_popup :innerWidth="innerWidth"/>
      <router-view/>
    </div>
  </div>
</template>

<script>

import Hint_popup from "@/components/hint_popup";
export default {
  name: 'App',
  data(){
    return{
      innerWidth:0
    }
  },
  components: {Hint_popup},
  mounted() {
    //这样 滚动条的出现和消失 不会影响 总宽度  影响布局
    this.$refs.box.style.width = window.innerWidth +"px";
    this.innerWidth = window.innerWidth;
    window.onresize = () => {
      return (() => {
        this.$refs.box.style.width = window.innerWidth +"px";
        this.innerWidth = window.innerWidth;
      })()
    }
  }
}
</script>
  
<style>
#app{
  margin: 0px;
  padding: 0px;
}

</style>
