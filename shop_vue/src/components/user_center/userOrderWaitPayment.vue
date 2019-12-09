<template>
  <div  :style="{background:this.GLOBAL.store_center_background}">


    <order_popup :order-parent="orderParent" :refresh_popup_flag="refresh_popup_flag"/>

    <div style="padding-left: 15px">
      <div style="z-index: 10;position: fixed;height: 30px;width: 1080px;" :style="{background:this.GLOBAL.store_center_background}">
        <span style="position: relative; left: 150px">商品信息</span>
        <span style="position: relative; left: 442px">店铺</span>
        <span style="position: relative; left: 560px">总付款</span>
        <span style="position: relative; left: 688px">交易时间</span>
      </div>

      <div style="height: 30px;"></div>




      <div class="listBox">
        <div class="itemBox" v-for="item in orderParentRoughList" @click="enterOrderParent(item.orderId)">
          <div class="only_imgBox"  v-if="item.only_img">
            <img :src="item.orderCommodityVOList[0].commodityPhoto">
          </div>
          <div class="multi_imgBox" v-else  >
            <div v-for="orderCommodityVO in item.orderCommodityVOList">
              <img :src="orderCommodityVO.commodityPhoto">
            </div>
          </div>

          <div class="cNameBox">
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

    methods:{
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



    },
    created() {


      this.$axios.get('/order/listUserOrderParentRoughByUserId')
        .then(res=>{
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
    width: 300px;
    float: left;
    position: absolute;
    left: 145px;
    margin-top: 10px;
  }

  .cNameboxItem{
    width: 300px;
    display: block;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }

  .userName{
    width:160px;
    position: absolute;
    left: 510px;
    top: 50px;
  }
  .orderSumPrice{
    width:150px;
    position: absolute;
    left: 660px;
    top: 50px;

  }

  .orderTime{
    width:300px;
    position: absolute;
    left: 825px;
    top: 50px;
  }



</style>
