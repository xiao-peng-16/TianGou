<template>
  <div>
    <nav_top :flag_fixed="true"/>
    <div style="position: fixed;width: 100%;height: 60px; background: white;z-index: 100;padding-top: 35px">
      <img id="tiangou" src="../assets/tmall.jpg">
      <span id="favorite">收藏夹</span>
    </div>
    <div style="height: 50px;"></div>



    <div v-if="flag_show">

      <div class="listBox">
        <div class="itemBox" v-for="item in dataList"   @click="gotoCommodityPage(item.commodityId)">
          <div style="width: 100%;border: 1px solid gray" >
            <img :src="item.commodityPhoto" style="height: 164px;width:164px" >
          </div>
          <div class="commodityNameBox"><span>{{item.commodityName}}</span></div>
          <div><span style="color: #FF4400">￥{{(item.commodityPrice).toFixed(2)}}</span></div>
        </div>
      </div>

    </div>
    <div v-else>
      <div class="notData">
        <img src="../assets/search_notdata.png">
        <span class="side">旺~旺~旺~</span>
        <span class="maddle">你的收藏夹还没有商品哟，快去收藏添加吧~</span>
      </div>
    </div>



  </div>
</template>

<script>
  import Nav_top from "@/components/nav_top";
  import Hint_popup from "@/components/hint_popup";
  export default {
    name: "favorite",
    components: {Hint_popup, Nav_top},
    data(){
      return{
        dataList:[]
      }
    },
    methods:{
      gotoCommodityPage(commodityId){

        const {href} = this.$router.resolve({
          name:'commodityPage',
          query:{
            //新页面 参数通过url 参数 不能直接json
            // CommodityMessage:this.$qs.stringify(this.props_CommodityMessage),
            commodityId:commodityId
          }
        });
        window.open(href,'_blank');
      }
    },
    computed:{
      flag_show(){
        return this.dataList.length>0;
        return this.dataList !=null && this.dataList.length != 0;
      },
    },
    created() {
      this.$axios.get('/favorite/selFavoriteCommodityVOByUserId')
        .then(res=>{
          if (this.$store.getters.getResultDispose(res)){
            this.dataList = res.data.data;
          }
        });

    }
  }
</script>

<style scoped>

  .listBox{
    width: 1000px;
    margin: 20px auto;
  }

  .itemBox{
    cursor: pointer;
    height: 226px;
    width: 165px;
    float: left;
    margin-bottom: 14px;
    margin-right: 18px;
    text-align: center;

  }
 .commodityNameBox{
   overflow: hidden;
   text-overflow:ellipsis;
   white-space: nowrap;


 }


  #tiangou{
    margin-left: 70px;
    height: 35px;
    position: relative;
    bottom: 5px;
  }
  #favorite{
    font-weight: 600;
    font-size: 25px;
  }



  .notData{
    margin-top: 50px;
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
</style>
