<template>
    <div class="box"  @click="gotoCommodityPage">
        <div class="top">
          <img :src="this.GLOBAL.commodityImagesUrl + props_CommodityMessage.commodityPhotoname">
        </div>
        <div class="bottom">
            <div class="bottomBox">
              <span id="prifix_price">￥</span>
              <span id="price">{{props_CommodityMessage.commodityPrice.toFixed(2)}}</span>
              <span v-show="props_CommodityMessage.baoYou" id="baoYou">包邮</span>
              <div class="commoditySalesBox"><span>{{props_CommodityMessage.commoditySales}}人付款</span></div>
              <div class="msgBox"><span>{{props_CommodityMessage.commodityName}}</span></div>
              <div class="storeBox">
                <div class="storeBox_left"><span>{{props_CommodityMessage.storeToCommodity.storeName}}</span></div>
                <div class="storeBox_right">
                    <span>{{props_CommodityMessage.storeToCommodity.storeProvince}}</span>
                    <span>{{props_CommodityMessage.storeToCommodity.storeCity}}</span  >
                </div>
              </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {

        name: "searchPage_commodity",
        props:['props_CommodityMessage'],
        methods:{
          gotoCommodityPage(){

            const {href} = this.$router.resolve({
              name:'commodityPage',
              query:{
                //新页面 参数通过url 参数 不能直接json
                // CommodityMessage:this.$qs.stringify(this.props_CommodityMessage),
                commodityId:this.props_CommodityMessage.commodityId
              }
            });
            window.open(href,'_blank');
          }
        }
    }
</script>

<style scoped>
  .box{
    width: 250px;
    height: 380px;
    cursor: pointer;
    border: 1.5px solid #EEEEEE;
  }
  .box:hover{
    border: 2px solid #F85300;
  }

  .top{
    width: 100%;
    height: 250px;
    padding-top: 11px;
  }
  .top img{
    position: relative;
    height: 100%;
    max-width: 98%;
    left: 50%;
    transform: translate(-50%);
  }


  .bottom{
    width: 100%;
    height: 130px;
    padding: 10px;
    position: relative;
  }
  .bottomBox{
    height: 100%;
    width: 100%;
  }
  #prifix_price{
    color: #F85300;
    position: relative;
    bottom: 1px;
  }
  #price{
    color: #F85300;
    font-weight: bold;
    font-size: 20px;
  }
  #baoYou{
    background: #F85300;
    color: white;
  }
  .commoditySalesBox{
      float: right;
      margin-top: 2px;
  }
  .commoditySalesBox span{
    font-size: 12.5px;
    color: #888888;
  }


  .msgBox span{
    font-size: 13px;
  }
  
  .storeBox{
    position: absolute;
    bottom: 13px;
  }
  .storeBox span{
    font-size: 12.5px;
    color: #888888;
  }
  .storeBox span:hover{

  }

  .storeBox_left{
    float: left;
    text-decoration:underline;

  }
  .storeBox_left span{
  }

  .storeBox_left span:hover{
    color: #FF4400;
  }


  .storeBox_right{
    width: 300px;
    text-align: right;


    position: relative;
    left: 25%;
    transform: translate(-50%);
  }
</style>
