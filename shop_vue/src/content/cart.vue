<template>
  <div>
    <nav_top :flag_fixed="true"/>


    <div class="cart_top_box">
      <el-row>
        <div class="cart_top">
          <img id="tiangou" src="../assets/tmall.jpg">
          <span id="cart">购物车</span>
        </div>
      </el-row>
    </div>
    <div style="height: 45px;"></div>



    <div v-if="flag_show">

        <div class="title">

          <el-row>
            <el-col :span="8" offset="4"><div style="padding-left: 20px;box-sizing: border-box"><span>商品信息</span></div></el-col>
            <el-col :span="3"><div style="padding-left: 15px;box-sizing: border-box"><span>单价</span></div></el-col>
            <el-col :span="3"><div style="margin-left: 15px;box-sizing: border-box"><span>数量</span></div></el-col>
            <el-col :span="3"><div  style="margin-left: 20px;box-sizing: border-box"><span>金额</span></div></el-col>
            <el-col :span="3"><div style="margin-left: 35px;box-sizing: border-box"><span>操作</span></div></el-col>
          </el-row>



        </div>

      <div style="height: 40px;"></div>

        <el-row>
          <div class="itemBox row" v-for="item in dataList">
            <el-col :span="2">
              <div class="checkbox">
                <label @click="click_selected(item)" class="myCkeck" :class="{myCkeck_selecked:item.selected}">
                  <span class="iconfont">&#xed1d;</span>
                </label>
              </div>
            </el-col>


            <el-col :span="2">
              <img :src="item.commodityPhoto" @click="gotoCommodityPage(item.commodityId)" style="cursor: pointer;">
            </el-col>
            <el-col :span="8">
              <div class=" cNameBox"  @click="gotoCommodityPage(item.commodityId)">
                <span>{{item.commodityName}}</span>
                <div><span style="color: #7E7E7E">{{item.commodityDescribe}}</span></div>
                <div style="position: absolute;bottom: 0px;color: red;font-size: 17.5px">{{hint_commodity(item)}}</div>
              </div>
            </el-col>

            <el-col :span="3">
              <div class=" down">
                <div v-if="undefined ==item.cartCommodityVO || item.commodityOldPrice != item.cartCommodityVO.commodityPrice">
                  <span style="text-decoration:line-through;color: #9E9E9E">
                    ￥{{item.commodityOldPrice.toFixed(2)}}
                  </span>
                </div>
                <div><span>{{getCommodityPrice(item)}}</span></div>
              </div>
            </el-col>

            <el-col :span="3">
              <div class=" down" style="width: 150%;margin-left: -25px">
                <div class="quantity_btn_box"><div class="quantity_btn" @click="click_changePurchaseQuantity($event,item,-1)"  @mouseout="quantity_btn_leave($event)"><span style="padding-left: 4px;">-</span></div></div>
                <input v-model.number="item.purchaseQuantity" @input="input(item)" class="quantity_input">
                <div class="quantity_btn_box"><div class="quantity_btn" @click="click_changePurchaseQuantity($event,item,1)"  @mouseout="quantity_btn_leave($event)"><span>+</span></div></div>
              </div>
              <div style="color: red">{{hint_commodityStock(item)}}</div>
            </el-col>
            <el-col :span="3">
              <div class="price col-1  down">
                <span>{{getSumCommodityPrice(item)}}</span>
              </div>
            </el-col>
            <el-col :span="2">
              <div class="price col-1  " style="margin-top: 25px;margin-left: 15px">
                <div style="padding-left: 20px;width: 200px"><span class="operationSpan" @click="delByCartId(item)" >删除</span></div>
                <div style="width: 200px"><span class="operationSpan" @click="cartToFavorite(item)" >移入收藏夹</span></div>
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
          <span class="operationSpan" @click="delByCartIdList" >删除</span>
          <span class="operationSpan" @click="cartListToFavorite" >移入收藏夹</span>
        </div>

        <div class="shop" @click="shop"><span>结 算</span></div>
        <div class="sumPriceBox">
          <span id="heji">合计 : </span>
          <span id="sumPrice">{{(this.sumPrice).toFixed(2)}}</span>
        </div>
      </div>


    </div>

    <div v-else style="margin-top: 40px">
      <not-data left="旺~旺~旺~" middle="你的购物车还没有商品哟，快去添加吧"/>
    </div>

  </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    import Hint_popup from "@/components/hint_popup";
    import NotData from "@/components/notData";
    export default {
        name: "cart",
      components: {NotData, Hint_popup, Nav_top},
      data(){
          return{
            dataList:[],
            flag_show:true,
            flag_notSubmitOrder:true,
          }
        },
        computed:{
          selectedList(){
            return this.dataList.filter(item=>item.selected);
          },
          flag_all_selecked(){
            return  this.dataList.length == this.selectedList.length;
          },
          sumPrice(){
            var sunPrice=0;
            this.selectedList.forEach((item)=>{
              if (undefined != item.cartCommodityVO)
                sunPrice += item.purchaseQuantity * item.cartCommodityVO.commodityPrice;
            });
            return sunPrice;
          }
        },
        watch:{
          dataList(){
            this.$store.state.user.cartNumber = this.dataList.length;
          },
        },
        methods:{
          getCommodityPrice(item){
            if (undefined != item.cartCommodityVO)
              return '￥' + item.cartCommodityVO.commodityPrice.toFixed(2);
          },
          getSumCommodityPrice(item){
            if (undefined != item.cartCommodityVO)
              return '￥' + (item.cartCommodityVO.commodityPrice * item.purchaseQuantity).toFixed(2);
          },
          hint_commodity(item){
            if (undefined == item.cartCommodityVO)
              return '该商品已永久下架';
            if (!item.cartCommodityVO.commodityOnShelves)
              return '该商品已下架';
            return undefined;
          },
          hint_commodityStock(item){
            var hint_commodity = this.hint_commodity(item);
            if (undefined != hint_commodity)
              return hint_commodity;
            if (0 == item.cartCommodityVO.commodityStock)
              return '商品暂时无货'
            if (item.purchaseQuantity >= item.cartCommodityVO.commodityStock)
              return '商品库存为' + item.cartCommodityVO.commodityStock + '件';
          },
          warning(message){
            this.$message.warning({
              message: message,
            });
            this.$notify.warning({
              title: message,
            });
          },
          click_selected(item){
            var target = !item.selected;
            item.selected =target;

            let tempThis = this;
            clearTimeout(item.timeout_selected);
            item.timeout_selected = setTimeout(function () {
              tempThis.$axios.get('/cart/updSelectedByUserId',{
                params:{
                  commodityId : item.commodityId,
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
              item.selected = target_flag_all_selecked;
            });

            let tempThis = this;
            clearTimeout(tempThis.timeout_all_selecked);
            tempThis.timeout_all_selecked = setTimeout(function () {
              tempThis.$axios.get('/cart/updAllSelectedByUserId',{
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
              tempThis.$axios.get('/cart/updChangePurchaseQuantityByUserId',{
                params:{
                  commodityId : item.commodityId,
                  purchaseQuantity : item.purchaseQuantity
                }
              }).then(res=>{
                tempThis.$store.getters.getResultDispose(res)
              });
            }, 300);

          },
          quantity_btn_leave($event){
            $event.currentTarget.parentNode.classList.remove("quantity_btn_box_active");
          },
          click_changePurchaseQuantity($event,item, change){
            $event.currentTarget.parentNode.classList.add("quantity_btn_box_active");
            if (undefined == item.cartCommodityVO){
              item.purchaseQuantity = 0;
              return;
            }
            var target = item.purchaseQuantity + change;
            target = target > 1 ? target : 1;
            target = target > item.cartCommodityVO.commodityStock ? item.cartCommodityVO.commodityStock : target;
            item.purchaseQuantity = target;
            this.changePurchaseQuantity(item);
          },
          input(item){
            if (undefined == item.cartCommodityVO){
              item.purchaseQuantity = 0;
              return;
            }
            var target = parseInt(item.purchaseQuantity.toString().replace(/[^\d]/g,''));
            if (isNaN(target) || target<1)
                target = 1;
            target = target > item.cartCommodityVO.commodityStock ? item.cartCommodityVO.commodityStock : target;
            item.purchaseQuantity = target;
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
          delByCartId(item){
              this.$axios.post('/cart/delCartByUserId',[item.commodityId])
                .then(res=>{
                  if (this.$store.getters.getResultDispose(res))
                this.updList_specific(item);
              })
          },
          delByCartIdList(){
            var delCartIdList =[];
            for (var i=0;i<this.selectedList.length;i++)
              delCartIdList.push(this.selectedList[i].commodityId)
            if (delCartIdList.length ==0)
              return;
            this.$axios.post('/cart/delCartByUserId',delCartIdList)
              .then(res=>{
                if (this.$store.getters.getResultDispose(res))
                this.updList_multi();
              })
          },
          cartToFavorite(item){
            this.$axios.post('/cart/cartToFavoriteByUserId',[item.commodityId])
              .then(res=>{
              if (this.$store.getters.getResultDispose(res))
                this.updList_specific(item);
              })
          },
          cartListToFavorite(){
            var  commodityIdList =[];
            this.selectedList.forEach((item)=>{
              commodityIdList.push(item.commodityId);
            });

            if (commodityIdList.length ==0)
              return;
            this.$axios.post('/cart/cartToFavoriteByUserId',commodityIdList)
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

            var cartList =[];
            this.selectedList.forEach((item)=>{
              cartList.push({commodityId:item.commodityId, purchaseQuantity:item.purchaseQuantity});
            });
            console.log(cartList  )
            if (cartList.length ==0){
              return;
            }
            this.$axios.post('/cart/CartSubmitOrderByUserId',cartList)
              .then(res=>{

                if (this.$store.getters.getResultDispose(res)){
                  this.$axios.post('/order/payOrderByUserId',res.data.data)
                    .then(res=>{
                      if (this.$store.getters.getResultDispose(res)){
                        this.$router.push({name:'shop_success'});
                      }
                    });
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
          this.$axios.post('/cart/listCartCommodityVOByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){
                this.dataList=res.data;
                this.flag_show = this.dataList.length>0;
              }
            });
        }

    }
</script>

<style scoped>


  .el-row{
    max-width: 1300px;
    margin: 0px auto;
    padding: 0px;
  }


  *{
    padding: 0px;
  }

  .cart_top_box .el-row{
    max-width: 1350px;
  }

  .cart_top_box{
    box-sizing: border-box;
    padding-top: 10px;
    position: fixed;
    width: 100%;
    height: 50px;
    background: white;
    z-index: 250;
  }

  .cart_top{
    position: relative;
    height: 35px;
  }

  #tiangou{
    height: 100%;
  }
  #cart{
    font-weight: 600;
    font-size: 28px;
    line-height: 38px;
    position: absolute;
    bottom: 0px;
  }


  .title{
    position: fixed;
    display: inline-block;
    z-index: 150;
    width: 100%;
    height: 25px;
    background: white;
    margin-top: 5px;
  }

  .itemBox{
    height: 130px;
    background: #FCFCFC;
    border: 1px solid #CCCCCC;
    padding: 10px 0px;
    box-sizing: border-box;
  }
  .itemBox img{
    height: 110px;
    max-width: 115px;
  }


  .cNameBox{
    position: relative;
    max-width: 363px;
    height: 98px;
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
    box-sizing: border-box;
    padding-left:8px;
    margin: 0px;
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
    box-sizing: border-box;
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

    background-image: url("../assets/cart_bj.jpg");
    background-repeat:repeat;
  }
  .bottom_bj img{
    height: 60px;
  }
  .placeholder{
    height: 120px;
  }



  .checkbox{
    height: 100px;
    /*width: 100%;*/
    height: 100%;
    position: relative;
    margin-top: 35%;
    margin-left: 40%;
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
    box-sizing: border-box;
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




  .quantity_btn_box{
    cursor: pointer;
    display: inline-block;
    width: 28px;
    height: 28px;
    border-radius: .5rem;
    border: 3px solid rgba(155,214,224,0);
  }
  .quantity_btn_box_active{
    border: 3px solid rgba(155,214,224,.5);
    background: #9BD6E0;
  }
  .quantity_btn_box:hover .quantity_btn{
    background: #138496;
  }
  .quantity_btn{
    width: 28px;
    height: 28px;
    border-radius: .25rem;

    background: #17A2B8;
    box-sizing: border-box;
    padding-left: 4px;
  }
  .quantity_btn_box span{
    line-height: 25px;
    font-size: 27px;
    font-weight: 400;
    color: white;

    -moz-user-select:none;/*火狐*/
    -webkit-user-select:none;/*webkit浏览器*/
    -ms-user-select:none;/*IE10*/
    -khtml-user-select:none;/*早期浏览器*/
    user-select:none;
  }


  .quantity_input{
    width: 50px;
    height: 28px;
    position: relative;
    bottom: 5px;
  }
</style>
