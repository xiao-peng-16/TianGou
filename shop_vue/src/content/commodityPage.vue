<template>
  <div>
    <nav_top :flag_fixed="true" :maxWidth="rowWidth"/>
    <br/>



    <div class="container">
<!--      <div class="row" style="height: 100px">-->

<!--      </div>-->
      
      <div class="row" style="margin-top: 50px;height: 460px">
        <div class="col-6">
          <div v-if="undefined != this.commodity.commodityVideo" class="videoBox"><video_component :resource="resource"/></div>
          <div v-else style="position: relative">
            <img_amplifier :img-src="resource.poster" style="position: absolute;left: 50%;transform: translate(-50%)"/>
          </div>

        </div>

        <div class=" offset-1 col-5" style="position: relative">
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

            <div class="chooseNumberBox">
              <span class="shu_liang">数量</span>
              <div class="numBox">
                <span @click="chooseNumber--" class="arrows_left iconfont">&#xe610;</span>
                <input v-model.number="chooseNumber" type="text">
                <span @click="chooseNumber++" class="arrows_right iconfont">&#xf034f;</span>
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
        </div>
      </div>


    </div>





  </div>


</template>


<script>

  import Video_component from "@/components/video_component";
  import Nav_top from "@/components/nav_top";
  import Hint_popup from "@/components/hint_popup";
  import Img_amplifier from "@/components/img_amplifier";
    export default {
        name: "commodityPage",
      components: {Img_amplifier, Hint_popup, Nav_top, Video_component},
      data(){
          return{
            rowWidth:undefined,

            chooseNumber:1,
            msg_addShopcar:'加入购物车',
            flag_notAddShop_car:true,
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
        addShop_Car(){
          if(this.flag_notAddShop_car){
            this.$axios.post('/car/addShopCarByUserId',{
                commodityId:this.commodity.commodityId,
                chooseNumber:this.chooseNumber
            }).then(res=>{
              if (this.$store.getters.getResultDispose(res)){
                this.flag_notAddShop_car=false;
                this.msg_addShopcar="已添加至购物车";
                this.$store.state.shopCarNumber = Number.parseInt(this.$store.state.shopCarNumber) + this.chooseNumber;
                alert("购物车添加成功");
              }
            })
          }

          },
        shop(){
          this.$axios.post('/order/submitOrderByUserId',[{
            commodityId:this.commodity.commodityId,
            chooseNumber:this.chooseNumber
          }]).then(res=>{

            if (this.$store.getters.getResultDispose(res)){
              this.$axios.get('/order/payOrderByUserId',{
                params:{
                  orderTime:res.data.data
                }
              }).then(res=>{
                if (this.$store.getters.getResultDispose(res))
                  this.$router.push({name:'shop_success'});
              })
            }
          })
        },

      },
      created() {
            this.$axios.get('/commodity/selCommodityByCommodityID',{
              params:{
                commodityId: this.$route.query.commodityId
              }
            }).then(res=>{
              this.commodity = res.data;
              this.resource.poster = this.commodity.commodityPhoto;
              this.resource.src = this.commodity.commodityVideo;
            })
      },
      watch:{
        chooseNumber:function (val) {
          this.chooseNumber=this.chooseNumber.toString().replace(/[^\d]/g,'');

          if (this.chooseNumber=='' || this.chooseNumber<1){
            this.chooseNumber=1;
          }

        }
      }
    }
</script>

<style scoped>

  .container{
    max-width: 1200px;
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
    }

  .messageBox{
    z-index: 0;
    width: 440px;
    height: 400px;
    margin-right: 50px;
  }
  .flag_img_messageBox{
    position: absolute;
    top: 50%;
    transform: translate(0px,-50%);
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
  .chooseNumberBox{
    margin-top: 20px;
  }
  .chooseNumberBox >span{
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
