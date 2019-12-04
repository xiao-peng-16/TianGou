<template>
  <div>

        <div class="fromBox">

          <div class="titleBox"><span>修改密码</span></div>

          <br/><input v-my-focus v-model.lazy="oldUserPassword" @keyup.enter="focusPassword_1" type="password" placeholder="原密码">
          <!--        <span v-show="flag_oldUserPassword" class="iconfont right">&#xed1d;</span>-->
          <div class="hintBox">
            <div v-show="hint_oldUserPassword!=''">
              <span class="hint_icon iconfont">&#xeb65;</span>
              <span class="hint_msg">{{this.hint_oldUserPassword}}</span>
            </div>
          </div>

          <br/><input v-model.lazy="newUserPassword" @keyup.enter="focusPassword_2" ref="Password_1" type="password" placeholder="新密码">
          <span v-show="flag_newUserPassword" class="iconfont right">&#xed1d;</span>
          <div class="hintBox">
            <div v-show="hint_newUserPassword!=''">
              <span class="hint_icon iconfont">&#xeb65;</span>
              <span class="hint_msg">{{this.hint_newUserPassword}}</span>
            </div>
          </div>

          <br/><input v-model.lazy="sureUserPassword" @keyup.enter="changeUserPassword" ref="Password_2" type="password" placeholder="确认密码">
          <span v-show="flag_sureUserPassword" class="iconfont right">&#xed1d;</span>
          <div class="hintBox">
            <div v-show="hint_sureUserPassword!=''">
              <span class="hint_icon iconfont">&#xeb65;</span>
              <span class="hint_msg">{{this.hint_sureUserPassword}}</span>
            </div>
          </div>
          <br/><button id="login" @click="changeUserPassword">修改密码</button>

        </div>


  </div>
</template>

<script>
  export default {
    name: "changeUserPassword",
    data(){
      return{
        oldUserPassword:"",
        newUserPassword:"",
        sureUserPassword:"",

        flag_oldUserPassword:false,
        flag_newUserPassword:false,
        flag_sureUserPassword:false,

        hint_oldUserPassword:"",
        hint_newUserPassword:"",
        hint_sureUserPassword:"",

        //等于0 开始监听
        flag_watch : 0
      }
    },
    watch:{
      oldUserPassword() {
        if (this.flag_watch > 0)
          return (--this.flag_watch);
        this.check_oldUserPassword();
      },
      newUserPassword() {
        if (this.flag_watch > 0)
          return (--this.flag_watch);
        this.check_newUserPassword();
        if (this.sureUserPassword!=""){
          this.check_sureUserPassword();
        }
      },
      sureUserPassword() {
        if (this.flag_watch > 0)
          return (--this.flag_watch);
        this.check_sureUserPassword();
      }
    },
    methods:{
      focusPassword_1(){
        this.$refs.Password_1.focus();
      },
      focusPassword_2(){
        this.$refs.Password_2.focus();
      },
      changeUserPassword(){
        this.check_oldUserPassword();
        this.check_newUserPassword();
        if (this.flag_oldUserPassword && this.flag_newUserPassword && this.flag_sureUserPassword){
          this.$refs.Password_2.blur();
          this.$axios.post('/user/changeUserPasswordByUserId',{
            oldUserPassword : this.oldUserPassword,
            newUserPassword : this.newUserPassword
          }).then(res=>{
            if (res.data.success){
              this.$store.state.status = "亲，您的密码修改成功"
              this.init();
            } else {
              this.$store.getters.getResultDispose(res);
              this.hint_oldUserPassword = "原密码不对";
            }
          });
        }
      },
      check_oldUserPassword(){
        if ("" != this.oldUserPassword){
          this.flag_oldUserPassword = true;
        }else {
          this.flag_oldUserPassword = false;
          this.hint_oldUserPassword = "请输入原密码";
        }
      },
      check_newUserPassword(){
        if (this.newUserPassword.match(/^\w{8,15}$/)){
          this.flag_newUserPassword=true;
          this.hint_newUserPassword = "";
        } else {
          this.flag_newUserPassword=false;
          this.hint_newUserPassword = "密码必须为8~15位的数字，字母，下划线";
        }
        this.check_sureUserPassword();
      },
      check_sureUserPassword(){
        if (this.sureUserPassword===this.newUserPassword){
          this.flag_sureUserPassword= ("" !=this.newUserPassword);
          this.hint_sureUserPassword = "";
        } else{
          this.flag_sureUserPassword=false;
          this.hint_sureUserPassword = "两次密码不一致";
        }
      },
      init(){
        this.flag_watch = 3;

        this.oldUserPassword = "";
        this.newUserPassword = "";
        this.sureUserPassword = "";

        this.flag_oldUserPassword = false;
        this.flag_newUserPassword = false;
        this.flag_sureUserPassword = false;

        this.hint_oldUserPassword = "";
        this.hint_newUserPassword = "";
        this.hint_sureUserPassword = "";
      }

    },


  }
</script>

<style scoped>
  span{
    font-weight: bold;
  }
  .fromBox{
    width: 550px;
    margin-top: 20px;
    margin-left: 26%;
  }
  .titleBox{
    padding-left: 140px;
    height: 100px;
  }
  .titleBox span{
    display: block;
    font-weight: normal;
    font-size: 44px;
    color: black;

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
