<template>
  <div>
    <nav_top :flag_fixed="true"/>


    <div style="position: fixed;width: 100%;height: 75px;padding-top: 35px; background: white;z-index: 250">
      <img id="tiangou" src="../assets/tmall.jpg">
      <span id="shop_car">购物车</span>
    </div>
    <div style="height: 50px;"></div>



    <div v-if="flag_show">

        <div class="title">

          <el-row>
            <el-col :span="7" offset="2"><div><span>商品信息</span></div></el-col>
            <el-col :span="3"><div  style="padding-left: 40px"><span>单价</span></div></el-col>
            <el-col :span="4"><div style="margin-left: 16px"><span>数量</span></div></el-col>
            <el-col :span="3"><span>金额</span></el-col>
            <el-col :span="3"><div style="margin-left: 28px"><span>操作</span></div></el-col>
          </el-row>



        </div>

      <div style="height: 55px;"></div>

        <el-row>
          <div class="itemBox row" v-for="item in dataList">
            <div class="checkbox">
               <label @click="click_selected(item)" class="myCkeck" :class="{myCkeck_selecked:item.shopCar.selected}">
                 <span class="iconfont">&#xed1d;</span>
               </label>
            </div>

            <el-col :span="2">
              <img :src="item.commodityPhoto" @click="gotoCommodityPage(item.shopCar.commodityId)" style="cursor: pointer;">
            </el-col>
            <el-col :span="7">
              <div class=" cNameBox"  @click="gotoCommodityPage(item.shopCar.commodityId)">
                <span>{{item.commodityName}}</span>
                <div style=""><span>{{item.commodityDescribe}}</span></div>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="commodityPrice  center down" style="padding-left: 40px">
                <span>￥{{item.commodityPrice.toFixed(2)}}</span>
              </div>
            </el-col>

            <el-col :span="4">
              <div class="center down" >
                <button type="button" class="btn btn-info" @click="click_changePurchaseQuantity(item,-1)"><span>-</span></button>
                <input v-model.number="item.shopCar.purchaseQuantity" @input="input(item)">
                <button type="button" class="btn btn-info" @click="click_changePurchaseQuantity(item,1)"><span>+</span></button>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="price col-1 center down">
                <span>￥{{(item.commodityPrice*item.shopCar.purchaseQuantity).toFixed(2)}}</span>
              </div>
            </el-col>
            <el-col :span="3">
              <div class="price col-1 center " style="margin-top: 25px;margin-left: 15px">
                <div style="padding-left: 20px;width: 200px"><span class="operationSpan" @click="delByShopCarId(item)" >删除</span></div>
                <div style="width: 200px"><span class="operationSpan" @click="shopCarToFavorite(item)" >移入收藏夹</span></div>
              </div>
            </el-col>
          </div>

        </el-row>


        <!--        不要删除  调列表 到底部距离-->
        <div class="placeholder"></div>


      <div class="bottom_bj">
      </div>
      <div class="bottomMessage">
        <div class="operationBox">
          <div style="padding-top: 8px;display: inline-block">
            <label @click="click_all_selecked" class="myCkeck" :class="{myCkeck_selecked:flag_all_selecked}">
              <span class="iconfont" >&#xed1d;</span>
