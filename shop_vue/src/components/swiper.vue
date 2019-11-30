<template>
  <div class="swiperBox" ref="swiperBox"  @mouseup="mouseup" @mousemove="mousemove" @mouseenter="mouseenter"  @mouseleave="mouseleave">
    <div style="position: relative;">

      <!-- 左侧按钮 -->
      <div class="arrow-l" @click="indexSub()"><</div>
      <!-- 右侧按钮 -->
      <div class="arrow-r" @click="indexAdd()">></div>
      <!-- 核心的滚动区域 -->
      <ul class="imgList" ref="imgList">
        <li v-for="item in imgList">
          <img :src="item.imgURI" ref="img" @mousedown="mousedown($event, item.commodityId)" >
        </li>

        <li>
          <img :src="imgList[0].imgURI">
        </li>
      </ul>
      <!--下面的小圈圈-->
      <ul class="optionList">
        <li v-for="(item,index) in imgList">
          <div @click="indexOption=index" :class="{indexOption:show_indexOption==index}"></div>
        </li>
      </ul>
    </div>


  </div>
</template>

<script>

    export default {
        name: "swiper",
      props: ['imgList'],
      data(){
          return{
            flag_indexChange:true,
            indexOption:0,
            show_indexOption:0,

            flag_move:false,
            mousedown_clientX:0,
            img_commodityId:0,
          }
      },
      computed:{
        listLength(){
          return this.imgList.length;
        },
        imgWidth(){
          return this.$refs.swiperBox.offsetWidth;
        }
      },
      watch:{
        indexOption(val){
          this.show_indexOption = val;
          this.animate(val)
        }
      },
      mounted(){
        this.$refs.swiperBox.firstChild.style.width = this.imgWidth + "px";
        this.$refs.swiperBox.firstChild.style.height = this.$refs.swiperBox.offsetHeight + "px";
        this.$refs.imgList.style.width = (this.listLength +1) + "00%";
        var imgArray =  document.querySelectorAll('.imgList img');
        for (var i =0;i < (this.listLength +1);i++){
          imgArray[i].style.width = this.imgWidth + "px";
          imgArray[i].style.height = this.$refs.swiperBox.offsetHeight + "px";
        }
        this.set_swiperBox_timer();
      },
      methods:{
        set_swiperBox_timer(){
          clearInterval(this.$refs.swiperBox.timer);
          let tempThis = this;
          this.$refs.swiperBox.timer = setInterval(function() {
            tempThis.indexAdd();
          },2000);
        },
        animate(index, callback){
          let target = -index* this.imgWidth;
          let obj = this.$refs.imgList;
          let tempThis = this;
          // 先清除以前的定时器，只保留当前的一个定时器执行
          clearInterval(obj.timer);
          obj.timer = setInterval(function() {
            // 步长值写到定时器的里面
            // 把我们步长值改为整数 不要出现小数的问题
            var step = (target - obj.offsetLeft) / 10;
            step = step > 0 ? Math.ceil(step) : Math.floor(step);
            if (obj.offsetLeft == target) {
              // 停止动画 本质是停止定时器
              clearInterval(obj.timer);
              tempThis.flag_indexChange = true;
              callback && callback(tempThis); //如果又回调函数 就执行
            }
            // 把每次加1 这个步长值改为一个慢慢变小的值  步长公式：(目标值 - 现在的位置) / 10
            obj.style.left = obj.offsetLeft + step + 'px';
          }, 15);
        },
        indexAdd(){
          if (this.flag_indexChange){
            this.flag_indexChange = false;
            var temp_index = this.indexOption+1;
            if (temp_index != this.listLength){
              this.indexOption =temp_index;
            }else {
              //先 show_indexOption = 0    这样 小圈圈 才和图片同步刷新 （不信就注释看看）
              this.show_indexOption = 0;
              this.animate(temp_index, function (tempThis) {
                tempThis.$refs.imgList.style.left = '0px';
                tempThis.indexOption =0;
              });
            }
          }
        },
        indexSub(){
          if (this.flag_indexChange){
            this.flag_indexChange = false;
            if (this.indexOption != 0){
              this.indexOption =this.indexOption - 1;
            }else {
              //看下面的mousemove函数  拖拽图片 松开鼠标 不用this.$refs.imgList.style.left = -(this.listLength * this.imgWidth) + 'px';
              if (false == this.flag_move){
                this.$refs.imgList.style.left = -(this.listLength * this.imgWidth) + 'px';
              } else {
                this.flag_move = false;
              }
              this.indexOption =this.listLength - 1;
            }
          }
        },
        mousedown(event, commodityId){
          this.mousedown_clientX = event.clientX;
          this.img_commodityId = commodityId;
          event.preventDefault();
        },
        mouseup(event, commodityId){
          if (0 != this.mousedown_clientX){
            var difference =   this.mousedown_clientX - event.clientX ;
            this.mousedown_clientX = 0;
            //如果 差值为不为0  鼠标按下 的x坐标 和 抬起的x坐标 不一样
            //就是在拖拽图片
            if ( 0 != difference) {
              difference > 0 ?  this.indexAdd() : this.indexSub();
            }else {
              //打开该图片对应的商品页面
              this.click_img();
            }
          }
        },
        mouseleave(event){
          if (0 != this.mousedown_clientX){
            var difference =   this.mousedown_clientX - event.clientX ;
            this.mousedown_clientX = 0;
            difference > 0 ?  this.indexAdd() : this.indexSub();
          }
          this.set_swiperBox_timer();
        },
        mouseenter(){
          // 操作过程不要定时器 干扰
          clearInterval(this.$refs.swiperBox.timer);
        },
        mousemove(event){
          if (0 != this.mousedown_clientX){
            var difference =   this.mousedown_clientX - event.clientX ;
            console.log(difference)
            if (0 < difference || 0!=this.indexOption){
              this.$refs.imgList.style.left = -(this.indexOption * this.imgWidth + difference )+ 'px';
            } else {
              this.$refs.imgList.style.left = -(this.listLength * this.imgWidth  + difference)+ 'px';
              this.flag_move = true;
            }
          }
        },
        click_img(){
          const {href} = this.$router.resolve({
            name:'commodityPage',
            query:{
              commodityId:this.img_commodityId
            }
          });
          window.open(href,'_blank');
        }


      }
    }
