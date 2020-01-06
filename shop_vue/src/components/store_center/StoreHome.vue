<template>
  <div style="padding-top: 1px;width: 1080px">

    <div class="store_home_top">
      <img :src="show_userPhotoURL" style="padding-left: 13px;">
      <div class="left_message">
        <span style="font-weight: 600;font-size: 25px;letter-spacing:5px;">{{getUserName()}}</span>
        <div style="margin-top: 33px">
          <span>店铺名称 : </span>
          <span style="padding-left: 8px">{{store.storeName}}</span>
        </div>
        <div style="margin-top: 15px">
          <span>店铺地址 : </span>
          <span style="padding-left: 8px">{{store.storeProvince}}  {{store.storeCity}}</span>
        </div>
      </div>
      <div class="right_message">
        <span style="font-weight: 700;font-size: 18px;color: black">店铺评分</span>
        <div>
          <span>如实描述 : </span>
          <span>{{store.storeDescribe}}</span>
        </div>
        <div>
          <span>服务态度 : </span>
          <span>{{store.storeAttitude}}</span>
        </div>
        <div>
          <span>发货速度 : </span>
          <span>{{store.storeDeliverySpeed}}</span>
        </div>
      </div>


    </div>

    <div class="storeLable">
      <img src="../../assets/StoreHomeLable.jpg">
      <br/>
      <div class="lable_1"><span >{{fullStoreSales.currentMonth.salesVolume}}</span></div>
      <div class="lable_2"><span >{{fullStoreSales.totality.salesVolume}}</span></div>
      <div class="lable_3"><span >{{fullStoreSales.currentMonth.earnings}}</span></div>
      <div class="lable_4"><span >{{fullStoreSales.totality.earnings}}</span></div>
    </div>



  </div>


</template>

<script>
  export default {
        name: "StoreHome",
      data(){
        return{
          store:{},
          fullStoreSales:{
            currentMonth: {
              salesVolume: 0,
              earnings: 0
            },
            totality: {
              salesVolume: 0,
              earnings: 0
            }
          }


        }
      },
      computed:{
        show_userPhotoURL(){
          return this.$store.getters.getUserPhotoURL();
        },
        flag_userOpenStore(){
          return this.$store.state.flag_userOpenStore;
        },
      },
      watch:{
        flag_userOpenStore(val){
          if (!val)
            this.init();
        }
      },
      methods:{
        getUserName(){
          return this.$store.state.user.userName;
        },
        init(){
          this.$axios.post('/store/selStoreResultBeanByStoreId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res))
                this.store = res.data.data;
            });

          this.$axios.post('/order/selStoreStatusFullVOByStoreId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){
                if (null!=res.data.data.currentMonth)
                  this.fullStoreSales.currentMonth = res.data.data.currentMonth;
                if (null!=res.data.data.totality)
                  this.fullStoreSales.totality = res.data.data.totality;
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

  .store_home_top{
    padding: 20px;
    height: 230px;
    box-shadow: 0px 63px 30px -50px #E1ECF5 inset;
  }
  
  .store_home_top img{
    float: left;
    height: 130px;
  }
  .left_message{
    float: left;
    display: inline-block;
    margin-left: 30px;
  }
  .left_message div{
    color:gray;
    font-size: 17.5px;
  }

  .right_message{
    float: right;
    padding: 10px 20px 10px 40px;
    display: inline-block;
    border-left: 1.2px dashed #7BD4F2;
    font-size: 16px;
    color: gray;
    margin-top: 12px;
  }


  .storeLable img{
    float: left;
  }
  .storeLable div{
    display: inline-block;
    width: 267.2px;
    position: relative;
    bottom: 98px;
    padding-left: 45px;
    float: left;
  }
.storeLable span{
  bottom: 105px;
  font-size: 40px;
  font-weight: 600;
}


  .lable_1{
      color: #FEFAEE;
  }
  .lable_2{
    color: #FEFDFC;
  }
  .lable_3{
    color: #FCFBFE;
  }
  .lable_4{
    color: #FFF6F8;
  }


</style>
