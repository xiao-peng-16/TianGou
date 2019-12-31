<template>
    <div style="">
        <nav_top :flag_fixed="true" :flag_scroll="true" />
        <div style="position: fixed;width: 100%;height: 110px;padding-top: 50px; background: white;z-index: 100;border-bottom: 1px solid #DADADA">
          <img  src="../assets/tmall.jpg" style="float: left;width: 243px;height: 45px">
          <div class="top_box">
            <span id="title">— 卖家中心 —</span>
          </div>
        </div>

      <div class="buttom">
        <div class="left">
          <div  v-for="(item, index) in left_list">
            <div class="left_itemBox"  @click="click_left_options(index)"  :class="{optionsBox:left_options==index,not_optionsBox:left_options!=index}">
              <span>{{item.title}}</span>
              <div class="left_item_parent_arrows" :class="{left_item_parent_arrows_right:!item.flag_show_son_list}" v-show="undefined!=item.son_list">
                <span style="font-size: 15px;" class="iconfont">&#xe60f;</span>
              </div>
            </div>
            <transition name="fade"  v-for="(son_item,son_index) in item.son_list">
              <div  class="left_itemBox_son"  v-show="undefined!=item.son_list && item.flag_show_son_list" @click="click_left_son_options(son_index)"  :class="{optionsBox:is_shine_target(index,son_index),not_optionsBox:!is_shine_target(index,son_index)}">
                <span>{{son_item.title}}</span>
              </div>
            </transition>
          </div>
        </div>


        <div class="right" :class="{flag_background:flag_background}">
          <component :is="optionsComponents"/>
        </div>
      </div>



    </div>
</template>

<script>
    import Nav_top from "@/components/nav_top";
    import StoreCommodityManage from "@/components/store_center/StoreCommodityManage";
    import StoreHome from "@/components/store_center/StoreHome";
    import StoreOrderManage from "@/components/store_center/StoreOrderManage";
    import Hint_popup from "@/components/hint_popup";
    export default {
        name: "store_center",
      components: {Hint_popup, StoreHome, StoreCommodityManage, StoreOrderManage, Nav_top},
      data(){
          return{
            l:undefined,
            ls:undefined,

            optionsComponents:undefined,
            left_list:[
              {
                title:'店铺概况',
                components:'StoreHome'
              },
              {
                title:'商品管理',
                flag_show_son_list:false,
                son_list:[
                  {
                    title:'查看商品',
                    components:'StoreCommodityManage'
                  },
                  {
                    title:'添加商品',
                    components:'StoreCommodityManage'
                  }
                ]
              },
              {
                title:'订单管理',
                components:'StoreOrderManage'
              },
            ],
          }
      },
      computed:{
        left_options(){
          for (var i = 0;i<this.left_list.length;i++){
            if (undefined != this.left_list[i].son_list){
              if (i == this.l)
                this.left_list[i].flag_show_son_list = true;
              else
                this.left_list[i].flag_show_son_list = false;
            }
          }
          return this.l;
        },
        left_son_options(){
          return parseInt(this.ls);
        },
        flag_background(){
          return this.left_options == 1 && this.left_son_options ==1;
        }
      },
      methods:{
        is_shine_target(l,ls){
          var query  = this.$route.query;
          return l == query.l && ls == query.ls;
        },
        setOptionsComponents(){
          if (undefined != this.left_list[this.left_options].components){
            this.optionsComponents = this.left_list[this.left_options].components;
          } else if (undefined != this.left_son_options){
            this.optionsComponents = this.left_list[this.left_options].son_list[this.left_son_options].components;
          }else return;
          this.$router.push({name:'store_center',query:{
              l:this.l,
              ls:this.ls
            }});
        },

        click_left_options(val){
          if (undefined != this.left_list[val].route){
            this.$router.push(this.left_list[val].route)
          } else {
            if (this.l == val){
              this.left_list[val].flag_show_son_list = !this.left_list[val].flag_show_son_list;
            } else {
              this.l = val;
              // 默认选项
              this.ls = this.l == 1 ? 0 : undefined;
              // if (undefined == this.left_list[this.left_options].son_list)
                this.setOptionsComponents();
            }
          }
        },
        click_left_son_options(val){
          this.ls = val;
          this.setOptionsComponents();
        }
      },
      created() {
        //拷贝 this.query 无法修改
        var query  = JSON.parse(JSON.stringify(this.$route.query));
        this.l = parseInt(query.l);
        this.ls = parseInt(query.ls);


        if (0>this.l || this.left_list.length-1<this.l || undefined == this.l)
          this.l = 0;

        // 默认选项
        if (this.l == 1){
          this.ls = 0;
        }

        this.setOptionsComponents();
      }
    }
</script>

<style scoped>


  .top_box{
    position: relative;
    width: 100%;
    height: 100%;
    margin-left: 260px;
  }
  #title{
    position: absolute;
    top: 45%;
    left: 33%;
    transform: translate(-38%,-50%);
    font-size: 16px;
    font-weight: 700;
  }


  .optionsBox_left{
    background: #292929;
    border-left: 2px solid #F28328;

  }
  .not_optionsBox{
    border-left: 2px solid #333333;

  }


  .right{
    width: 100%;
    position: absolute;
    padding-left: 280px;
    padding-top: 110px;
    background: #F8F8F8;
    height: 100%;
  }

  .left{
    padding-top: 112px;

    width: 260px;
    height: 100%;
    background: #333333;
    position: fixed;
    z-index: 10;
  }
  .optionsBox{
    background: #292929;
    border-left: 2px solid #F28328;

  }
  .not_optionsBox{
    border-left: 2px solid #333333;

  }
  .left_itemBox{
    padding: 10px 0px 10px 70px;
    color: #FAFAFA;
    letter-spacing:2px;
    font-weight: 400;
    cursor: pointer;
  }

  .left_item_parent_arrows_right{
    transform: rotate(-90deg);
  }
  .left_item_parent_arrows{
    display: inline-block;
    transition:transform 0.3s;
    -moz-transition:transform 0.3s; /* Firefox 4 */
    -webkit-transition:transform 0.3s; /* Safari and Chrome */
    -o-transition:transform 0.3s; /* Opera */
  }
  .left_itemBox_son{
    padding: 10px 0px 10px 105px;
    color: #FAFAFA;
    letter-spacing:2px;
    font-weight: 400;
    cursor: pointer;
  }


  .fade-enter-active, .fade-leave-active {
    transition: opacity .5s;
  }
  .fade-enter, .fade-leave-to /* .fade-leave-active below version 2.1.8 */ {
    opacity: 0;
  }

  .white_background{
    background: white;
  }

</style>