</script>

<style scoped>
  .arrow-l, .arrow-r{
    cursor: pointer;
    display: none;
    position: absolute;
    top: 45%;
    width: 24px;
    height: 40px;
    background: rgba(0, 0, 0, .3);
    text-align: center;
    line-height: 40px;
    color: #fff;
    font-size: 18px;
    z-index: 2;
  }
  .arrow-r{
    right: 0px;
  }
  .swiperBox:hover .arrow-l, .swiperBox:hover .arrow-r{
    display: block;
  }
  .swiperBox{
    /*写在 swiperBox 的div 里面了，这样swiperBox 可以不relative*/
    /*position: relative;*/
    overflow: hidden;

    /*写在调用方了*/
    /*top: -520px;*/
    /*left: 23%;*/
    /*width: 900px;*/
    /*height: 512px;*/
  }
  .imgList img{
    cursor: pointer;

    /*写 mounted（）里面了*/
    /*width: 900px;*/
    /*height: 512px;*/
  }
  .imgList{
    padding: 0px;
    position: absolute;
    /*width: 9900%;*/   /*写 mounted（）里面了*/
    list-style: none;
  }
  .imgList li{
    float: left;
  }
  .optionList{
    position: absolute;
    bottom: 1%;
    left: 1%;
    list-style: none;
  }
  .optionList li{
    float: left;
    z-index: 2;
  }
  .optionList li div{
    cursor: pointer;
    border-radius: 20px;
    border: 2px solid rgba(255, 255, 255, 0.5);
    margin: 0px 7px;
    width: 15px;
    height: 15px;

    transition:background 0.5s;
    -moz-transition:background 0.5s; /* Firefox 4 */
    -webkit-transition:background 0.5s; /* Safari and Chrome */
    -o-transition:background 0.5s; /* Opera */
  }
  .indexOption{
    background: white;
  }
</style>