<!--              用于对齐 注释看看-->
              <span style="display: inline-block;"  v-if="!flag_all_selecked"></span>
            </label>
          </div>
          <span class="operationSpan" @click="click_all_selecked">全选</span>
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
        <span class="middle">你的购物车还没有商品哟，还不给我去购物</span>
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

            flag_notSubmitOrder:true,
          }
        },
        computed:{
          selectedList(){
            return this.dataList.filter(item=>item.shopCar.selected);
          },
          flag_all_selecked(){
            return  this.dataList.length == this.selectedList.length;
          },
          sumPrice(){
            var sunPrice=0;
            this.selectedList.forEach((item)=>{
              sunPrice += item.shopCar.purchaseQuantity * item.commodityPrice;
            });
            return sunPrice;
          },
          flag_show(){
            return this.dataList.length>0;
          },
        },
        watch:{
          dataList(){
            this.$store.state.user.shopCarNumber = this.dataList.length;
          },
        },
        methods:{
          warning(message){
            this.$message.warning({
              message: message,
            });
            this.$notify.warning({
              title: message,
            });
          },
          click_selected(item){
            var target = !item.shopCar.selected;
            item.shopCar.selected =target;

            let tempThis = this;
            clearTimeout(item.timeout_selected);
            item.timeout_selected = setTimeout(function () {
              tempThis.$axios.get('/car/updSelectedByUserId',{
                params:{
                  commodityId : item.shopCar.commodityId,
                  selected : target
                }
              }).then(res=>{
                tempThis.$store.getters.getResultDispose(res)
              });
            }, 300);

          },
          click_all_selecked(){
            var target_flag_all_selecked = !this.flag_all_selecked;
            this.dataList.forEach((item)=>{
              item.shopCar.selected = target_flag_all_selecked;
            });

            let tempThis = this;
            clearTimeout(tempThis.timeout_all_selecked);
            tempThis.timeout_all_selecked = setTimeout(function () {
              tempThis.$axios.get('/car/updSelectedByUserId',{
                params:{
                  selected : target_flag_all_selecked
                }
              }).then(res=>{
                tempThis.$store.getters.getResultDispose(res)
              });
            },300);

          },
          changePurchaseQuantity(item){

            let tempThis = this;
            clearTimeout(item.timeout_purchaseQuantity);
            item.timeout_purchaseQuantity = setTimeout(function () {
              tempThis.$axios.get('/car/updChangePurchaseQuantityByUserId',{
                params:{
                  commodityId : item.shopCar.commodityId,
                  purchaseQuantity : item.shopCar.purchaseQuantity
                }
              }).then(res=>{
                tempThis.$store.getters.getResultDispose(res)
              });
            }, 300);

          },
          click_changePurchaseQuantity(item, change){
            var target = item.shopCar.purchaseQuantity + change;
            item.shopCar.purchaseQuantity = target > 1 ? target : 1;
            this.changePurchaseQuantity(item);
          },
          input(item){
            item.shopCar.purchaseQuantity=item.shopCar.purchaseQuantity.toString().replace(/[^\d]/g,'');
            if (item.shopCar.purchaseQuantity=='' || item.shopCar.purchaseQuantity<1)
              item.shopCar.purchaseQuantity=1;
            this.changePurchaseQuantity(item);
          },
          //  直接对 某一行 删除/移入收藏夹   需要对dataList更新
          updList_specific(item){
            var index = this.dataList.indexOf(item);
            this.dataList.splice(index,1);
          },
          //通过勾选 多行 删除/移入收藏夹   需要对dataList更新
          updList_multi(){
            var targetList = this.selectedList;
            targetList.forEach((item)=>{
              var index = this.dataList.indexOf(item);
              this.dataList.splice(index,1);
            });
          },
          delByShopCarId(item){
              this.$axios.post('/car/delShopCarByUserId',[item.shopCar.commodityId])
                .then(res=>{
                  if (this.$store.getters.getResultDispose(res))
                this.updList_specific(item);
              })
          },
          delByShopCarIdList(){
            var delShopCarIdList =[];
            for (var i=0;i<this.selectedList.length;i++)
              delShopCarIdList.push(this.selectedList[i].shopCar.commodityId)
            if (delShopCarIdList.length ==0)
              return;
            this.$axios.post('/car/delShopCarByUserId',delShopCarIdList)
              .then(res=>{
                if (this.$store.getters.getResultDispose(res))
                this.updList_multi();
              })
          },
          shopCarToFavorite(item){
            this.$axios.post('/car/shopCarToFavoriteByUserId',[item.shopCar.commodityId])
              .then(res=>{
              if (this.$store.getters.getResultDispose(res))
                this.updList_specific(item);
              })
          },
          shopCarListToFavorite(){
            var  commodityIdList =[];
            this.selectedList.forEach((item)=>{
              commodityIdList.push(item.shopCar.commodityId);
            });

            if (commodityIdList.length ==0)
              return;
            this.$axios.post('/car/shopCarToFavoriteByUserId',commodityIdList)
              .then(res=>{
                  this.updList_multi();
              })
          },
          shop(){
            if (this.flag_notSubmitOrder){
              this.flag_notSubmitOrder = false;
            } else {
              this.warning('订单提交中请稍后');
              return;
            }

            var shopCarList =[];
            this.selectedList.forEach((item)=>{
              shopCarList.push(item.shopCar)
            });

            if (shopCarList.length ==0){
              return;
            }
            this.$axios.post('/car/ShopCarSubmitOrderByUserId',shopCarList)
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
          this.$axios.post('/car/listShopCarCommodityVOByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){
                this.dataList=res.data;
              }
            });
        }

    }
</script>

<style scoped>


  .el-row{
    max-width: 1280px;
    margin: 0px auto;
    padding: 0px;
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
  .middle{
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
    margin-left: 90px;
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



  .checkbox{
    width: 77.5px;
    position: relative;
  }
  .checkbox .myCkeck{
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%,-50%);
    }
   .myCkeck{
     width: 20px;
     height: 20px;
     cursor: pointer;
     border: 1px solid #d3d3d3;
     line-height: 20px;
   }
   .myCkeck span{
     position:relative;
     bottom:1px;
     right:1px;
     color: white;
     font-size: 21px;
     font-weight: 500;
     display: none;
   }

  .myCkeck_selecked{
    background: #ff6700;
    border: 1px solid #ff6700!important;
  }
  .myCkeck_selecked span{
    display: inline-block!important;

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
