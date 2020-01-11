<template>
  <div>
    <div class="bj"><img src="http://q2wh9mmyk.bkt.clouddn.com/vue/register_bj.jpgd"></div>
      <div class="fromBox">

        <div class="titleBox">
          <span id="first">欢迎注册天狗</span>
          <span id="second"> 每一天，乐在消费</span>
        </div>

        <br/><input v-my-focus v-model.lazy="userName" @keyup.enter="focusPassword_1" type="text" placeholder="昵称">
        <span v-show="flag_userName" class="iconfont right">&#xed1d;</span>
        <div class="hintBox">
          <div v-if="hint_userName!=''">
            <span class="hint_icon iconfont">&#xeb65;</span>
            <span class="hint_msg">{{this.hint_userName}}</span>
          </div>
        </div>

        <br/><input v-model.lazy="userPassword" @keyup.enter="focusPassword_2" ref="Password_1" type="password" placeholder="密码">
        <span v-show="flag_userPassword" class="iconfont right">&#xed1d;</span>
        <div class="hintBox">
          <div v-if="hint_userPassword!=''">
            <span class="hint_icon iconfont">&#xeb65;</span>
            <span class="hint_msg">{{this.hint_userPassword}}</span>
          </div>
        </div>

        <br/><input v-model.lazy="sureUserPassword" @keyup.enter="addUser" ref="Password_2" type="password" placeholder="确认密码">
        <span v-show="flag_sureUserPassword" class="iconfont right">&#xed1d;</span>
        <div class="hintBox">
          <div v-if="hint_sureUserPassword!=''">
            <span class="hint_icon iconfont">&#xeb65;</span>
            <span class="hint_msg">{{this.hint_sureUserPassword}}</span>
          </div>
        </div>

        <br/><button id="login" @click="addUser">注册</button>


      </div>

  </div>
</template>

<script>
  export default {
    name: "register",
    data(){
      return{
        userName:"",
        userPassword:"",
        sureUserPassword:"",
        flag_userName:false,
        flag_userPassword:false,
        flag_sureUserPassword:false,
        hint_userName:"",
        hint_userPassword:"",
        hint_sureUserPassword:""
      }
    },
    methods:{
      focusPassword_1(){
        this.$refs.Password_1.focus();
      },
      focusPassword_2(){
        this.$refs.Password_2.focus();
      },
      addUser(){
        this.check_userName();
        this.check_userPassword();
        if (this.flag_userName && this.userPassword && this.sureUserPassword){

          this.$axios.post('/user/addUserAndGetToken',{
            userName : this.userName,
            userPassword : this.userPassword
          }).then(res=>{
            if (res.data.success){
              this.user_Login(res.data.data);
              this.$router.go(-1);
              if (this.$router.options.routes)
              this.$router.push({name:'home'});//防止没上一级页面
            } else {
              if (res.data.status == this.GLOBAL.ResultStatus.USER_NAME_DISABLED){
                this.flag_userName = false;
                this.hint_userName="该用户名已存在";
              }
            }
          })
        }
      },
      check_userName(){
        if (this.userName.length<3 || this.userName.length>10){
          this.flag_userName = false;
          this.hint_userName="用户名请长度在3~10之间";
        } else {

          this.$axios.get('/user/is_usable_userName',{
            params:{
              userName : this.userName
            }
          }).then(res=>{
            this.flag_userName = res.data.success;
            if (res.data.success) {
              this.flag_userName = true;
              this.hint_userName="";
            }else {
              this.flag_userName = false;
              this.hint_userName="该用户名已存在";
            }
          })
        }
      },
      check_userPassword(){
        if (this.userPassword.match(/^\w{8,15}$/)){
          this.flag_userPassword=true;
          this.hint_userPassword = "";
        } else {
          this.flag_userPassword=false;
          this.hint_userPassword = "密码必须为8~15位的数字，字母，下划线";
        }
        this.check_sureUserPassword();
      },
      check_sureUserPassword(){
        if (this.sureUserPassword===this.userPassword){
          this.flag_sureUserPassword= ("" == this.userPassword.match);
          this.hint_sureUserPassword = "";
        } else{
          this.flag_sureUserPassword=false;
          this.hint_sureUserPassword = "两次密码不一致";
        }
      }
    },
    watch:{
      userName:function () {
        this.check_userName();
      },
      userPassword:function () {
        this.check_userPassword();
        if (this.sureUserPassword!=""){
          this.check_sureUserPassword();
        }
      },
      sureUserPassword:function () {
        this.check_sureUserPassword();
      }
    }

  }
</script>

<style scoped>
  span{
    font-weight: bold;
  }
  .bj{
    float: left;
    background-repeat:no-repeat ;
  }
  .bj img{
    width: 425px;
  }

  .fromBox{
    background: white;
    height: 600px;
    width: 550px;
    position: relative;
    right: 10px;
    top: 90px;
    margin-left: 750px;
  }
  .titleBox{
    width: 500px;
    height: 110px;
    margin-bottom: 50px;
  }
  .titleBox span{
    display: block;
    font-weight: normal;
  }
  #first{
    font-size: 44px;
    margin-bottom: 20px;
    color: black;
  }
  #second{
    font-size: 28px;
    margin-bottom: 64px;
    line-height: 1.2;
    color: #333;
    margin-left: 20px;
  }
  input[type=text],input[type=password]{
    border: 1px #aaa solid;
    height: 50px;
    line-height: 50px;
    font-size: 20px;
    /*margin-top: 20px;*/
    border-radius: 4px;
    padding: 0 20px;
    width: 480px;
  }
  #login{
    width: 480px;
    height: 60px;
    line-height: 42px;
    background-color: #3587FF;
    color: white;
    border: none;
    border-radius:5px;
  }
  .hint_icon{
    margin: 0px 5px 0px 10px;
    color: #FF5B5B;
    font-size: 15px;
  }
  .hint_msg{
    color: #FF5B5B;
  }
  .hintBox{
    height: 5px;
    width: 480px;
  }
  .right{
    color: #6EF87E;
    font-size: 30px;
  }
</style>
