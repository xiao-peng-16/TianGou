<template>
    <div>
        <nav_top :flag_scroll="true" :flag_fixed="false"/>
        <nav_right/>

        <div class="topFixed_serarch" ref="topFixed_serarch">
          <img src="../assets/tmall.jpg">
          <div style="position: relative;top: -40px;left:23%">
            <serarch @event_click_search_hotWord="event_click_search_hotWord" :flag_home="false"/>
          </div>
        </div>

      <el-row>
        <div class=" second">
          <img src="../assets/tmall.jpg" style="display: inline-block;position: relative;bottom: 33px">
          <div class="serarch" style="display: inline-block;">
            <serarch  @event_click_search_hotWord="event_click_search_hotWord" :flag_bottom_work="true" :flag_home="false"/>
          </div>
        </div>
      </el-row>


          <el-row><div class="split"></div></el-row>


          <div v-show="!flag_notData">
            <el-row>
              <div class="col-sm-1-3 col-md-1-4 col-lg-1-5 spc" v-for="itemData in commodityList">
                <SearchPage_commodity  :props_CommodityMessage="itemData"/>
              </div>
            </el-row>
            <el-row>
              <div class="pageBox">
                <el-pagination :background="true"
                               layout="total, prev, pager, next"
                               :total="commoditySum"
                               :page-size="pageStepSize"
                               :current-page.sync="pageNo">
                </el-pagination>
              </div>
            </el-row>


          </div>

        <div class="notData" v-show="flag_notData">
          <img src="../assets/search_notdata.png">
          旺~没找到与
          <span>“ {{search_word}} ”</span>
          相关的商品哦。
        </div>

    </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    import serarch from "@/components/serarch";
    export default {
        name: "searchPage",
      components: {serarch, Nav_top,
        SearchPage_commodity:resolve => {
          require(['../components/SearchPage_commodity'],resolve)
        },
        Nav_right:resolve => {
          require(['../components/Nav_right'],resolve)
        },
      },
      data(){
          return{
            flag_topFixed_serarch:false,
            flag_notData:false,
            commoditySum:0, //搜索到的商品总数
            commodityList:[],  //搜索到的商品列表 分页

            pageNo:1,//  分页 页码
            pageStepSize:10 ,//  分页 步长

            flag_backTop:false
          }
      },
      computed:{
        search_word(){
          return this.$store.state.search_word;
        }
      },
      watch:{
        pageNo(val){
            this.sendSearch();
            this.backTop();
        },
        flag_topFixed_serarch(val){
          if (val){
            this.$refs.topFixed_serarch.style.transform = "translate(0px,-37px)"
          } else {
            this.$refs.topFixed_serarch.style.transform = "translate(0px,-90px)"
          }
        }

      },
      methods:{// 点击图片回到顶部方法，加计时器是为了过渡顺滑
        backTop () {
          let that = this;
          let timer = setInterval(() => {
            let ispeed = Math.floor(-that.scrollTop / 5)
            document.documentElement.scrollTop = document.body.scrollTop = that.scrollTop + ispeed;
            if (that.scrollTop == 0) {
              clearInterval(timer)
            }
          }, 0.6)
        },
        // 为了计算距离顶部的高度，当高度大于xx显示回顶部图标，小于x则隐藏
        scrollToTop () {
          let that = this;
          let scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop
          that.scrollTop = scrollTop;
          if (that.scrollTop > 100) {
            that.flag_topFixed_serarch = true
          } else {
            that.flag_topFixed_serarch = false
          }
        },



          sendSearch(){
            this.$axios.get('/commodity/getSearchVO',{
              params:{
                searchWord: this.$store.state.search_word,
                pageNo : this.pageNo,
                pageStepSize : this.pageStepSize
              }
            }).then(res=>{
              this.commoditySum = res.data.commoditySum;
              this.commodityList = res.data.searchCommodityVOList;
              this.flag_notData =  undefined == this.commoditySum  || this.commoditySum == 0;

              // 第一次 不
              if (this.flag_backTop){
                this.backTop();
              }else this.flag_backTop=true;
            });

          },
          event_click_search_hotWord(){
              this.pageNo = 1;
              this.sendSearch();
          }
      },
      created() {
        this.sendSearch();
      },
      mounted () {
        window.addEventListener('scroll', this.scrollToTop)
      },
      destroyed () {
        window.removeEventListener('scroll', this.scrollToTop)
      },
    }
</script>

<style scoped>
  
  .go-top{
    cursor: pointer;
    height: 90px;
    position: fixed;
    bottom: 20px;
    right: 20px;
  }

  .topFixed_serarch{
    margin-left: 5%;
    width: 90%;
    height: 50px;
    background: white;
    position: fixed;
    z-index: 50;
    border: 1px solid #e8e8e8;

    transform: translate(0px,-90px);
    transition:transform 0.8s;
    -moz-transition:transform 0.8s; /* Firefox 4 */
    -webkit-transition:transform 0.8s; /* Safari and Chrome */
    -o-transition:transform 0.8s; /* Opera */
  }
  
  
  .spc{
    margin-top: 20px;
  }
  
  .el-row{
    max-width: 1380px;
    margin: 0px auto;
    padding: 0px;
  }



  .second{
    padding-top: 15px;
    padding-left: 10px;
    padding-bottom: 1px;
    height: 100px;
    width: 100%;
  }
  .second img{
    width: 220px;
    height: 40px;
  }
  .serarch{
    margin-left: 90px;
    margin-top: 8px;
  }
  .split{
    margin: 0 auto;
    height: 1px;
    width: 98.5%;
    background: #E5E5E5;

  }
  .notData{
    background: #FFF8F6;
    border: 1px solid #F7EAE7;
    height: 90px;
    padding-left: 25%;
  }
  .notData img{
    height: 60px;
    margin: 15px;
  }
  .notData span{
    color: #790103;
    font-weight: bold;
  }


  .pageBox{
    float: right;
    margin-top: 40px;
    margin-bottom: 70px;
  }




  @media (min-width: 768px) {
    .col-sm-1-3 {
      width: 33%;
      float: left;
    }
  }

  @media (min-width: 1100px) {
    .col-md-1-4 {
      width: 25%;
      float: left;
    }
  }

  @media (min-width: 1280px) {
    .col-lg-1-5 {
      width: 20%;
      float: left;
    }
  }

</style>
