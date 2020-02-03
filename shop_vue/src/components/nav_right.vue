<template>

  <div class="right_navBox" ref="right_navBox" @mouseleave="leave_right_navBox">

    <div class="right_navSplit"></div>

    <div class="right_navContent">
      <!--      购物车 、 收藏夹 标题-->
      <div style="color: #5e5050;height: 40px;font: 17px/40px '微软雅黑';padding-left: 20px">
        <div v-if="options == 1" >
          <span class="iconfont" style="font-size: 19px;font-weight: 500">&#xe657;</span> 购物车
        </div>
        <div v-else-if="options == 2" >
          <span class="iconfont" style="font-size: 19px;font-weight: 500">&#xeca1;</span> 收藏夹
        </div>
      </div>


<!--      购物车 、 收藏夹 列表-->
      <div class="itemBoxList" ref="itemBoxList">
        <div v-if="options == 1" >
          <div class="itemBox" v-for="item in cartList">
            <div class="imgBox">
              <img :src="item.commodityPhoto" @click="gotoCommodityPage(item.commodityId)">
            </div>
            <div class="commodityNameBox"  @click="gotoCommodityPage(item.commodityId)">
              <span>{{item.commodityName}}</span>
            </div>
            <div class="commodityPriceBox" >
                <div v-if="hint_commodity(item)" style="color:red;">{{hint_commodity(item)}}</div>
                <div v-else><span>￥{{item.cartCommodityVO ? item.cartCommodityVO.commodityPrice.toFixed(2) : undefined}}</span>×{{item.purchaseQuantity}}</div>
            </div>
          </div>
        </div>

        <div v-else-if="options == 2">
          <div class="itemBox"  v-for="item in favoriteList">
            <div class="imgBox">
              <img :src="item.commodityPhoto" @click="gotoCommodityPage(item.commodityId)">
            </div>
            <div class="commodityNameBox"  @click="gotoCommodityPage(item.commodityId)">
              <span>{{item.commodityName}}</span>
            </div>
            <div class="commodityPriceBox" >
              <span>￥{{item.commodityPrice.toFixed(2)}}</span>
            </div>
          </div>
        </div>


      </div>



      <!--      购物车 、 收藏夹 底部-->
      
      <div class="bottomBox" v-if="options == 1">
        <div class="bottomMsgBox">
            <div><span>{{cartNumber}}</span>件商品</div>
            <div>共计: <span>￥{{sumPrice.toFixed(2)}}</span></div>
        </div>
        <div class="bottom_goto" @click="toCart">
            去购物车结算
        </div>
      </div>

      <div class="bottomBox" v-if="options == 2">
        <div class="bottomMsgBox">
          <div style="margin-top: 15px"><span>{{favoriteList.length}}</span>件宝贝</div>
        </div>
        <div class="bottom_goto" @click="toFavorite">
          查看收藏夹
        </div>
      </div>


    </div>





    <ul class="right_navTitleBox">
      <li @click="click_cart" @mouseenter="mouseenter($refs.cart)" @mouseleave="mouseleave($refs.cart)">
        <div class="right_navTitleMsg" ref="cart"> 购物车 </div>
        <div class="right_navTitleImg">
          <span class="iconfont">&#xe657;</span>
          <div class="cartNumberBox">{{cartNumber}}</div>
        </div>
      </li>

      <li @click="click_favorite" @mouseenter="mouseenter($refs.favorite)" @mouseleave="mouseleave($refs.favorite)">
        <div class="right_navTitleMsg"  ref="favorite"> 收藏夹 </div>
        <div class="right_navTitleImg">
          <span class="iconfont">&#xeca1;</span>
        </div>
      </li>

      <li @click="backTop"  @mouseenter="mouseenter($refs.backTop)" @mouseleave="mouseleave($refs.backTop)">
        <div class="right_navTitleMsg" ref="backTop" style="padding-left: 15px;"> 回到顶部 </div>
        <div class="right_navTitleImg" ><span class="iconfont">&#xe76c;</span>
        </div>
      </li>
    </ul>


  </div>
