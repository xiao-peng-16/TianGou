<template>
    <div style="z-index: 2;">
      <div style="position: relative;">
        <div @mouseenter="mouseenter" @mousemove="mousemove" @mouseleave="mouseleave">
          <div id="popup" ref="popup"></div>
          <div class="smallImgBox"  ref="smallImgBox" ><img ref="smallImg"  :src="imgSrc"></div>
        </div>
        <div ref="bigImgBox_position" style="position: absolute;top: 0px">
          <div class="bigImgBox" ref="bigImgBox"><img ref="bigImg" :src="imgSrc"></div>
        </div>
      </div>
    </div>
</template>
；
<script>
    export default {
        name: "img_amplifier",
      props:['imgSrc'],
      data(){
        return{
          popup_d:0,
          smallImgMax_w:0,
          smallImgMax_h:0,
        }
      },
      mounted(){

      },
      methods:{
        mouseenter(){
          this.$refs.bigImgBox_position.style.left = this.$refs.smallImgBox.firstChild.offsetWidth + 15 + "px";
          this.$refs.popup.style.display = 'inline-block';
          this.$refs.bigImgBox.style.display = 'inline-block';
          this.popup_d = this.$refs.popup.offsetWidth  ;
          this.smallImgMax_w = this.$refs.smallImgBox.offsetWidth - this.popup_d;
          this.smallImgMax_h = this.$refs.smallImgBox.offsetHeight - this.popup_d;
        },

        mouseleave(){
          this.$refs.popup.style.display = 'none';
          this.$refs.bigImgBox.style.display = 'none';
        },
        mousemove(event){
          var BoundingClientRect = this.$refs.smallImgBox.getBoundingClientRect();
          var left = event.clientX  - BoundingClientRect.left - this.popup_d/2;
          var top = event.clientY  - BoundingClientRect.top - this.popup_d/2;

          left = left>=0?left:0;
          top = top>=0?top:0;
          left = left<=this.smallImgMax_w?left:this.smallImgMax_w;
          top = top<=this.smallImgMax_h?top:this.smallImgMax_h;

          this.$refs.popup.style.left = left + 'px';
          this.$refs.popup.style.top = top + 'px';

          var bigBoxLeft = -left/this.smallImgMax_w * (this.$refs.bigImg.offsetWidth -this.$refs.bigImgBox.offsetWidth);
          var bigBoxTop = -top/this.smallImgMax_h * (this.$refs.bigImg.offsetHeight -this.$refs.bigImgBox.offsetHeight);

          this.$refs.bigImg.style.left = bigBoxLeft + 'px';
          this.$refs.bigImg.style.top = bigBoxTop + 'px';
        }
      }
    }
</script>

<style scoped>
  #popup{
    position: absolute;
    left: 0px;
    top: 0px;
    background: red;
    opacity: 0.4;
    width: 200px;
    height: 200px;
    display: none;
    cursor: move;

  }
  .smallImgBox{
    float: left;
    border: 1px solid gray;
  }
  .smallImgBox img{
    margin: 0px auto;
    height: 460px;
    max-width: 600px;
  }
  .bigImgBox {
    float: left;
    z-index: 10;
    border: 1px solid gray;
    display: none;
    height: 500px;
    width: 500px;
    overflow: hidden;
    position: relative;
  }
  .bigImgBox img{
    z-index: 10;

    position: absolute;
    left: 0px;
    top: 0px;
    height: 900px;
  }
</style>
