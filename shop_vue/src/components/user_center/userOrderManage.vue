<template>
  <div  style="background: #F8F8F8">


    <order_popup :order-parent="orderParent" :refresh_popup_flag="refresh_popup_flag" style="z-index: 9999999"/>

    <div style="padding-left: 15px">
      <div style="z-index: 10;position: fixed;height: 30px;width: 1080px;background: #F8F8F8" >
        <span style="position: relative; left: 150px">商品信息</span>
        <span style="position: relative; left: 425px">店铺</span>
        <span style="position: relative; left: 530px">总付款</span>
        <span style="position: relative; left: 638px">下单时间</span>
        <span style="position: relative; left: 740px">支付状态</span>
      </div>

      <div style="height: 30px;"></div>




      <div class="listBox">
        <div class="itemBox" v-for="item in orderParentRoughList" @click="enterOrderParent(item.orderId)">
          <div v-if=" undefined != item.orderCommodityVOList">
            <div class="only_imgBox"  v-if="item.only_img">
              <img :src="item.orderCommodityVOList[0].commodityPhoto">
            </div>
            <div class="multi_imgBox" v-else  >
              <div v-for="orderCommodityVO in item.orderCommodityVOList">
                <img :src="orderCommodityVO.commodityPhoto">
              </div>
            </div>
          </div>

          <div class="cNameBox" v-if=" undefined != item.orderCommodityVOList">
            <div class="cNameboxItem" v-for="orderCommodityVO in item.orderCommodityVOList">
              <span>{{orderCommodityVO.commodityName}}</span><br>
            </div>
          </div>
          <div class="userName">
            <span>{{item.storeName}}</span>
          </div>
          <div class="orderSumPrice">
            <span>￥{{item.orderSumPrice.toFixed(2)}} 元</span>
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

  </div>
</template>

<script>
  import Order_popup from "@/components/order_popup";
  export default {
    name: "userOrderWaitPayment",
    components: {Order_popup},
    data(){
      return{
        //粗略 订单列表
        orderParentRoughList:null,
        //缓存列表 缓存拿到过的详细订单
        cacheList_orderParent : [],
        //详细订单
        orderParent:undefined,
        refresh_popup_flag:0,
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
      enterOrderParent(orderId){
        //如果之前 访问过了 缓存列表有里记录
        for (let i in this.cacheList_orderParent){
          if (orderId == this.cacheList_orderParent[i].orderId){
            this.orderParent = this.cacheList_orderParent[i];
            this.refresh_popup_flag++;
            return;
          }
        }


        this.$axios.get('/order/selUserOrderParentByUserId',{
          params:{
            orderId:orderId
          }
        }).then(res=>{
          if (this.$store.getters.getResultDispose(res)) {
            this.orderParent = res.data.data;
            this.cacheList_orderParent.push(res.data.data);
            this.refresh_popup_flag++;
          }
        });
      },
      init(){
        this.$axios.get('/order/listUserOrderParentRoughByUserId',{
          params:{
            orderState:this.return_userOrderStateByStore()
          }
        }).then(res=>{
          if (this.$store.getters.getResultDispose(res)) {

            this.orderParentRoughList = res.data.data;
            for (let i in this.orderParentRoughList) {
              if (this.orderParentRoughList[i].orderCommodityVOList.length == 4) {
                var newStr = '. . . ' + "共" + this.orderParentRoughList[i].orderSumNumber + "件商品";
                this.orderParentRoughList[i].orderCommodityVOList[3].commodityName = newStr;
              }
              //图片大于一张  就小图
              if (this.orderParentRoughList[i].orderCommodityVOList.length > 1)
                this.orderParentRoughList[i].only_img = false;
              else
                this.orderParentRoughList[i].only_img = true;

            }

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
    padding-top: 3px;
  }
  .only_imgBox img{
    float: left;
    height: 115px;
    width: 115px;
  }
  .multi_imgBox{
    width: 130px;
  }
  .multi_imgBox img{
    margin: 0px;
    padding: 0px;
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
  .orderSumPrice{
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
