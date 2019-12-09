<template>
  <div>
    <nav_top :flag_fixed="true"/>


    <div style="position: fixed;width: 100%;height: 60px;padding-top: 10px; background: white;z-index: 250">
      <img id="tiangou" src="../assets/tmall.jpg">
      <span id="shop_car">购物车</span>
    </div>
    <div style="height: 30px;"></div>



    <div v-if="flag_show">

        <div class="title">
          <div style="position: relative;">
            <div style="position: absolute; left: 21%;"><span>商品信息</span></div>
            <div style="position: absolute; left: 46%;width: 40px"><span>单价</span></div>
            <div style="position: absolute; left: 57%;width: 40px"><span>数量</span></div>
            <div style="position: absolute; left: 69.5%;width: 40px"><span>金额</span></div>
            <div style="position: absolute; left: 82%;width: 40px"><span>操作</span></div>
          </div>
        </div>

      <div style="height: 55px;"></div>
      <div class="container ">



        <div class="itemBox row" v-for="(item,index) in dataList">
          <input  v-model="checkIndexList" :value="index" type="checkbox" >
          <div class="col-lg-1 col-md-2">
            <img :src="item.commodityPhoto" @click="gotoCommodityPage(item.shopCar.commodityId)" style="cursor: pointer;">
          </div>
          <div class="col-3 cNameBox"  @click="gotoCommodityPage(item.shopCar.commodityId)">
            <span>{{item.commodityName}}</span>
            <div style=""><span>{{item.commodityDescribe}}</span></div>
          </div>
          <div class="commodityPrice col-1 center down" style="padding-left: 40px">
            <span>￥{{item.commodityPrice.toFixed(2)}}</span>
          </div>
          <div class="center down" style="padding-left: 80px">
            <button type="button" class="btn btn-info" @click="less(item)"><span>-</span></button>
            <input v-model.number="item.shopCar.chooseNumber" @input="input(item)">
            <button type="button" class="btn btn-info" @click="item.shopCar.chooseNumber++"><span>+</span></button>
          </div>
          <div class="price col-1 center down" style="margin-left: 88px">
            <span>￥{{(item.commodityPrice*item.shopCar.chooseNumber).toFixed(2)}}</span>
          </div>
          <div class="price col-1 center " style="margin-left: 89px;margin-top: 25px">
            <div style="padding-left: 20px"><span class="operationSpan" @click="delByShopCarId(item,index)" >删除</span></div>
            <div style="width: 200px"><span class="operationSpan" @click="shopCarToFavorite(item,index)" >移入收藏夹</span></div>
          </div>
        </div>
        <!--        不要删除  调列表 到底部距离-->
        <div class="placeholder"></div>


      </div>
      <div class="bottom_bj">
        <!--      <img src="../assets/shop_car_bj.jpg">-->
      </div>
      <div class="bottomMessage">
        <div class="operationBox">
          <input  v-model="checkAllFlag" type="checkbox" style="top: 3px; cursor: pointer">
          <span class="operationSpan" @click="checkAllFlag=!checkAllFlag">全选</span>
          <span class="operationSpan" @click="delByShopCarIdList" >删除</span>
          <span class="operationSpan" @click="shopCarListToFavorite" >移入收藏夹</span>
        </div>

        <div class="shop" @click="shop"><span>结 算</span></div>
        <div class="sumPriceBox">
          <span id="heji">合计 : </span>
          <span id="sumPrice">{{(this.sumPrice).toFixed(2)}}</span>
        </div>
      </div>


    </div>
    <div v-else>
      <div class="notData">
        <img src="../assets/search_notdata.png">
        <span class="side">旺~旺~旺~</span>
        <span class="maddle">你的购物车还没有商品哟，还不给我去购物</span>
      </div>
    </div>

  </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    import Hint_popup from "@/components/hint_popup";
    export default {
        name: "shop_car",
      components: {Hint_popup, Nav_top},
      data(){
          return{
            dataList:[],
            checkIndexList:[],
            checkAllFlag:false
          }
        },
        computed:{
          sumPrice(){
            var itemIndex,sunPrice=0;
            for (var i=0;i<this.checkIndexList.length;i++) {
              itemIndex = this.checkIndexList[i];
              sunPrice+=this.dataList[itemIndex].shopCar.chooseNumber*this.dataList[itemIndex].commodityPrice;
              }
            return sunPrice;
          },
          flag_show(){
            return this.dataList.length>0;
          },
        },
        watch:{
          checkAllFlag:function (val) {
            if (val){
              this.checkIndexList =[];
              for (var i = 0; i < this.dataList.length; i++) {
                this.checkIndexList.push(i);
              }
            } else {
              this.checkIndexList =[];
            }
          }
        },
        methods:{
          less(item){
            if(item.shopCar.chooseNumber>1){item.shopCar.chooseNumber--}
          },
          input(item){
            item.shopCar.chooseNumber=item.shopCar.chooseNumber.toString().replace(/[^\d]/g,'');

            if (item.shopCar.chooseNumber=='' || item.shopCar.chooseNumber<1){
              item.shopCar.chooseNumber=1;
            }
          },
          //  直接对 某一行 删除/移入收藏夹   需要对dataList和checkIndexList更新
          updList_specific(index){
            this.dataList.splice(index,1);
            for (var i=0;i<this.checkIndexList.length;i++){
              if (this.checkIndexList[i]>index){
                this.checkIndexList[i]--;
              } else if (this.checkIndexList[i] === index){
                this.checkIndexList.splice(i,1);
                i--;
              }
            }
          },
          //通过勾选 多行 删除/移入收藏夹   需要对dataList和checkIndexList更新
          updList_multi(){
            //先降序 这样从下往上删除 不影响前面元素
            this.checkIndexList.sort(function (a, b) {
              return b-a;
            });
            for (var i=0;i<this.checkIndexList.length;i++)
              this.dataList.splice(this.checkIndexList[i],1);
            this.checkIndexList = [];
          },
          delByShopCarId(item,index){
              this.$axios.post('/car/delShopCarByUserId',[item.shopCar.shopCarId])
                .then(res=>{
                this.updList_specific(index);
                  this.setShopCarNumber();
              })
          },
          delByShopCarIdList(){
            var delShopCarIdList =[];
            for (var i=0;i<this.checkIndexList.length;i++)
              delShopCarIdList.push(this.dataList[this.checkIndexList[i]].shopCar.shopCarId)
            if (delShopCarIdList.length ==0)
              return;
            this.$axios.post('/car/delShopCarByUserId',delShopCarIdList)
              .then(res=>{
                this.updList_multi();
                this.setShopCarNumber();
              })
          },
          shopCarToFavorite(item,index){
            this.$axios.post('/car/shopCarToFavoriteByUserId',{
                shopCarIdList:[item.shopCar.shopCarId],
                commodityIdList:[item.shopCar.commodityId]
            }).then(res=>{
                this.updList_specific(index);
              this.setShopCarNumber();
              })
          },
          shopCarListToFavorite(){
            var shopCarIdList =[], commodityIdList =[];
            for (var i=0;i<this.checkIndexList.length;i++){
              shopCarIdList.push(this.dataList[this.checkIndexList[i]].shopCar.shopCarId);
              commodityIdList.push(this.dataList[this.checkIndexList[i]].shopCar.commodityId);
            }

            if (shopCarIdList.length ==0)
              return;
            this.$axios.post('/car/shopCarToFavoriteByUserId',{
                shopCarIdList:shopCarIdList,
                commodityIdList:commodityIdList
            }).then(res=>{
              this.updList_multi();
                this.setShopCarNumber();
              })
          },
          setShopCarNumber(){
            var shopCarNumber = 0;
            for (let i in this.dataList){
              shopCarNumber += this.dataList[i].shopCar.chooseNumber
            }
            this.$store.state.shopCarNumber = shopCarNumber;
          },
          shop(){
            var shopCarList =[];
            for (var i=0;i<this.checkIndexList.length;i++)
              shopCarList.push(this.dataList[this.checkIndexList[i]].shopCar)
            if (shopCarList.length ==0){
              return;
            }
            this.$axios.post('/car/ ShopCarSubmitOrderByUserId',shopCarList)
              .then(res=>{
                if (this.$store.getters.getResultDispose(res)){
                  this.$router.push({name:'shop_success'});
                }
              });
          },
          gotoCommodityPage(commodityId){
            const {href} = this.$router.resolve({
              name:'commodityPage',
              query:{commodityId:commodityId}
            });
            window.open(href,'_blank');
          },
        },
        created() {
          this.$axios.post('/car/selShopCarCommodityVOByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){
                this.dataList=res.data.data;
                this.checkAllFlag = true;
              }
            });
        }

    }
