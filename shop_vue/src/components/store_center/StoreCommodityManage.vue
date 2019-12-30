<template>
  <div  style="background: #F8F8F8">


    <commodity_popup :refresh_popup_flag="refresh_popup_flag" :commodity-id="commodityId" @refresh_commodity_list="init" style="z-index: 9999999"/>


    <div style="padding-left: 15px">
      <div style="z-index: 10;position: fixed;height: 30px;width: 1080px;background: #F8F8F8" >
        <span style="position: relative; left: 150px">商品信息</span>
        <span style="position: relative; left: 450px">单价</span>
        <span style="position: relative; left: 560px">销量</span>
        <span style="position: relative; left: 688px">库存</span>
      </div>

      <div style="height: 30px;"></div>

      <div class="itemBox" v-for="item in StoreCommodityList" >
        <div class="imgBox" @click="click_img(item.commodityId)">
          <img :src="item.commodityPhoto">
        </div>
        <div class="cNameBox" @click="click_img(item.commodityId)">
          <span>{{item.commodityName}}</span>
        </div>
        <div class="commodityPrice">
          <span>￥{{item.commodityPrice.toFixed(2)}} 元</span>
        </div>
        <div class="commoditySales">
          <span>{{item.commoditySales}} 件</span>
        </div>
        <div class="commodityStock">
          <span>{{item.commodityStock}} 件</span>
        </div>
        <div class="operate" @click="update_commodity(item.commodityId)">
            <span>修改</span>
        </div>
      </div>
    </div>


  </div>



</template>

<script>
    import Commodity_popup from "@/components/commodity_popup";
    export default {
        name: "StoreCommodityManage",
      components: {Commodity_popup},
      data(){
        return{
          StoreCommodityList:[],
          refresh_popup_flag:0,
          commodityId:undefined,
          dialog:true
        }
      },
      computed:{
        ls(){
          return parseInt(this.$route.query.ls);
        },
      },
      watch:{
        ls(val){
          if (1 == val)
            this.refresh_popup_flag++;
        }
      },
      methods:{
        update_commodity(val){
          this.commodityId=val;
          this.refresh_popup_flag++;
        },
        click_img(commodityId){
          this.$router.push({name:'commodityPage',query:{commodityId}});
        },
        init(title){
          this.$axios.get('/commodity/selStoreCommodityVOByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)) {
                this.StoreCommodityList = res.data.data;

                if (undefined != title){
                  this.refresh_popup_flag = -1;
                  this.$router.push({name:'store_center',query:{l:'1',ls:'0'}});
                  this.$notify.success({
                    title:title,
                  });
                }
              }
            });
        },
      },
      created() {
          this.init();
      }
    }
</script>



<style scoped>
  .itemBox{
    position: relative;
    background: #FCFCFC;
    border: 1px solid #CCCCCC;
    padding:  10px 10px 10px 10px;
    width:1080px;
  }
  .itemBox div{
    display: inline-block
  }

  .imgBox{
    width: 130px;
    cursor: pointer;
  }
  .imgBox img{
    height: 100px;
    width:  100px;
    cursor: pointer;
  }

  .cNameBox{
    width: 300px;
    position: absolute;
    top: 20px;
    left: 145px;
    cursor: pointer;
  }
  .cNameBox:hover{
    text-decoration:underline;
    color: #FF6600;
  }



  .commodityPrice{
    width:160px;
    position: absolute;
    left: 500px;
    top: 45px;
  }
  .commoditySales{
    width:150px;
    position: absolute;
    left: 665px;
    top: 45px;
  }

  .commodityStock{
    width:300px;
    position: absolute;
    left: 825px;
    top: 45px;
  }

  .operate{
    background: #20c997;
    cursor: pointer;
    position: absolute;
    left: 945px;
    top: 45px;
  }

</style>
