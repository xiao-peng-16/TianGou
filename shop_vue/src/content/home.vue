<template>
    <div class="home">
        <div>
          <nav_top id="nav_top" :flag_fixed="false"  :maxWidth="rowWidth"/>
          <div class="gg">
            <div class="container">
              <img src="../assets/home_top_img.png">
            </div>
          </div>

          <div style="background: white">
            <div class="container">
              <div class="row" style="height: 120px">
                <div class="col-2" ref="gifBox" style="position:relative;">
                  <img src="../assets/gif.gif" style="position: absolute;bottom: 0px">
                </div>
                <div class="col-7 offset-1" style="margin: 38px 20px 0px 60px">
                  <serarch @event_click_search_hotWord="event_click_search_hotWord":flag_home="true" :flag_bottom_work="true"/>
                </div>
              </div>
            </div>
          </div>

          <div class="container">

            <div class="row xxxxx">
              <div class="col-2" style="background: #FF5000">
                主题市场
              </div>
              <div class="col-10" style="background-image: linear-gradient(to right, #FF8300, #FF5000);">
                <ul>
                  <li>天狗</li>
                  <li>聚划算</li>
                  <li>天狗超市</li>
                  <li>|</li>
                  <li>并夕夕</li>
                  <li>淘抢购</li>
                  <li>电器城</li>
                  <li>私发拍卖</li>
                  <li>兴农脱贫</li>
                  <li>|</li>
                  <li>智能生活</li>
                  <li>苏宁易购</li>
                </ul>
              </div>
            </div>

            <div class="row">
              <div class="col-2" >
                <div class="sortBox">
                  <div class="sordWordItemBox" v-for="item in sortList" @click="event_click_sort_word(item.word)">
                    <span class="iconfont" v-html="item.icon"></span>
                    <span>{{item.word}}</span>
                    <span style="position: absolute;right: 10px;">></span>
                  </div>
                </div>
              </div>
              <div class="col-7" style="padding: 10px">
                <!--      relative 中的 top left ，还有 width 、 height-->
                <swiper :imgList="imgList" style="width: 100%; height: 400px;"/>
              </div>

              <div class="col-3">
                <div class="home_user_Box">
                  <img  class="home_user_bj"  :src="home_user_bj">
                  <img  class="home_user_Head" :src="show_userPhotoURL" @click="click_user_head">
                  <span class="home_userName">Hi! {{userName}}</span>
                  <div class="home_user_bottom" v-if="undefined == this.$store.state.userName">
                    <div @click="$router.push({name:'login'})">登录</div>
                    <div @click="$router.push({name:'register'})">注册</div>
                  </div>
                </div>

                <div style="height: 26px;background: #ffe4dd;color: #f40;font-size: 12px;padding-left: 80px;padding-top: 3px">
                  网上有害信息举报专区 <span class="iconfont" style="margin-left: 20px;font-size: 13px;">&#xf034f;</span>
                </div>
                <ul class="rrrrrr" >
                  <li v-for="(item,index) in bbbbbbbbb_title">
                    <div @mouseenter="bbbbbbbbb_index=index " :class="{choose:bbbbbbbbb_index == index}">{{item}}</div>
                  </li>
                </ul>
                <div class="bbbbbbbbb">
                  <div>{{bbbbbbbbb_list[bbbbbbbbb_index][0]}}</div>
                  <div>{{bbbbbbbbb_list[bbbbbbbbb_index][1]}}</div>
                  <div>{{bbbbbbbbb_list[bbbbbbbbb_index][2]}}</div>
                  <div>{{bbbbbbbbb_list[bbbbbbbbb_index][3]}}</div>
                </div>

                <div class="icoList">
                  <div class="icoItem" v-for="item in icoList">
                    <div><img src="../assets/ico.png" :style="{top:item.top}"></div>
                    <p style="text-align: center;">{{item.name}}</p>
                  </div>
                </div>

              </div>

            </div>
            <div class="row" style="background: white;margin-top: 5px;">

              <div class="col-2">
                <countdown/>
              </div>
              <div class="col-10 bottom_img_list">
                <long_swiper :long_swiper_img_list="long_swiper_img_list" />
              </div>

            </div>

          </div>




        </div>
    </div>
</template>

