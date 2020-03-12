<template>
  <div  style="background: #F8F8F8;padding-left: 35px">


    <commodity_popup :refresh_popup_flag="refresh_popup_flag" :commodity-id="commodityId" @refresh_commodity_list="init" style="z-index: 9999999"/>


    <div>
      <div style="z-index: 10;position: fixed;height: 30px;width: 1080px;background: #F8F8F8" >
        <span style="position: relative; left: 150px">商品信息</span>
        <span style="position: relative; left: 450px">单价</span>
        <span style="position: relative; left: 560px">销量</span>
        <span style="position: relative; left: 688px">库存</span>
        <span style="position: relative; left: 780px">操作</span>
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
        <div class="operate">

          <div v-if="item.commodityOnShelves" class="true_OnShelves">
            <el-dropdown split-button type="primary" @click="update_commodity(item.commodityId)">
              修改
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <div class="my_true_OnShelves_item_box" @click="updCommodityOnShelves(item, false)">
                    下架
                  </div>
                </el-dropdown-item>
                <el-dropdown-item>
                  <div class="my_true_OnShelves_item_box" @click="delCommodity(item)">
                    删除
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
          <div v-else class="false_OnShelves">
            <el-dropdown split-button type="primary" @click="updCommodityOnShelves(item, true)">
              上架
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item>
                  <div class="my_false_OnShelves_item_box" @click="update_commodity(item.commodityId)">
                    修改
                  </div>
                </el-dropdown-item>
                <el-dropdown-item>
                  <div class="my_true_OnShelves_item_box" @click="delCommodity(item)">
                    删除
                  </div>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>




        </div>
      </div>
    </div>



    <div v-show="flag_notData" style="margin-top: 30px;margin-left: -30px ">
      <not-data middle="店铺未出售商品"/>
    </div>

  </div>
</template>

<script>
    import Commodity_popup from "@/components/commodity_popup";
    import NotData from "@/components/notData";
    export default {
        name: "StoreCommodityManage",
      components: {NotData, Commodity_popup},
      data(){
        return{
          StoreCommodityList:[],
          refresh_popup_flag:0,
          commodityId:undefined,
          dialog:true,

          flag_notData:false
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
        updCommodityOnShelves(commodity,commodityOnShelves){

          var msg = commodityOnShelves? '重新上架' : '下架';
          this.$confirm('此商品将'+msg+', 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            this.$axios.get('/commodity/updCommodityOnShelvesByStoreId',{
              params:{
                commodityId: commodity.commodityId,
                commodityOnShelves : commodityOnShelves
              }
            }).then(res=>{
              if (this.$store.getters.getResultDispose(res)) {
                if (res.data){
                  commodity.commodityOnShelves = commodityOnShelves;
                  this.$notify.success({
                    title:'商品'+msg+'成功',
                  });
                }
              }
            });

          });
        },
        delCommodity(commodity){
          this.$confirm('此商品将永久删除, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {

            this.$axios.get('/commodity/delCommodityByStoreId',{
              params:{
                commodityId: commodity.commodityId,
              }
            }).then(res=>{
              if (this.$store.getters.getResultDispose(res)) {
                if (res.data){
                  this.init();
                  this.$notify.success({
                    title:'商品永久删除',
                  });
                }
              }
            });

          });
        },
        init(title){
          this.$axios.get('/commodity/listStoreCommodityVOByStoreId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)) {
                this.StoreCommodityList = res.data;
                this.flag_notData = 0 == this.StoreCommodityList.length;

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
    cursor: pointer;
    position: absolute;
    left: 945px;
    top: 45px;
  }







  /deep/ .operate .el-button{
    padding: 7px 6px;
    font-size: 15px;
  }
  /deep/ .true_OnShelves .el-button{
      background: #17A2B8;
      border-color:#17A2B8;
  }
  /deep/ .false_OnShelves .el-button{
    background: #24B846;
    border-color:#24B846;
  }

 /deep/ .operate .el-button--primary:first-child{
    width: 55px;
  }

  /deep/ .operate .el-button--primary:nth-child(2){
    width: 30px;
  }

  /deep/  .popper__arrow{
    display: none;
  }

  /deep/ .el-dropdown-menu{
    padding: 0px;
    margin: 0px;
  }

  /deep/  .el-dropdown-menu__item{
    padding: 0px;
    width: 85px;
    line-height: 30px;
    color: white;
    }
  /deep/  .el-dropdown-menu__item:hover{
    color: white;
  }

 .my_true_OnShelves_item_box{
  padding-left: 25px;
  background: #EE3143;
}
  .my_false_OnShelves_item_box{
    padding-left: 25px;
    background: #17A2B8;
  }

</style>