</template>

<script>
    export default {
        name: "nav_right",
      data(){
        return{
          cartList:[],
          favoriteList:[],
          options:0
        }
      },
      computed:{
        cartNumber(){
          var cartNumber = this.$store.state.user.cartNumber;
          if (0<cartNumber)
            return cartNumber;
          else return 0;
        },
        sumPrice(){
          var sunPrice=0;
          if (!this.cartList)
            return 0;
          this.cartList.forEach((item)=>{
            if (item.cartCommodityVO)
              sunPrice += item.purchaseQuantity * item.cartCommodityVO.commodityPrice;
          });
          return sunPrice;
        }
      },
      methods:{
        hint_commodity(item){
          if (undefined == item.cartCommodityVO)
            return '该商品已永久下架';
          if (!item.cartCommodityVO.commodityOnShelves)
            return '该商品已下架';
          return undefined;
        },
        gotoCommodityPage(commodityId){
          const {href} = this.$router.resolve({
            name:'commodityPage',
            query:{commodityId:commodityId}
          });
          window.open(href,'_blank');
        },
        toCart(){
          this.$router.push({name:'cart'})
        },
        toFavorite(){
          this.$router.push({name:'favorite'})
        },
        mouseenter(event){
          event.style.transform = "translate(-80px)"
        },
        mouseleave(event){
          event.style.transform = "translate(10px)"
        },
        leave_right_navBox(){
          this.$refs.right_navBox.style.transform = "translate(0px)"

        },
        click_cart(){
          this.options = 1;
          this.$axios.post('/cart/listCartCommodityVOByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){
                this.cartList = undefined == res.data ? [] : res.data;
              }
            });
          this.$refs.right_navBox.style.transform = "translate(-280px)"
        },
        click_favorite(){
          this.options = 2;
          this.$axios.get('/favorite/listFavoriteCommodityVOByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){
                this.favoriteList = undefined == res.data ? [] : res.data;
              }
            });
          this.$refs.right_navBox.style.transform = "translate(-280px)"
        },

        backTop () {
          let that = this;
          let timer = setInterval(() => {
            let ispeed = Math.floor(-that.scrollTop / 5)
            document.documentElement.scrollTop = document.body.scrollTop = that.scrollTop + ispeed;
            if (that.scrollTop === 0) {
              clearInterval(timer)
            }
          }, 1)
        },
        backTop () {
          let that = this;
          let timer = setInterval(() => {
            let ispeed = Math.floor(-that.scrollTop / 5)
            document.documentElement.scrollTop = document.body.scrollTop = that.scrollTop + ispeed;
            if (that.scrollTop === 0) {
              clearInterval(timer)
            }
          }, 0.6)
        },
        // 为了计算距离顶部的高度，当高度大于60显示回顶部图标，小于60则隐藏
        scrollToTop () {
          let that = this;
          let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
          that.scrollTop = scrollTop;
          if (that.scrollTop > 30) {
            that.btnFlag = true
          } else {
            that.btnFlag = false
          }
        },
      },
      mounted () {
        window.addEventListener('scroll', this.scrollToTop)
      },
      destroyed () {
        window.removeEventListener('scroll', this.scrollToTop)
      },

    }
</script>

