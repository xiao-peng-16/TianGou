<template>
    <div class="home">
        <div>
          <nav_top id="nav_top" :flag_fixed="false"  :maxWidth="rowWidth"/>
          <div class="gg">
            <el-row>
                <img src="../assets/home_top_img.png">
            </el-row>
          </div>

          <div style="background: white;position: relative;height: 120px">
            <div style="position: absolute;bottom: 0px;width: 100%">
              <el-row>
                <el-col :span="4" style="height: 88px" >
                  <div ref="gifBox" style=""><img src="../assets/gif.gif"></div>
                </el-col>
                <el-col :span="20">
                  <div style="margin: 10px 20px 0px 60px">
                    <serarch @event_click_search_hotWord="event_click_search_hotWord":flag_home="true" :flag_bottom_work="true"/>
                  </div>
                </el-col>
              </el-row>
            </div>
          </div>


            <el-row class="xxxxx">
              <el-col :span="4" style="background: #FF5000">
                主题市场
              </el-col>
              <el-col :span="20" style="background-image: linear-gradient(to right, #FF8300, #FF5000);">
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
              </el-col>
            </el-row>

            <el-row>
              <el-col :span="4">
                <div class="sortBox">
                  <div class="sordWordItemBox" v-for="item in sortList" @click="event_click_sort_word(item.word)">
                    <span class="iconfont" v-html="item.icon"></span>
                    <span>{{item.word}}</span>
                    <span style="position: absolute;right: 10px;">></span>
                  </div>
                </div>
              </el-col>
              <el-col :span="14" style="padding: 10px">
                <!--      relative 中的 top left ，还有 width 、 height-->
                <swiper :imgList="imgList" style="width: 100%; height: 400px;"/>
              </el-col>

              <el-col :span="6">

                <div class="home_user_Box">
                  <img  class="home_user_bj"  :src="home_user_bj">
                  <img  class="home_user_Head" :src="show_userPhotoURL" @click="to_user_center_function">
                  <span class="home_userName">Hi! {{userName}}</span>
                  <div class="home_user_bottom" v-if="undefined == this.$store.state.user.userName">
                    <div @click="$router.push({name:'login'})">登录</div>
                    <div @click="$router.push({name:'register'})">注册</div>
                  </div>
                </div>

                <div style="height: 26px;background: #ffe4dd;color: #f40;font-size: 12px;box-sizing:border-box;padding-left: 80px;padding-top: 3px">
                  网上有害信息举报专区 <span class="iconfont" style="margin-left: 20px;font-size: 13px;">&#xf034f;</span>
                </div>
                <ul class="rrrrrr" >
                  <li v-for="(item,index) in bbbbb_title">
                    <div @mouseenter="bbbbb_index=index " :class="{choose:bbbbb_index == index}">{{item}}</div>
                  </li>
                </ul>
                <div class="bbbbb">
                    <div class="bbbbb_item">{{bbbbb_list[bbbbb_index][0]}}</div>
                    <div class="bbbbb_item">{{bbbbb_list[bbbbb_index][1]}}</div>
                    <div class="bbbbb_item">{{bbbbb_list[bbbbb_index][2]}}</div>
                    <div class="bbbbb_item">{{bbbbb_list[bbbbb_index][3]}}</div>
                </div>

                <div class="icoList">
<!--                  <el-col :span="6" v-for="item in icoList">-->
<!--                    <div class="icoItem">-->
<!--                      <div><img src="../assets/ico.png" :style="{top:item.top}"></div>-->
<!--                      <p style="text-align: center;">{{item.name}}</p>-->
<!--                    </div>-->
<!--                  </el-col>-->


                  <el-col :span="6" class="icoItemBox" v-for="item in icoList">
                    <div class="icoItem">
                      <div><img src="../assets/ico.png" :style="{top:item.top}"></div>
                      <p style="text-align: center;">{{item.name}}</p>
                    </div>
                  </el-col>

                </div>
              </el-col>
            </el-row>

            <el-row style="background: white;margin-top: 5px;">

                <el-col :span="4">
                    <countdown/>
                </el-col>
                <el-col :span="20">
                  <div style="width: 100%;height:230px;">
                    <long_swiper :long_swiper_img_list="long_swiper_img_list"/>
                  </div>
                </el-col>
            </el-row>





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


          bbbbb_index:0,
          bbbbb_title:["公告","规则","论坛","安全","公益"],
          bbbbb_list:[
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
          console.log(this.$router)
        this.rowWidth = document.querySelector('.el-row').offsetWidth;
        var gifBox = this.$refs.gifBox;
        gifBox.firstChild.style.width = gifBox.offsetWidth +"px";
      },
      computed:{
        userName(){
          var userName = this.$store.state.user.userName;
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
        to_user_center_function(){
          this.$router.push({name:'user_center',query:{t:'0',l:'0',ls:'0'}});
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
  .el-row{
    max-width: 1180px;
    margin: 0px auto;
    padding: 0px;
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
    height: 100%;
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
    padding-left: 16px;
  }
  .rrrrrr li{
    float: left;
    width: 20%;
  }
  .rrrrrr li div{
    cursor: pointer;
    margin-top: 10px;
    width: 28px;
  }

  .choose{
    font-weight: 700;
    border-bottom: 2px solid #FF4400;
  }


  .bbbbb{
    background: white;
    height: 60px;
    box-sizing: border-box;
    padding-top: 6px;
    padding-left: 5px;

  }
  .bbbbb_item{
    margin-top: 3px;
    margin-left: 5px;
    float: left;
    width: 47%;
    height: 20px;
    overflow: hidden;
  }


  .icoItemBox{
    height: 74px;
    padding: 0px;
    margin: 0px;
  }


  .icoItem{
    margin: 0px;
    background: white;
    /*float: left;*/
    width: 100%;
    height: 74px;

    border: 1px  solid #F4F4F4;;
    border-right:none;
    border-bottom: none;
  }
  .icoItemBox:nth-child(1) .icoItem,.icoItemBox:nth-child(5) .icoItem{
    border-left:none;
  }

  .icoItem div{
    margin: 0px auto;
    position: relative;
    /*background: #138496;*/
    overflow: hidden;
    width: 24px;
    height: 24px;
    margin-top: 18px;
  }
  .icoList img{
    position: absolute;
    width: 24px;
  }










</style>