<script>
    import home_data from "./home_data"

    import Nav_top from "@/components/nav_top";
    import serarch from "@/components/serarch";
    import Hint_popup from "@/components/hint_popup";
    import Swiper from "@/components/swiper";

    import home_user_1 from "@/assets/home_user_1.png";
    import home_user_2 from "@/assets/home_user_2.png";
    import Long_swiper from "@/components/long_swiper";
    import Countdown from "@/components/countDown";


    export default {
        name: "home",
      components: {Countdown, Long_swiper, Swiper, Hint_popup, serarch, Nav_top},
      data() {
        return {
          rowWidth:undefined,
          home_user_bj:home_user_1,

          imgList:home_data.imgList,

          sortList:home_data.sortList,


          bbbbbbbbb_index:0,
          bbbbbbbbb_title:["公告","规则","论坛","安全","公益"],
          bbbbbbbbb_list:[
            ["20余家服务商受邀造访上交所",'阿里巴巴推出“联盟级标准”',"淘宝造物节之城市秘密","聚划算88红包省钱卡"],
            ["新增《淘宝网汽配行业管理规范》公示通知","《淘宝网区域零售服务说明》变更公示通知","《淘宝网票务行业管理规范》变更公示通知","《淘宝网数字娱乐市场须提供官方授权的商品目录》变更公示通知"],
            ["淘宝1212大促招商","在线职业培训招商","在线职业培训招商","运营神器年中大促"],
            ["魔豆妈妈公益项目","让母爱永不打烊！","卖家注意：风险通报！","售假获刑又起诉！"],
            ["淘宝公益平台正式更名","益起来商家招募即将截止","卖家如何设置公益宝贝","公益机构开店全攻略"],
          ],
          icoList:[
            {top:'0px',name:'充话费'},{top:'-44px',name:'旅行'},{top:'-88px',name:'车险'},{top:'-130px',name:'游戏'},
            {top:'-175px',name:'彩票'},{top:'-220px',name:'电影'},{top:'-264px',name:'酒店'},{top:'-308px',name:'理财'},
          ],


          long_swiper_img_list:home_data.long_swiper_img_list,
        }
      },
      mounted() {
        this.rowWidth = document.querySelector('.row').offsetWidth;
        var gifBox = this.$refs.gifBox;
        gifBox.firstChild.style.width = gifBox.offsetWidth +"px";
      },
      computed:{
        userName(){
          var userName = this.$store.state.userName;
          if (undefined ==userName){
            this.home_user_bj = home_user_1;
            return "你好";
          }
          else{
            this.home_user_bj = home_user_2;
            return userName;
          }
        },
        show_userPhotoURL() {
          return this.$store.getters.getUserPhotoURL();
        }
      },
      methods:{
        event_click_search_hotWord(){
          this.$router.push({name:'searchPage'});
        },
        event_click_sort_word(val){
          this.$store.state.search_word = val.split('/')[0]  ;
          this.$router.push({name:'searchPage'});
        },
        click_user_head(){
          this.$router.push({name:'user_center'});
        }

      },

    }
</script>

<style scoped>
  .home{
    width:100%;
    background: #F4F4F4;
    padding-bottom: 20px;
    font: 12px/1.5 tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;

  }

  *{
    padding: 0;
    margin: 0;
  }
  .container{
    margin: 0px auto;
    max-width: 1180px;
  }
  .gg{
      background: #FFE700;
      height: 70px;
  }
  .gg img{
    height: 70px;
    width: 100%;
  }





  .sortBox{
    border: 1.2px solid #ff5000;
    border-top: none;
    background: white;
    /*width: 290px;*/
    color: #666;
    font-size: 16px;
    font-weight: 400;
    padding-top: 4px;
  }

  .sordWordItemBox{
    position: relative;
    cursor: pointer;
    padding: 5px 0px 4px 22px;
  }
  .sordWordItemBox:hover{
    background: #FFF4F1;
    color: #ff5000;

  }
  
  .xxxxx{
    height: 30px;
    text-align: center;
    font-size: 16px;
    color: white;
    font-weight: bold;
  }
  .xxxxx div{
    padding-top: 3px;
  }
  ul{
    list-style: none;
    margin: 0px;
    padding-left: 0px;
  }
  .xxxxx li{
    float: left;
    margin: 0px 5px;
    padding: 0px 4px;
  }




  .home_user_Box{
    height: 135px;
    margin-top: 10px;
    overflow: hidden;
    position:relative;
  }

  .home_user_bj{
    width: 100%;
    height: 150px;
  }
  .home_user_Head{
    width: 50px;
    height: 50px;
    position: absolute;
    top: 10px;
    left: 50%;
    transform: translate(-50%);
    border-radius: 50px;
    cursor: pointer;
  }
  .home_userName{
    position: absolute;
    top: 60px;
    left: 50%;
    transform: translate(-50%);

  }

  .home_user_bottom{
    position: absolute;
    bottom: 12px;
    width: 100%;
    padding-left: 50px;

  }
  .home_user_bottom div{
    cursor: pointer;
    width: 92px;
    height: 25px;
    float: left;
    font-size: 14px;
    line-height: 25px;
    color: white;
    text-align: center;
    margin-right: 7px;
    border-radius: 4px;
    background: #FF5601;
    font-weight: 700;
  }



  .rrrrrr{
    background: white;
    height: 32px;
    padding-top: 10px;    
    padding-left: 16px;
  }
  .rrrrrr li{
    float: left;
    width: 20%;
  }
  .rrrrrr li div{
    cursor: pointer;
    width: 28px;
    width: 28px;
  }

  .choose{
    font-weight: 700;
    border-bottom: 2px solid #FF4400;
  }


  .bbbbbbbbb{
    background: white;
    height: 60px;
    width:100%;
    padding-top: 6px;
    padding-left: 5px;
  }
  .bbbbbbbbb div{
    margin-top: 3px;
    margin-left: 5px;
    float: left;
    width: 47%;
    height: 20px;
    overflow: hidden;
  }





  .icoItem{
    background: white;
    float: left;
    width: 25%;
    height: 74px;

    border-right: 1px solid #F4F4F4;
    border-bottom: 1px solid #F4F4F4;
  }
  .icoItem:nth-child(4n){
    border-left:none;
  }
  .icoItem:nth-child(1),.icoItem:nth-child(2),.icoItem:nth-child(3),.icoItem:nth-child(4){
    border-top: 1px solid #F4F4F4;;
  }
  .icoItem div{
    margin: 0px auto;
    position: relative;
    /*background: #138496;*/
    overflow: hidden;
    width: 24px;
    height: 24px;
    margin-top: 11px;
  }
  .icoList img{
    position: absolute;
    width: 24px;
  }










</style>
