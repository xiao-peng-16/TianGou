<template>
  <div  style="background: #F8F8F8;min-height:100%;padding-left: 35px">


      <order_popup :order-parent="orderParent" :refresh_popup_flag="refresh_popup_flag" style="z-index: 9999999"/>

      <div>
        <div style="z-index: 10;position: fixed;height: 30px;width: 1080px;background: #F8F8F8" >
          <span style="position: relative; left: 150px">商品信息</span>
          <span style="position: relative; left: 445px">买家</span>
          <span style="position: relative; left: 560px">收益总额</span>
          <span style="position: relative; left: 688px">下单时间</span>
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
              <span>{{item.userName}}</span>
            </div>
            <div class="orderTotalPrice">
              <span>￥{{item.orderTotalPrice.toFixed(2)}} 元</span>
            </div>
            <div class="orderTime">
              <span>{{item.orderTime}}</span>

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
        name: "StoreOrderManage",
      components: {NotData, Order_popup},
      data(){
        return{
          //粗略 订单列表
          orderParentList:null,
          //缓存列表 缓存拿到过的详细订单
          cacheList_orderParent : [],
          //详细订单
          orderParent:undefined,
          refresh_popup_flag:0,

          flag_notData:false
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
        click_img(commodityId){
          this.$router.push({name:'commodityPage',query:{commodityId}})
        },
        enterOrderParent(orderParent) {
          this.orderParent = orderParent;
          this.refresh_popup_flag++;
        }
      },
      created() {


        this.$axios.get('/order/listStoreOrderParentByStoreId')
          .then(res=>{
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
    text-align: center;
    width:120px;
    position: absolute;
    left: 470px;
    top: 50px;
  }
  .orderTotalPrice{
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
