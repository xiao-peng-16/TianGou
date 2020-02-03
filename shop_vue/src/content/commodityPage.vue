<template>
  <div>
    <nav_top :flag_fixed="true" :maxWidth="rowWidth"/>
    <br/>



      <el-row style="margin-top: 50px;height: 460px">
        <el-col :span="12" >
          <div v-if="undefined != this.commodity.commodityVideo" class="videoBox"><video_component :resource="resource"/></div>
          <div v-else style="position: relative;height: 200px;width: 100%" >
            <img_amplifier :img-src="resource.poster" style="position: absolute;left: 50%;transform: translate(-50%)"/>
          </div>

        </el-col>

        <el-col span="10" :offset="2">
            <div class="messageBox" :class="{flag_img_messageBox:undefined == this.commodity.commodityVideo}">
              <div class="topBox">
                <span class="cName">{{commodity.commodityName}}</span><br/>
                <div class="beizhu"><span>{{commodity.commodityDescribe}}</span></div>
              </div>

              <div class="priceBox">
                <span class="price_left">价格</span>
                <span class="price_right">{{parseInt(commodity.commodityPrice).toFixed(2)}}</span>
              </div>

              <div class="split"></div>
              <div class="middleBox">
                <div>
                  <span class="middleBox_left">销量</span>
                  <span class="middleBox_right">{{commodity.commoditySales}} </span>
                </div>
                <span class="middleBox_solit">|</span>
                <div>
                  <span class="middleBox_left">人气</span>
                  <span class="middleBox_right">{{commodity.commodityPopularity}} </span>
                </div>
                <span class="middleBox_solit">|</span>
                <div>
                  <span class="middleBox_left">送天狗积分</span>
                </div>
              </div>
              <div class="split"></div>

              <div class="purchaseQuantityBox">
                <span class="shu_liang">数量</span>
                <div class="numBox">
                  <span @click="purchaseQuantity--" class="arrows_left iconfont">&#xe610;</span>
                  <input v-model.number="purchaseQuantity" type="text">
                  <span @click="purchaseQuantity++" class="arrows_right iconfont">&#xf034f;</span>
                </div>
                <span class="cStock">库存{{commodity.commodityStock}}件 </span>
              </div>

              <div class="buttonBox">
                <div class="buttonBox_left" @click="shop"><span>立刻购买</span></div>
                <div class="buttonBox_right" @click="addShop_Car" :class="{buttonBox_right_notAdd:this.flag_notAddShop_car,buttonBox_right_Add:!this.flag_notAddShop_car}">
                  <span class="iconfont">&#xe63a;</span>
                  <span>{{this.msg_addShopcar}}</span>
                </div>
              </div>
            </div>
        </el-col>



      </el-row>



  </div>


</template>


<script>

  import Nav_top from "@/components/nav_top";
  import Hint_popup from "@/components/hint_popup";
  export default {
    name: "commodityPage",
    components: {Hint_popup, Nav_top,
      Img_amplifier:resolve => {
        require(['../components/Img_amplifier'],resolve)
      },
      Video_component:resolve => {
        require(['../components/Video_component'],resolve)
      },

    },
    data(){
      return{
        rowWidth:undefined,

        purchaseQuantity:1,
        msg_addShopcar:'加入购物车',
        flag_notAddShop_car:true,
        flag_notSubmitOrder:true,
        commodity:{},
        resource:{
          poster:"",
          src:""
        },

      }
    },
    mounted() {
      this.rowWidth = document.querySelector('.row').offsetWidth;
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
      addShop_Car(){

        if (!this.commodity.commodityOnShelves){
          this.warning('该商品已下架');
          return;
        }

        if(this.flag_notAddShop_car){
          this.$axios.get('/cart/addCartByUserId',{
            params:{
              commodityId:this.commodity.commodityId,
              purchaseQuantity:this.purchaseQuantity
            }
          }).then(res=>{
            if (this.$store.getters.getResultDispose(res)){
              this.flag_notAddShop_car=false;
              this.msg_addShopcar="已添加至购物车";
              this.$axios.get('/cart/countCartByUserId')
                .then(res=>{
                  this.$store.state.user.cartNumber=res.data;
                });
              this.$message({
                message: '购物车添加成功',
                type: 'success'
              });
              this.$notify.success({
                title:'购物车添加成功',
                message: '商品成功添加进购物车',
              });
            }
          })
        }

      },
      shop(){

        if (!this.commodity.commodityOnShelves){
          this.warning('该商品已下架');
          return;
        }

        if (this.flag_notSubmitOrder){
          this.flag_notSubmitOrder = false;
        } else {
          this.warning('订单提交中请稍后');
          return;
        }

        this.$axios.post('/order/submitSingleOrderByUserId',{
          commodityId:this.commodity.commodityId,
          purchaseQuantity:this.purchaseQuantity
        }).then(res=>{
          if (this.$store.getters.getResultDispose(res)){
            this.$router.push({name:'shop_success'});
          }
        })
      },

    },
    created() {
      this.$axios.get('/commodity/getCommodityPageByCommodityId',{
        params:{
          commodityId: this.$route.query.commodityId
        }
      }).then(res=>{
        this.commodity = res.data;
        if (this.commodity){
          this.resource.poster = this.commodity.commodityPhoto;
          this.resource.src = this.commodity.commodityVideo;
        } else {
          this.$store.state.status = this.GLOBAL.ResultStatus.COMMODITY_NOT_FOUND;
        }
      })
    },
    watch:{
      purchaseQuantity:function (val) {
        this.purchaseQuantity=this.purchaseQuantity.toString().replace(/[^\d]/g,'');

        if (this.purchaseQuantity=='' || this.purchaseQuantity<1){
          this.purchaseQuantity=1;
        }

      }
    }
  }
