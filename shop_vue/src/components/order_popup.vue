<template>
    <div>

      <!--      弹窗-->
      <div id="background" v-show="order_popup_flag" @click="leverOrderParent"></div>
      <!--      不用v-show  用v-if  这样每次都重新渲染 保证滚动条在顶部-->
      <div id="popupBox"  v-if="order_popup_flag">

        <div id="popup">

          <div class="orderSon_title">
            <div style="position: absolute; left: 5px;margin-top: 5px;color: black;font-weight: 600;font-size: 14px">
              订单编号: <span style="font-weight: 700">{{orderParent.orderId}}</span>
            </div>
            <div style="position: absolute; left: 140px;width: 80px">商品信息</div>
            <div style="position: absolute; left: 378px;width: 40px">数量</div>
            <div style="position: absolute; left: 490px;width: 40px">单价</div>
            <div style="position: absolute; left: 630px;width: 40px">小计</div>
          </div>
          <div style="height: 50px"></div>
          <div class="orderSon_itemBox" v-for="item in orderParent.orderSonList">
            <img class="orderSon_img" :src="item.orderCommodityVO.commodityPhoto">
            <div  class="orderSon_CommodityName">
              <span>{{item.orderCommodityVO.commodityName}}</span>
            </div>
            <span class="orderSon_ChooseNumber">  {{item.purchaseQuantity}} 件</span>
            <span class="orderSon_CommodityPrice">￥{{item.commodityPrice.toFixed(2)}}</span>
            <span class="orderSon_SumPrice">      ￥{{item.purchaseQuantity * item.commodityPrice.toFixed(2)}}</span>
          </div>
          <div style="height: 60px"></div>
          <div class="orderSon_bottom">
            <div style="position: relative">
              <div class="orderParent_user_store_Name">
                <div>买家 :  <span>{{orderParent.userName}}</span></div>
                <div>店铺 :  <span>{{orderParent.storeName}}</span></div>
              </div>
              <div class="orderParent_orderTime">下单时间 :  <span>{{orderParent.orderTime}}</span></div>
              <div class="orderParent_SunPrice">合计 : <span>{{orderParent_SunPrice.toFixed(2)}}</span></div>
            </div>
          </div>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
        name: "order_popup",
      props:['orderParent','refresh_popup_flag'],
      data(){
        return{
          order_popup_flag:false
        }
      },
      computed:{
        orderParent_SunPrice(){
          var sum = 0;
          for (let i in this.orderParent.orderSonList)
            sum += this.orderParent.orderSonList[i].purchaseQuantity * this.orderParent.orderSonList[i].commodityPrice;
          return sum;
        }
      },
      watch:{
        refresh_popup_flag(){
          this.order_popup_flag = true;
        }
      },
      methods:{
        leverOrderParent(){
          this.order_popup_flag = false;
        }
      }
    }
</script>

<style scoped>

  #background {

    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 999999;
  }

  #popupBox {
    position: fixed;
    top: 60px;
    left: 52%;
    transform: translate(-50%);
    z-index: 9999999;
  }

  #popup{
    box-shadow: 0px 0px 10px #ddd;
    border: #ebebeb solid 1px;
    background: #F8F8F8;
    border-radius: 5px;
    position: relative;
    width: 730px;
    height: 600px;
    margin: 0px auto;
    /*overflow:scroll;*/
    overflow-x: hidden;
  }
  #popup div{
    background: #F8F8F8;

  }

  .orderSon_title{
    position: fixed;
    display: inline-block;
    z-index: 250;
    width: 710px;
    height: 48px;
    color: #FF4400;
    font-weight: 600;

  }
  .orderSon_title div{
    margin-top: 15px;
  }




  .orderSon_itemBox{
    height: 65px;
    position: relative;
  }

  .orderSon_img{
    width: 60px;
    height: 60px;
    position: absolute;
    left: 25px;
    top: 5px;
  }

  .orderSon_CommodityName{
    overflow: hidden;
    text-overflow:ellipsis;
    display: -webkit-box;
    -webkit-box-orient: vertical;
    -webkit-line-clamp: 2;
    width:210px;
    height: 50px;
    position: absolute;
    left: 120px;
    top: 3px;
  }

  .orderSon_ChooseNumber{
    position: absolute;
    left: 380px;
    top: 6px;
  }

  .orderSon_CommodityPrice{
    position: absolute;
    left: 480px;
    top: 6px;
  }


  .orderSon_SumPrice{
    position: absolute;
    left: 620px;
    top: 6px;
  }


  .orderSon_bottom{
    /*float: bottom;*/
    position: fixed;
    z-index: 250;
    height: 55px;
    width: 720px;
    top: 545px;
    line-height: 10px;
  }

  .orderParent_orderTime{
    position: absolute;
    left: 30px;
    top: 33px;
  }
  .orderParent_orderTime span{
    font-weight: 600;
  }
  .orderParent_user_store_Name{
    position: absolute;
    left: 30px;
    top: 10px;
  }
  .orderParent_user_store_Name span{
    font-weight: 600;
  }
  .orderParent_user_store_Name div{
    display: inline-block;
    min-width: 120px;
  }


  .orderParent_SunPrice{
    position: absolute;
    right: 20px;
    top: 17px;
    font-size: 20px;
    font-weight: 600
  }
  .orderParent_SunPrice span{
    color: #FE5525;
    font-size: 30px;
    font-weight: 700;
  }



  #popup::-webkit-scrollbar
  {
    width: 5px;
    height: 16px;
    background-color: #F5F5F5;
  }


  #popup::-webkit-scrollbar-thumb
  {
    border-radius: 20px;
    -webkit-box-shadow: inset 0 0 6px rgba(0,0,0,.3);
    background-color: #F28328;
  }
</style>