</script>

<style scoped>

  .container{
    max-width: 1280px;
  }


  *{
    padding: 0px;
  }
  .notData{
    margin-top: 55px;
    background: #FFF8F6;
    border: 1px solid #F7EAE7;
    height: 90px;
    padding-left: 250px;
  }
  .notData img{
    height: 60px;
    margin: 15px;
  }
  .maddle{
    color: #790103;
    font-weight: bold;
  }

  #tiangou{
    margin-left: 70px;
    height: 35px;
    position: relative;
    bottom: 5px;
  }
  #shop_car{
    font-weight: 600;
    font-size: 28px;
  }


  .title{
    position: fixed;
    display: inline-block;
    z-index: 150;
    width: 100%;
    height: 25px;
    background: white;
    margin-top: 25px;
  }

  .itemBox{
    height: 130px;
    background: #FCFCFC;
    border: 1px solid #CCCCCC;
    padding: 10px 0px;
  }
  .itemBox img{
    height: 110px;
    max-width: 115px;
  }

  . center{
    text-align: center;
  }
  .cNameBox{
    margin-left: 20px;
    margin-top: 15px;
    cursor: pointer;
  }
  .cNameBox:hover{
    text-decoration:underline;
    color: #FF6600;
  }
  .itemBox input{
    width: 50px;
    padding-left:8px;
    margin: 0px;
  }
  .btn{
    position: relative;
    bottom: 2px;
    padding-left: 10px;
    padding-right: 10px;
    width: 28px;
    height: 28px;
  }

  .btn span{
    color: #FCFCFC;
    font-size: 30px;
    line-height: 20px;
    position: absolute;
    top: 40%;
    left: 50%;
    transform: translate(-50%,-50%);
  }
  .down{
    margin-top: 30px;
  }


  

  .sumPriceBox{
    min-width: 160px;
    float: right;
    margin-right: 10px;
  }
  #heji{
    color: #4F4F4F;
    position: relative;
    top: 10px;
    font-size: 15px;
  }
  #sumPrice{
    color: #FE5525;
    font-size: 28px;
    font-weight: bold;
    float: right;
  }
  .shop{
    float: right;
    display: inline-block;
    background: #FF0036;
    width: 150px;
    height: 50px;
    text-align: center;
    border-radius:5px;
    cursor: pointer;
  }
  .shop span{
    color: white;
    font-size: 24px;
    position: absolute;
    top: 45%;
    transform: translate(-50%,-50%);
  }

  .bottomMessage{
    height: 50px;
    /*border: 1px solid red;*/
    border-radius:5px;
    padding-left: 30px;
    background: #E5E5E5;
    z-index: 150;
    position: fixed;
    left: 50%;
    transform: translate(-50%);
    bottom: 50px;
    margin: 0 auto;
    width: 1330px;
  }
  .bottom_bj{
    background: black;
    position: fixed;
    left: 50%;
    transform: translate(-50%);
    bottom: 0px;
    width: 1800px;
    height:100px;

    background-image: url("../assets/shop_car_bj.jpg");
    background-repeat:repeat;
  }
  .bottom_bj img{
    height: 60px;
  }
  .placeholder{
    height: 120px;
  }


  input[type="checkbox"] {
    zoom: 155%;
    position: relative;
    top: 28px;
  }

  .operationBox{
    display: inline-block;
    padding-top: 8px;
  }
  .operationSpan{
    cursor: pointer;
    margin-right: 36px;
  }
  .operationSpan:hover{
    color: #FF4400;
    text-decoration:underline;
  }
</style>
