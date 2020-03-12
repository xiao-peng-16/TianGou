<template>
  <div>
    <nav_top :flag_fixed="true"/>
    <div class="cart_top_box">
      <el-row>
        <div class="cart_top">
          <img id="tiangou" src="../assets/tmall.jpg">
          <span id="cart">收藏夹</span>
        </div>
      </el-row>
    </div>

    <div style="height: 50px;"></div>



    <div v-if="flag_show">

      <div class="listBox">
        <div class="itemBox" v-for="item in favoriteList"   @click="gotoCommodityPage(item.commodityId)">
          <div style="width: 100%;border: 1px solid gray" >
            <img :src="item.commodityPhoto" style="height: 164px;width:164px" >
          </div>
          <div class="commodityNameBox"><span>{{item.commodityName}}</span></div>
          <div><span style="color: #FF4400">￥{{(item.commodityPrice).toFixed(2)}}</span></div>
        </div>
      </div>

    </div>
    <div v-else style="margin-top: 40px">
      <not-data left="旺~旺~旺~" middle="你的收藏夹还没有商品哟，快去收藏添加吧~"/>
    </div>



  </div>
</template>

<script>
  import Nav_top from "@/components/nav_top";
  import Hint_popup from "@/components/hint_popup";
  import NotData from "@/components/notData";
  export default {
    name: "favorite",
    components: {NotData, Hint_popup, Nav_top},
    data(){
      return{
        favoriteList:[]
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
        return this.favoriteList.length>0;
        return this.favoriteList !=null && this.favoriteList.length != 0;
      },
    },
    created() {
      this.$axios.get('/favorite/listFavoriteCommodityVOByUserId')
        .then(res=>{
          if (this.$store.getters.getResultDispose(res)){
            this.favoriteList = undefined == res.data ? [] : res.data;
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


  .cart_top_box .el-row{
    max-width: 1380px;
    margin: 0px auto;
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



</style>
