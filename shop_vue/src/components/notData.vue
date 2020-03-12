<template>
  <div class="notData">
    <img src="../assets/search_notdata.png">
    <div style="padding-top: 30px">
      {{left}}
      <span class="msg_middle">{{middle}}</span>
      {{right}}
    </div>
  </div>
</template>

<script>
    export default {
        name: "notData",
        props:['left','middle','right']
    }
</script>

<style scoped>

  .notData{
    width: 98%;
    margin: 0px auto;
    background: #FFF8F6;
    border: 1px solid #F7EAE7;
    height: 90px;
    padding-left: 30%;
    box-sizing: border-box;
  }

  .notData img{
    height: 60px;
    margin: 15px;
    float: left;
  }
  .msg_middle{
    color: #790103;
    font-weight: bold;
  }
</style>
