<template>
    <div>
        <div class="box">
          <img :src="show_userPhotoURL">
          <div style="margin-top: 5px;width: 175px;transform: translate(50%);position: absolute;right: 50%">
            <span class="btn btn-info fileinput-button" @click="click_browse">
              <span>浏览</span>
              <input ref="img_input" @change="change" type="file" accept="image/jpeg,image/jpg,image/png,image/svg"  >
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
            imgFile:undefined,
            userPhoto_URL:undefined
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
            this.$refs.img_input.click()
          },
        change(){
          this.imgFile = this.$refs.img_input.files[0];
          var tempThis = this;
          var reader = new FileReader();
          reader.onload = function (e) {
            tempThis.userPhoto_URL = e.target.result;
          };
          reader.readAsDataURL(this.imgFile)
        },
        click_submit(){
          if (undefined == this.imgFile){
            this.$store.state.status = '请先点击“浏览”添加图片';
            return;
          }



          this.$axios.get('images/getQiniuUpTokenByUserId')
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)){

                var tempThis = this;

                var token = res.data.data.token;
                var domain = res.data.data.domain;
                var suffix = this.imgFile.name.substring(this.imgFile.name.lastIndexOf('.'));
                var date = new  Date();
                var h = date.getHours() < 10 ? '0' + date.getHours() : date.getHours();
                var m = date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes();
                var s = date.getSeconds() < 10 ? '0' + date.getSeconds() : date.getSeconds();
                var imgURI = 'userPhoto/'+this.$store.state.user.userId +'_'+date.getFullYear()+date.getMonth()+date.getDate() +h+m+s+suffix;
                var imgURL = domain + imgURI;
                
                var observer = {                         //设置上传过程的监听函数
                  next(result){                        //上传中(result参数带有total字段的 object，包含loaded、total、percent三个属性)
                    Math.floor(result.total.percent);//查看进度[loaded:已上传大小(字节);total:本次上传总大小;percent:当前上传进度(0-100)]
                  },
                  error(err){                          //失败后
                  },
                  complete(res){                       //成功后
                    // ?imageView2/2/h/100：展示缩略图，不加显示原图
                    // ?vframe/jpg/offset/0/w/480/h/360：用于获取视频截图的后缀，0：秒，w：宽，h：高

                    tempThis.$axios.get('/user/changeUserPhotoByUserId',{
                      params:{
                        userPhoto:imgURL
                      }
                    }).then(res=>{
                      tempThis.$store.state.user.userPhoto = imgURL;
                    });
                  }
                };
                var putExtra = {
                  fname: "",                          //原文件名
                  params: {},                         //用来放置自定义变量
                  mimeType: ['image/jpeg','image/jpg','image/png','image/svg'] //限制上传文件类型
                };
                var configccccc = {
                  region:qiniu.region.z2,             //存储区域(z0:代表华东;z2:代表华南,不写默认自动识别)
                  concurrentRequestLimit:3            //分片上传的并发请求量
                };

                var observable = qiniu.upload(this.imgFile,imgURI,token,putExtra,configccccc);
                var subscription = observable.subscribe(observer);          // 上传开始
                // 取消上传
                // subscription.unsubscribe();
              }
            });


          // 后台保存图片

          // let formData = new FormData();
          // formData.append('file', this.imgFile);
          //
          // let config = {
          //   headers:{'Content-Type':'multipart/form-data'}
          // };

          // this.$axios.post('/images/addUserPhotoByUserId', formData, config)
          //   .then(res=>{
          //     if (this.$store.getters.getResultDispose(res)){
          //       var store_userPhoto = this.$store.state.userPhoto;
          //       var userPhoto = res.data.data;
          //       if (undefined == store_userPhoto || '' == store_userPhoto){
          //         this.$axios.get('/user/changeUserPhotoByUserId',{
          //           params:{
          //             userPhoto:userPhoto
          //           }
          //         }).then(res=>{
          //             if (this.$store.getters.getResultDispose(res)){
          //               window.location.reload();
          //             }
          //           });
          //       } else {
          //         window.location.reload();
          //       }
          //     }
          //   });




        },
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

  .fileinput-button{
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
