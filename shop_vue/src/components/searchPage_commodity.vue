<template>
  <div class="commodity_box"  @click="gotoCommodityPage">
    <div class="box_top">
      <img :src="props_CommodityMessage.commodityPhoto">
    </div>
    <div class="bottom">
      <div class="bottomBox">
        <span id="prifix_price">￥</span>
        <span id="price">{{props_CommodityMessage.commodityPrice.toFixed(2)}}</span>
        <span v-show="props_CommodityMessage.baoYou" id="baoYou">包邮</span>
        <div class="commoditySalesBox"><span>{{props_CommodityMessage.commoditySales}}人付款</span></div>
        <div class="msgBox"><span>{{props_CommodityMessage.commodityName}}</span></div>
        <div class="storeBox">
          <div class="storeBox_left"><span>{{props_CommodityMessage.storeBase.storeName}}</span></div>
          <div class="storeBox_right">
            <span>{{address.province}}</span>
            <span>{{address.city}}</span  >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import city_code from '@/components/city_code'
  export default {

    name: "searchPage_commodity",
    props:['props_CommodityMessage'],
    computed:{
      address(){
        var cityCode = this.props_CommodityMessage.storeBase.cityCode;
        return city_code.getAddress(cityCode);
      }
    },
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
    },
    created() {
    }
  }
</script>

<style scoped>
  span{
    font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";
  }
  .commodity_box{
    box-sizing: border-box;
    width: 250px;
    height: 380px;
    cursor: pointer;
    border: 1.5px solid #EEEEEE;
    margin: 0px auto;
  }
  .commodity_box:hover{
    border: 2px solid #F85300;
  }

  .box_top{
    width: 100%;
    height: 239px;
    margin-top: 11px;
    text-align: center;
  }
  .box_top img{
    /*margin: 0px auto;*/
    height: 100%;
    max-width: 98%;
  }


  .bottom{
    width: 100%;
    height: 130px;
    box-sizing: border-box;
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
    line-height: 30px;
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
    line-height: 25px;
    font-size: 12.5px;
    color: #888888;
  }

  .msgBox{
    width: 226px;
  }

  .msgBox span{
    font-size: 13px;
    line-height: 24px;
  }

  .storeBox{
    position: absolute;
    bottom: 13px;
    line-height: 23px;

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
