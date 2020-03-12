<template>
  <div>


    <el-row style="height: 83px">
      <img src="../assets/tmall.jpg" height="38">
    </el-row>

    <div class="body">
      <el-row class=" content">
        <el-col :offset="14" :span="10">
          <div class="fromBox">
            <div v-if="flag_hint" class="hintBox">
              <span id="hint_icon" class="iconfont">&#xeb65;</span>
              <span id="hint_msg">{{this.hint}}</span>
            </div>
            <span v-else style="font-weight: bold;color: #212529;">密码登录</span>
            <div class="from">
              <div class="inputBox">
                <div class="iconfontBox"><span class="iconfont">&#xe7ae;</span></div>
                <input v-my-focus v-model="userName" @keyup.enter="focusPassword"  type="text" placeholder=" 用户名/邮箱/手机号">
              </div>

              <div class="inputBox">
                <div class="iconfontBox"><span class="iconfont">&#xe7c9;</span></div>
                <input v-model="userPassword" @keyup.enter="getTokenByPassword" ref="el_userPassword" type="password" placeholder="密码">
              </div>

              <br/><button id="enter" @click="getTokenByPassword">登录</button>
              <br/><br/>
              <router-link class="buttom" :to="{name:'register'}">免费注册</router-link>
              <router-link class="buttom" :to="{name:'drop_password'}">忘记密码</router-link>
            </div>
          </div>
        </el-col>
      </el-row>

    </div>

  </div>
</template>

<script>
  export default {
    name: "login",
    data(){
      return{
        // userName:"",
        // userPassword:"",
        userName:"晓鹏",
        userPassword:"123456",

        flag_hint:false,
        hint:"提示"
      }
    },
    created(){

    },
    methods:{
      focusPassword(){
        this.$refs.el_userPassword.focus();
      },
      getTokenByPassword(){
        if (this.userName=="" && this.userPassword==""){
          this.flag_hint=true;
          this.hint="请输入账户名和密码";
        } else if (this.userName==""){
          this.flag_hint=true;
          this.hint="请填写账户名";
        }else if (this.userPassword==""){
          this.flag_hint=true;
          this.hint="请输入密码";
        } else {

          this.$axios.post('user/getTokenByPassword',{
            userName : this.userName,
            userPassword : this.userPassword
          }).then(res=>{
            if (res.data.success){
              localStorage.setItem("token",res.data.data);
              this.$router.go(-1);
              this.$router.push({name:'home'});//防止没上一级页面
            } else {
              this.flag_hint=true;
              this.hint="你输入的密码和账户名不匹配";
            }
          })
        }
      }



    }




  }
</script>

<style scoped>


  .el-row{
    max-width: 1200px;
    box-sizing: border-box; 
    margin: 0px auto;
    padding: 0px;
  }
  .el-row img{
    margin: 20px 0px 25px 0px;
  }
  .body{
    background:#F94F02;
    font-size: 16px;
    font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";

  }
  .content{
    background-image: url("http://q72z15qf5.bkt.clouddn.com/login_bj.jpg");
    background-repeat:no-repeat ;
    background-size:100% 100%;
    height: 600px;
    box-sizing: border-box;
    padding-top: 90px;
  }
  .fromBox{
    background: white;
    width: 354px;
    height: 400px;
    box-sizing: border-box;
    padding: 25px 26px 20px;
    margin-left: 75px;

  }
  .hintBox{
    display: inline-block;
  }
  .iconfont {
    line-height: 25px;
    font-family: "iconfont" !important;
    font-size: 25px;
    font-style: normal;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-weight: bold;
  }
  .iconfontBox{
    height: 38px;
    width: 40px;
    box-sizing: border-box;
    padding-top: 8px;
    padding-bottom: 8px;
    display: inline-block;
    float: left;
  }
  input[type=text],input[type=password]{
    font-family: inherit;
    height: 38px;
    line-height: 27px;
    font-size: 16px;
    width: 256px;
    float: right;
    outline:none;
    border: none;
    box-sizing: border-box;
    padding-left: 10px;

  }
  .inputBox{
    margin-top: 25px;
    background: #DDDDDD;

    width: 299px;
    height: 38px;
    overflow: hidden;
    border: 1px solid #DDDDDD;
  }

  .buttom{
    float: right;
    color: #6C6C6C;
    font-size: 12px;
    text-decoration: none;
    margin-left: 10px;
  }

  .inputBox span{
    margin-left: 7px;
  }
  .hintBox{
    height: 25px;
    width: 300px;
    background: #FEF2F2;
  }
  #hint_icon{
    margin: 0px 5px 0px 10px;
    color: #D64848;
    font-size: 15px;
  }
  #hint_msg{
    font-size: 13px;
    color: #6C6C6C;
  }

  #enter{
    width: 300px;
    height: 42px;
    line-height: 42px;
    cursor: pointer;
    background-color: #ff0036;
    color: white;
    border: none;
    border-radius:5px;
    font-size: 16px;
    font-family: -apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";

  }
</style>
