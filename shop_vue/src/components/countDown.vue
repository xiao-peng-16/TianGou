<template>
    <div>
      <div class="countdown">
        <p class="miao_sha">京东秒杀</p>
        <img src="../assets/countdown_bj.png">
        <p class="time_top"><span>{{target_hour}}:00</span>点场 倒计时</p>
        <div class="time_countdown">
          <div>{{hour}}</div>
          :<div>{{minute}}</div>
          :<div>{{second}}</div>
        </div>
      </div>
    </div>
</template>

<script>
    export default {
        name: "countdown",
      data(){
          return{
            target_hour:'00',
            hour:'00',
            minute:'00',
            second:'00',
          }
      },
      created() {
        var target_hour = 21;
        this.target_hour = target_hour;
        var tarData = new Date();
        tarData.setHours(target_hour,0,0);
        // 加号 把tarData 转为多少毫秒
        var tar = +tarData;

        let tempThis = this;
        this.timer = setInterval(function () {
          var nowData = new Date();
          var times = (tar - nowData);
          if (0 < times){
            var h = target_hour - nowData.getHours()
            tempThis.hour = h < 10 ? '0' + h : h;
            var m = 60 - nowData.getMinutes();
            tempThis.minute = m < 10 ? '0'+ m : m;
            var s = 60 - nowData.getSeconds();
            tempThis.second = s < 10 ? '0' + s: s;
          } else {
            tempThis.hour ="00";
            tempThis.minute ="00";
            tempThis.second ="00";

          }
        },1000);
      }

    }
</script>

<style scoped>


  .countdown{
    position: relative;
    color: white;
    font: 30px/1.5 Microsoft YaHei,Heiti SC,tahoma,arial,Hiragino Sans GB,"\5B8B\4F53",sans-serif;
  }
  .countdown p{
    width: 100%;
    text-align: center;

  }
  .miao_sha{
    position: absolute;
    top: 25px;
    font-weight: bold;
  }
  .countdown img{
    width: 100%;
  }
  .time_top{
    position: absolute;
    bottom: 75px;
    font-size: 14px;
  }
  .time_top span{
    font-size: 18px;
    font-weight: bold;
    margin-right: 1px;
    margin-top: 50px;
    position: relative;
    top: 2px;
  }
  .time_countdown{
    position: absolute;
    bottom: 30px;
    width: 130px;
    left: 50%;
    transform: translate(-50%);
  }
  .time_countdown div{
    display: inline-block;
    width: 30px;
    height: 30px;
    text-align: center;
    background: #2F3430;
    margin-left: 4px;
    margin-right: -5px;
    font-size: 20px;
    font-weight: 700;
  }
</style>
