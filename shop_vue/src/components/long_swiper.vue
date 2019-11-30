<template>

  <div class="long_swiper" @mouseenter="mouseenter"  @mouseleave="mouseleave">
    <ul ref="ul">
      <li v-for="(item,index) in long_swiper_img_list">
          <div class="item_box" @click="click(item.commodityId)">
            <p v-show="0 == index%2" style="margin-bottom: 10px">{{item.commodityName}}</p>
            <img :src="item.imgURI">
            <p v-show="1 == index%2" style="margin-top: 10px">{{item.commodityName}}</p>
          </div>
      </li>

    </ul>
  </div>





</template>

<script>
    export default {
        name: "long_swiper",
      props:['long_swiper_img_list'],
      data(){
          return{
            len:0
          }
      },
      created(){
          //如果冰爽偶数删除最后一个
        if (0 != this.long_swiper_img_list.length%2)
          this.long_swiper_img_list.pop();
        this.len = this.long_swiper_img_list.length;

        for (var i =0;i<this.len;i++){
          this.long_swiper_img_list.push(this.long_swiper_img_list[i])
        }
      },
      mounted() {
          var ul = this.$refs.ul;
          ul.style.width = this.long_swiper_img_list.length * (ul.firstChild.offsetWidth) + "px";
          this.set_swiperBox_timer();
      },
      methods:{
        set_swiperBox_timer(){
          var ul = this.$refs.ul;
          var normLeft = -this.len * (ul.firstChild.offsetWidth);
          var left = ul.offsetLeft;
          ul.timer = setInterval(function () {
            var temp = left -1;
            left = (normLeft == left) ? 0 : temp;
            ul.style.left = left +"px";
          },15);
        },
        mouseenter(){
          clearInterval(this.$refs.ul.timer);
        },
        mouseleave(){
          this.set_swiperBox_timer();
        },
        click(commodityId){
          const {href} = this.$router.resolve({
            name:'commodityPage',
            query:{
              commodityId:commodityId
            }
          });
          window.open(href,'_blank');
        }
      }
    }
</script>

<style scoped>
  .long_swiper{
    position: relative;
    padding-top: 20px;
    height: 100%;
    width: 100%;
    overflow: hidden;
  }
  ul{
    position: absolute;
    left: 0px;

    list-style: none;
    /*padding: 0px 20px;*/
    padding: 0px;
    margin: 0px;
  }
  li{
    float: left;
  }
  li:nth-child(odd){
    margin-top: 30px;
  }
  .item_box{
    cursor: pointer;
    margin-left: 25px;
    margin-right: 25px;
  }
  img{
    width: 150px;
    height: 150px;
    }

  p{
    width: 150px;
    overflow: hidden;
    text-align: center;
    font: 14px/1.5 Microsoft YaHei,Heiti SC,tahoma,arial,Hiragino Sans GB,"\5B8B\4F53",sans-serif;
  }
</style>
