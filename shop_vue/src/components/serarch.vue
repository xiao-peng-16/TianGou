<template>
    <div class="searchBox">
      <div class="search">
        <div  :class="{flagHome:flag_home,flagSearch:!flag_home}">
          <div class="search_form">
              <input type="text" v-my-focus v-model="search_word" @keyup.enter="event_click_search" :placeholder="msg" :class="{red_hint:flag_red_hint}">
              <input @click="event_click_search" type="button" value="搜 索">
          </div>
        </div>
        <ul class="words_list" v-if="flag_bottom_work">
          <li v-for="(hot_word,index) in hot_words_list" >
            <span v-show="(index!=0)" class="split">|</span>
            <span @click="event_click_hot_word(hot_word)"
                  :class="{Home_hot_wordBox:flag_home,SearchPage_hot_wordBox:!flag_home}">
              {{hot_word}}
            </span>
          </li>
        </ul>

      </div>
    </div>
</template>

<script>
    export default {
        name: "serach",
      props:['flag_home','flag_bottom_work','props_search_word'],
      data(){
          return{
            flag_red_hint:false,
            msg:"搜索 天猫 商品/品牌/店铺",
            search_word:this.$store.state.search_word,
            hot_words_list:['美食','装饰品','珠宝','水果','宠物','运动健身','男装','女装','童装','手机']
          }
      },
      watch:{
        search_word(val){
          if (this.flag_red_hint) {
            this.flag_red_hint=false;
            this.msg="搜索 天猫 商品/品牌/店铺";
          }
          this.$store.state.search_word = this.search_word;
        },
        store_search_word(val){
          this.search_word= val;
        }
      },
      computed:{
        store_search_word(){
          return this.$store.state.search_word;
        }
      },
      methods:{
        event_click_search(){
            if (this.search_word!=''&&this.search_word!=undefined) {
              this.$emit('event_click_search_hotWord',this.search_word);
            }else {
              this.flag_red_hint=true;
              this.msg="请输入关键词";
            }
          },
          event_click_hot_word(val_hot_word){
            this.$store.state.search_word = val_hot_word;
            this.$emit('event_click_search_hotWord');
         }

      }
    }
</script>

<style scoped>


  .red_hint::-webkit-input-placeholder {
    color:red;
    font-weight: 700;
  }

  *{
    padding: 0;
    margin: 0;
  }
  body{
    font-family: "微软雅黑";
  }
  .searchBox{
    display: inline-block;
  }
  .split{
    color: #999999;
    margin-right:5px;
  }
  ul,li{
    margin: 0px;
    padding: 0px;
    list-style: none;
    display: inline-block;

  }
  .words_list{
  }

  .words_list li{
    height: 32px;
    margin-right:10px;
  }
  .words_list li:nth-of-type(odd){
    color: #999999;
  }
  .words_list li:nth-of-type(even){
    color: #FF0036;
  }
  .words_list span{
    cursor: pointer;
  }
  .split{
    cursor: none;
  }
  .search{
    display: inline-block;
  }
  .searchMannerList{
    float: left;
  }
  .search_form{
    overflow:  hidden;
    display: flex;
  }
  .search_form input[type=text]{
    border: none;
    margin-left: 10px;
    margin-top: 3px;
    outline:none;
    float: left;
    background: white;

  }
  .search_form input[type=button]{
    font-size: 18px;
    border: none;
    cursor: pointer;
    text-align: center;
    font-family: "微软雅黑";
    position: relative;
    left: 2px;
    color: white;
  }

  .flagHome{
    height: 40px;
    width: 625px;
    border: 2px solid #FF5000;
  }
  .flagSearch{
    height: 35px;
    width: 850px;
    border: 3px solid #FF0036;

  }

  .flagHome input[type=text]{

    font: 12px/1.5 tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;

    margin-top: 7px;
    height: 25px;
    line-height: 25px;
    font-size: 16px;
    width: 100%;
  }
  .flagHome input[type=button]{
    background: #FF5000;
    width: 167px;
    height: 38px;
    line-height: 40px;
    font-weight:900;
  }


  .flagSearch input[type=text]{

    font: 12px/1.5 tahoma,arial,'Hiragino Sans GB','\5b8b\4f53',sans-serif;

    height: 25px;
    line-height: 25px;
    font-size: 16px;
    width: 100%;
  }
  .flagSearch input[type=button]{
    background: #FF0036;


    height: 35px;
    line-height: 40px;
    font-weight:900;
    width: 132px;
    position: relative;
    bottom: 5px;
  }
  .Home_hot_wordBox{
    font-size: 13.8px;

  }
    .SearchPage_hot_wordBox{
      font-size: 12.5px;
    }
</style>
