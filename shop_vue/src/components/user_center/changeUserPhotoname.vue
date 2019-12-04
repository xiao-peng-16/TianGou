<template>
    <div>
        <div class="box">
          <img :src="show_userPhotonameURL">
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
    export default {
        name: "changeUserPhotoname",
      data(){
          return{
            imgFile:undefined,
            userPhotoname_URL:undefined
          }
      },
      computed:{
        show_userPhotonameURL(){
          if (undefined == this.userPhotoname_URL)
            return this.$store.getters.getUserPhotonameURL();
          else {
            return this.userPhotoname_URL;
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
            tempThis.userPhotoname_URL = e.target.result;
          };
          reader.readAsDataURL(this.imgFile)
        },
        click_submit(){
          if (undefined == this.imgFile){
            this.$store.state.status = '请先点击“浏览”添加图片'
          }else {


            this.imgFile = undefined;
          }
        }
      }
    }
</script>

<style scoped>
  .box{

    margin-left: 25%;
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
