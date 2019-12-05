<template>
    <div  :style="{background:this.GLOBAL.store_center_background}">

      <div style="padding-left: 15px">
        <div style="z-index: 10;position: fixed;height: 30px;width: 1080px;" :style="{background:this.GLOBAL.store_center_background}">
          <span style="position: relative; left: 150px">商品信息</span>
          <span style="position: relative; left: 442px">买家</span>
          <span style="position: relative; left: 560px">收益总额</span>
          <span style="position: relative; left: 688px">交易时间</span>
        </div>

        <div style="height: 30px;"></div>




        <div class="listBox">
          <div class="itemBox" v-for="item in orderGeneraList" @click="enterOrderParent(item.orderId)">
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
              <span>{{item.orderParent.userName}}</span>
            </div>
            <div class="orderSumPrice">
              <span>￥{{item.orderSumPrice.toFixed(2)}} 元</span>
            </div>
            <div class="orderTime">
              <span>{{item.orderParent.orderTime}}</span>

            </div>
          </div>
        </div>



        <!--      弹窗-->
        <div id="background" v-show="popup_flag" @click="leverOrderParent"></div>
        <!--      不用v-show  用v-if  这样每次都重新渲染 保证滚动条在顶部-->
        <div id="popupBox"  v-if="popup_flag">

          <div id="popup">

            <div class="orderSon_title">
              <div style="position: absolute; left: 5px;margin-top: 5px;color: black; font-weight: initial;font-weight: 600;font-size: 16px">
                买家: <span style="font-weight: 800">{{orderParent.userName}}</span>
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
              <span class="orderSon_ChooseNumber">  {{item.chooseNumber}} 件</span>
              <span class="orderSon_CommodityPrice">￥{{item.commodityPrice.toFixed(2)}}</span>
              <span class="orderSon_SumPrice">      ￥{{item.orderSumPrice.toFixed(2)}}</span>
            </div>
            <div style="height: 60px"></div>
            <div class="orderSon_bottom">
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
        name: "StoreOrderManage",
      data(){
        return{
          popup_flag:false,
          //粗略 订单列表
          orderGeneraList:null,
          //详细订单
          orderParent:{},
          //缓存列表 缓存拿到过的详细订单
          cacheList_orderParent : []
        }
      },
      computed:{
        orderParent_SunPrice(){
          var sum = 0;
          for (let i in this.orderParent.orderSonList)
            sum += this.orderParent.orderSonList[i].orderSumPrice;
          return sum;
        }
      },
      methods:{
        click_img(commodityId){
          this.$router.push({name:'commodityPage',query:{commodityId}})
        },
        enterOrderParent(orderId){
          //如果之前 访问过了 缓存列表有里记录
          for (let i in this.cacheList_orderParent)
            if (orderId == this.cacheList_orderParent[i].orderId){
              this.orderParent = this.cacheList_orderParent[i];
              this.popup_flag = true;
              return;
            }

          this.$axios.post('/order/selStoreOrderParentByUserId',orderId)
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)) {
                this.orderParent = res.data.data;
                this.cacheList_orderParent.push(res.data.data);
                this.popup_flag = true;
              }
            });
        },
        leverOrderParent(){
          this.popup_flag = false;
        }
        

      },
      created() {


        this.$axios.get('/order/selStoreOrderGeneraVOByUserId')
          .then(res=>{
            if (this.$store.getters.getResultDispose(res)) {

              this.orderGeneraList = res.data.data;
              for (let i in this.orderGeneraList) {
                  if (this.orderGeneraList[i].orderCommodityVOList.length == 4) {
                    var newStr = '. . . ' + "共" + this.orderGeneraList[i].chooseNumber + "件商品";
                    this.orderGeneraList[i].orderCommodityVOList[3].commodityName = newStr;
                  }
                  //图片大于一张  就小图
                  if (this.orderGeneraList[i].orderCommodityVOList.length > 1)
                    this.orderGeneraList[i].only_img = false;
                  else
                    this.orderGeneraList[i].only_img = true;

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



  #background {

    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 200;
  }

  #popupBox {
    position: fixed;
    top: 60px;
    left: 52%;
    transform: translate(-50%);
    z-index: 300;
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
    float: bottom;
    position: fixed;
    z-index: 250;
    height: 55px;
    width: 720px;
    top: 545px;
  }
  .orderParent_orderTime{
    float: left;
    margin-left: 30px;
    margin-top: 16px;
  }
  .orderParent_orderTime span{
    font-weight: 600;
  }

  .orderParent_SunPrice{
    float: right;
    margin-right: 20px;
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
