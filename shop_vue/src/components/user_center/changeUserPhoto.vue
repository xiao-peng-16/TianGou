<template>
    <div>
        <div class="box">
          <img :src="show_userPhotoURL">
          <el-progress v-show="show_percentage"  type="circle" :percentage="percentage" :width="200"  class="submit_percent"></el-progress>

          <div style="margin-top: 5px;width: 175px;transform: translate(50%);position: absolute;right: 50%">
            <span class="btn btn-info fileinput-button" @click="click_browse">
              <span>浏览</span>
              <el-upload
                action="http://upload-z2.qiniu.com"
                :data="postData"
                :auto-upload="false"
                :show-file-list="false"
                :on-success="success_submit"
                :on-change="change"
                :on-progress="progress"
                accept=".jpg,.jpeg,.png"
                ref="submit">
                <div ref="upload"></div>
              </el-upload>


            </span>
            <span class="btn btn-success fileinput-button" @click="click_submit">提交</span>
          </div>

        </div>
    </div>
</template>

<script>
  var qiniu = require('qiniu-js')

  export default {
        name: "changeUserPhoto",
      data(){
          return{
            allow_change:true,
            imgFile:undefined,
            userPhoto_URL:undefined,


            postData:{
              token: '',
              key:'',
            },
            imgURL:'',
            percentage:0,
            show_percentage:false
          }
      },
      computed:{
        show_userPhotoURL(){
          if (undefined == this.userPhoto_URL)
            return this.$store.getters.getUserPhotoURL();
          else {
            return this.userPhoto_URL;
          }
        }
      },
      methods:{


        click_browse(){
          if (!this.allow_change){
            this.$notify.warning({
              title:'图片上传请稍后'
            });
            return;
          }
            this.$refs.upload.click()
          },
        change(file,filelist){
          if ("ready" == file.status)
            this.imgFile = file.raw;
          filelist.splice(0,filelist.length-1)
          var tempThis = this;
          var reader = new FileReader();
          reader.onload = function (e) {
            tempThis.userPhoto_URL = e.target.result;
          };
          reader.readAsDataURL(this.imgFile)
        },
        click_submit(){
          if (!this.allow_change){
            this.$notify.warning({
              title:'图片上传请稍后'
            });
            return;
          }
          if (undefined == this.imgFile){
            this.$notify.warning({
              title:'请先点击“浏览”添加图片'
            });
            return;
          }

          this.allow_change = false;
          this.$axios.get('images/getQiniuUpTokenByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){

                this.postData.token = res.data.data.token;
                var domain = res.data.data.domain;
                var suffix = this.imgFile.name.substring(this.imgFile.name.lastIndexOf('.'));
                var date = new  Date();
                var h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                var m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                var s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                this.postData.key = 'userPhoto/'+this.$store.state.user.userId +'_'+date.getFullYear()+date.getMonth()+date.getDate() +h+m+s+suffix;
                this.imgURL = domain + this.postData.key;

                this.percentage=0;
                this.show_percentage=true;
                this.$refs.submit.submit();
              }
            });


        },
        success_submit(){
          this.$axios.get('/user/changeUserPhotoByUserId',{
            params:{
              userPhoto:this.imgURL
            }
          }).then(res=>{
            console.log(res)
            if (this.$store.getters.getResultDispose(res)) {
              if (res.data){
                this.percentage = 100;
                this.allow_change = true;
                this.$store.state.user.userPhoto = this.imgURL;
                this.imgFile = undefined;
                this.$notify.success({
                  title:'头像更换成功',
                });
                this.show_percentage=false;
              }
            }
          });
        },
        progress(event,file,filelist){
          this.percentage = parseInt(file.percentage.toFixed(0));
        }

      }
    }
</script>

<style scoped>
  .box{

    margin-left: 28%;
    width: 300px;
    position: relative;
  }

  img{
    width: 300px;
    height: 300px;
  }
  .submit_percent{
    position: absolute;
    left: 150px;
    top: 150px;
    transform: translate(-50%,-50%);
  }
  .el-progress__text{

    color: aqua;
    font-size: 25px!important;
  }

  .fileinput-button{
    font-size: 18px;
    color: white;
    cursor: pointer;
    border-radius: 4px;
    width: 85px;
    height: 40px;
  }
  .fileinput-button input{
    display: none;
  }
  span{
    font-size: 18px;
  }

</style>