</script>

<style scoped>

  .el-row{
    max-width: 1200px;
    margin: 0px auto;
    padding: 0px;
  }

  *{
    padding: 0px;
  }

  .middleBox_left,.cStock,.middleBox_solit,.shu_liang{
    color: #999;
  }
  .videoBox{
    width: 650px;
    margin-left: 20px;
    margin-top: 15px;
  }

  .messageBox{
    z-index: 0;
    width: 440px;
    height: 400px;
    margin-right: 50px;
  }
  .flag_img_messageBox{
    margin-top: 30px;
  }

  .topBox{
    margin: 10px;
  }
  .cName{
    color: black;
    font-size: 26px;
    font-weight: bold;
  }
  .beizhu{
    margin-left: 10px;
    width: 100%;
    height: 25px;
    color: #FF3761;
  }
  .price_left{
    position: relative;
    top: 3px;
    left: 15px;
    font-size: 18px;
    color: #999;
  }
  .price_right{
    position: relative;
    top: 10px;
    left: 50px;
    color: #FF0036;
    font-size: 35px;
    font-weight: bold;
  }
  .priceBox{
    margin-top: 25px;
    margin-bottom: 10px;
    background-image: url("../assets/price_bj.png");
    width: 100%;
    height: 70px;
  }

  .middleBox div{
    display: inline-block;
    margin: 0px 30px;
  }
  .middleBox_right{
    color: #FF0070;
  }
  .split{
    margin: 7px auto;
    height: 0.8px;
    background:  #999;
  }
  .purchaseQuantityBox{
    margin-top: 20px;
  }
  .purchaseQuantityBox >span{
    margin-left: 10px;
    position: relative;
    bottom: 1px;
  }
  .numBox{
    display: inline-block;
  }
  input[type=text]{
    width: 50px;
    padding-left: 15px;
    font-size: 18px;
    line-height: 18px;
  }
  .arrows_left{
    cursor: pointer;
    font-size: 25px;
    position: relative;
    top:3px;
    left: 8px;
  }
  .arrows_right{
    cursor: pointer;
    font-size: 25px;
    position: relative;
    top:3px;
    right: 8px;
  }

  .buttonBox{
    margin-top: 30px;
    margin-left: 18px;
  }

  .buttonBox div{
    display: inline-block;
    text-align: center;

  }
  .buttonBox div span{
    position: relative;
    top: 7.5px;
  }
  .buttonBox_left{
    cursor: pointer;
    background: #FFEDED;
    color: #FF0036;
    border: 1px solid #FF0036;
    width: 180px;
    height: 40px;
    margin-right: 15px;
  }
  .buttonBox_right{
    background: #FF0036;
    color: white;
    width: 182px;
    height: 42px;
    position: relative;
    bottom: 2px;
  }
  .buttonBox_right_notAdd{
    cursor: pointer;
  }
  .buttonBox_right_Add{
    color: #713936;
  }
</style>
