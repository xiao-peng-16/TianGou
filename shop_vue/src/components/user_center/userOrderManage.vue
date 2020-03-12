<template>
  <div  style="background: #F8F8F8;min-height:100%;padding-left: 35px">


    <order_popup :order-parent="orderParent" :refresh_popup_flag="refresh_popup_flag" style="z-index: 9999999"/>

    <div>
      <div style="z-index: 10;position: fixed;height: 30px;width: 1080px;background: #F8F8F8" >
        <span style="position: relative; left: 150px">商品信息</span>
        <span style="position: relative; left: 425px">店铺</span>
        <span style="position: relative; left: 530px">总付款</span>
        <span style="position: relative; left: 638px">下单时间</span>
        <span style="position: relative; left: 740px">支付状态</span>
      </div>

      <div style="height: 30px;"></div>




      <div class="listBox">
        <div class="itemBox" v-for="item in orderParentList" @click="enterOrderParent(item)">
          <div v-if=" undefined != item.orderSonList">
            <div class="only_imgBox"  v-if="item.only_img">
              <img :src="item.orderSonList[0].commodityPhoto">
            </div>
            <div class="multi_imgBox" v-else  >
              <img v-for="orderCommodityVO in item.orderSonList" :src="orderCommodityVO.commodityPhoto">
            </div>
          </div>

          <div class="cNameBox" v-if=" undefined != item.orderSonList">
            <div class="cNameboxItem" v-for="(orderCommodityVO,index) in item.orderSonList">
              <span>{{getCommodityName(orderCommodityVO.commodityName, index, item.orderSonList.length)}}</span><br>
            </div>
          </div>

          <div class="userName">
            <span>{{item.storeName}}</span>
          </div>
          <div class="orderTotalPrice">
            <span>￥{{item.orderTotalPrice.toFixed(2)}} 元</span>
          </div>
          <div class="orderTime">
            <span>{{item.orderTime}}</span>
          </div>
          <div class="userOrderState">
            <span>{{return_userOrderState_word(item.orderState)}}</span>
          </div>
        </div>
      </div>


    </div>




    <div v-show="flag_notData" style="margin-top: 30px;margin-left: -30px">
      <not-data middle="没有对应的订单"/>
    </div>


  </div>
</template>

<script>
  import Order_popup from "@/components/order_popup";
  import NotData from "@/components/notData";
  export default {
    name: "userOrderWaitPayment",
    components: {NotData, Order_popup},
    data(){
      return{
        orderParentList : [],
        //详细订单
        orderParent:undefined,
        refresh_popup_flag:0,

        flag_notData:false
      }
    },
    computed:{
      user_center_left_son_options(){
        return parseInt(this.$route.query.ls);
      }
    },
    watch:{
      user_center_left_son_options(){
        this.init();
      }
    },

    methods:{
      getCommodityName(commodityName, index, length){
        if (index <3){
          return commodityName;
        }else if (index === 3){
          return '......共'+length+'件商品';
        }
      },
      return_userOrderStateByStore(){
        switch (this.user_center_left_son_options) {
          case 0: return undefined;
          case 1: return this.GLOBAL.userOrderState.WAIT_PAYMENT;
          case 2: return this.GLOBAL.userOrderState.WAIT_SHIPMENTS;
          case 3: return this.GLOBAL.userOrderState.WAIT_RECEIVING;
          case 4: return this.GLOBAL.userOrderState.WAIT_EVALUATED;
          default: return undefined;
        }
      },
      return_userOrderState_word(val){
        switch (val) {
          case this.GLOBAL.userOrderState.WAIT_PAYMENT:   return '待付款';
          case this.GLOBAL.userOrderState.WAIT_SHIPMENTS: return '待发货';
          case this.GLOBAL.userOrderState.WAIT_RECEIVING: return '待收货';
          case this.GLOBAL.userOrderState.WAIT_EVALUATED: return '待评价';
        }
      },
      click_img(commodityId){
        this.$router.push({name:'commodityPage',query:{commodityId}})
      },
      enterOrderParent(orderParent){
        this.orderParent = orderParent;
        this.refresh_popup_flag++;
      },
      init(){
        this.$axios.get('/order/listUserOrderParentByUserId',{
          params:{
            orderState:this.return_userOrderStateByStore()
          }
        }).then(res=>{
          if (this.$store.getters.getResultDispose(res)) {
            this.orderParentList = res.data;
            this.flag_notData = 0 == this.orderParentList.length;

            this.orderParentList.forEach(orderParent =>{

              //图片大于一张  就小图
              if (orderParent.orderSonList.length > 1)
                orderParent.only_img = false;
              else
                orderParent.only_img = true;
            });

          }
        });
      }

    },
    created() {
      this.init();
    }
  }
</script>

<style scoped>


  .listBox{
    height: 100%;
  }

  .itemBox{
    background: #FCFCFC;
    border: 1px solid #CCCCCC;
    box-sizing: border-box;
    padding:  10px 10px 10px 10px;
    width:1080px;
    height: 140px;
    cursor: pointer;
    position: relative;
  }
  .itemBox div{
    display: inline-block;
  }
  .itemBox:hover{
    text-decoration:none;
    color: #FF6600;
  }

  .only_imgBox{
    width: 130px;
    box-sizing: border-box;
    padding-top: 3px;
  }
  .only_imgBox img{
    float: left;
    height: 115px;
    width: 115px;
  }
  .multi_imgBox{
    width: 115px;
    height: 115px;
    overflow: hidden;
  }
  .multi_imgBox img{
    margin: 0px;
    float: left;
    height: 57.5px;
    width:  57.5px;
  }

  .cNameBox{
    width: 280px;
    float: left;
    position: absolute;
    left: 145px;
    margin-top: 10px;
  }

  .cNameboxItem{
    width: 280px;
    display: block;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }

  .userName{
    text-align: center;
    width:120px;
    position: absolute;
    left: 450px;
    top: 50px;
  }
  .orderTotalPrice{
    width:150px;
    position: absolute;
    left: 630px;
    top: 50px;

  }

  .orderTime{
    width:300px;
    position: absolute;
    left: 775px;
    top: 50px;
  }


  .userOrderState{
    position: absolute;
    left: 970px;
    top: 50px;
  }



</style>