<style scoped>

  .right_navBox{
    position: fixed;
    right: 0px;
    top: 0px;
    width: 7px;
    height: 100%;
    z-index: 1000;

    transform: translate(0px);
    transition:transform 0.8s;
    -moz-transition:transform 0.8s; /* Firefox 4 */
    -webkit-transition:transform 0.8s; /* Safari and Chrome */
    -o-transition:transform 0.8s; /* Opera */
  }

  .right_navSplit{
    width: 7px;
    height: 100%;
    background: #7A6D6D;
    position: fixed;
    z-index: 9999;
  }
  .right_navContent{
    width: 500px;
    height: 100%;
    background: #ECEAEA;
    position: fixed;
    z-index: 9998;

    padding-bottom: 90px;
  }

  .itemBoxList{
    background: white;
    width: 290px;
    padding-left: 13px;
    padding-right: 8px;

    max-height: 100%;
    overflow-x: hidden;
  }
  .itemBoxList::-webkit-scrollbar
  {
    width: 5px;
    height: 16px;
    background-color: #F5F5F5;
  }
  .itemBoxList::-webkit-scrollbar-thumb
  {
    border-radius: 20px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    background-color: #F28328;
  }



  .right_navTitleBox{
    position: relative;
    left: -40px;
    top: 40%;
    padding: 0px;
    list-style: none;
    width: 50px;
  }
  .right_navTitleBox li{
    cursor: pointer;
    margin-bottom: 3px;
    height: 40px;
  }

  .right_navTitleMsg{
    z-index: 50;
    width: 100px;
    border-radius: 4px;
    position: relative;
    top: 40px;
    height: 40px;
    background: #C81623;

    transform: translate(10px);
    transition:transform 0.7s;
    -moz-transition:transform 0.7s; /* Firefox 4 */
    -webkit-transition:transform 0.7s; /* Safari and Chrome */
    -o-transition:transform 0.7s; /* Opera */

    color: white;
    padding-left: 20px;
    line-height: 35px;
  }



  .right_navTitleImg{
    z-index: 999;
    position: relative;
    background: #7A6D6D;
    border-radius: 4px;
    width: 42px;
    height: 40px;
    padding-top: 3px;
    padding-left: 7px;
  }

  .right_navTitleBox li:hover .right_navTitleImg{
    background: #C81623;
  }

  .right_navTitleImg span{
    color: white;
    font-size: 25px;
  }
  .cartNumberBox{
    position: absolute;
    top: -5px;
    right: 4px;
    background: #D45454;
    border: 1.5px solid #D45454;
    min-width: 11px;
    color: white;
    border-radius: 5px;
    text-align: center;
    padding: 1px 2px 2px 2px ;
    font: 11px/11px verdana;

  }
  .right_navTitleBox li:hover .cartNumberBox{
    background: white;
    border: 1.5px solid #C81623;
    color: #C81623;
  }


  
  .itemBox{
    position: relative;
    height: 72px;
    border-top: 1px dashed #E0E0E0;
    padding-top: 10px;

  }

  .imgBox{
    cursor: pointer;
    position: absolute;
  }
  .imgBox img{
    width: 50px;
    height: 50px;

  }
  .commodityNameBox{
    position: absolute;
    cursor: pointer;
    height: 40px;
    left: 55px;
    font: 12px/150% tahoma,arial,Microsoft YaHei,Hiragino Sans GB,"\u5b8b\u4f53",sans-serif;
  }
  .commodityPriceBox{
    position: absolute;
    left: 55px;
    top: 45px;
    color: #666;
    font-weight: 400;
    font-family: Verdana;
    font-size: 12px;
  }
  .commodityPriceBox span{
    color: #c81623;
  }
  
  
  
  .bottomBox{
    position: fixed;
    bottom: 0px;
    height: 50px;
    width: 290px;
    padding-top: 7px;
  }
  .bottomMsgBox{
    float: left;
    font: 12px/150% tahoma,arial,Microsoft YaHei,Hiragino Sans GB,"\u5b8b\u4f53",sans-serif;
    margin-left: 12px;
  }
  .bottomMsgBox span{
    color: #c81623;
    font-family: verdana;
    font-weight: bold;
  }

  .bottom_goto{
    margin-left: 170px;
    width: 110px;
    height: 36px;
    background: #C81623;
    text-align: center;
    border-radius: 2px;
    cursor: pointer;
    color: white;
    padding-top: 5px;
  }

</style>
