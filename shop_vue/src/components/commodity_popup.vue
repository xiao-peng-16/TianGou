<template>
  <div id="popupBoxBox">

    <!--      弹窗-->
    <div id="background" v-show="flag_commodity_popup"></div>
    <!--      不用v-show  用v-if  这样每次都重新渲染 保证滚动条在顶部-->
    <div id="popupBox"  v-if="flag_commodity_popup" v-loading="flag_uploading"
         element-loading-text="商品上传中 , 请稍等"
         element-loading-spinner="el-icon-loading"
         element-loading-background="rgba(100, 100, 100,0.3)">

      <div id="popup">


        <div style="padding: 5px 3px 0px 5px">
          <div style="float: left;color: black;font-weight: 600;font-size: 14px">
            <span v-if="flag_add" style="">添加商品</span>
            <span v-else>修改商品</span>
          </div>

          <div style="float:right;cursor: pointer" @click="leverOrderParent">
            <i class="el-icon-close" style="font-size: 23px"></i>
          </div>
        </div>



        <div class="top">
          <div :class="{close_card:flag_close_card}">
            <el-upload
              action="http://upload-z2.qiniu.com"
              list-type="picture-card"
              :on-success="img_success_upload"
              :before-upload="img_beforeAvatarUpload"
              :data="postData"
              :file-list="img_fileList"
              :on-preview="img_preview"
              :auto-upload="false"
              :on-change="img_change"
              :on-remove="remove"
              accept=".jpg,.jpeg,.png"
              :limit="1"
              ref="img_upload"
              class="primary_commodityPrice">
              <i class="el-icon-plus"></i>
            </el-upload>
            <el-dialog :visible.sync="flag_img_dialog" :append-to-body="true">
              <img width="100%" :src="dialogImageUrl" alt="" ref="ccc">
            </el-dialog>
          </div>

          <div class="top_right">
            <div class="commodityText commodityName">
              商品标题
              <el-input
                type="textarea"
                placeholder="商品标题"
                v-model="commodity.commodityName"
                maxlength="40"
                show-word-limit
                :autosize="{minRows:1,maxRows:2}"
                class="input_text">
              </el-input>
            </div>


            <div class="commodityText commodityDescribe">
              商品描述
              <el-input
                type="textarea"
                placeholder="商品描述信息"
                v-model="commodity.commodityDescribe"
                maxlength="50"
                show-word-limit
                :autosize="{minRows: 3,maxRows: 3}"
                class="input_text">
              </el-input>
            </div>
          </div>
        </div>



        <div class="middle">

          <div class="middle_left">
            <el-upload
              action="http://upload-z2.qiniu.com"
              :on-success="video_success_upload"
              :data="postData"
              :file-list="video_fileList"
              :before-upload="video_beforeAvatarUpload"
              :on-preview="video_preview"
              :on-change="video_change"
              :auto-upload="false"
              accept=".mp4,.avi"
              :limit="1"
              ref="video_upload">
              <el-button size="small" type="primary">上传视频</el-button>
              <div slot="tip" class="el-upload__tip">只能上传一个avi/mp4文件，且不超过50Mb</div>
            </el-upload>
            <el-dialog :visible.sync="flag_video_dialog" :append-to-body="true">
              <div style="width: 100%" v-if="flag_video_dialog"><video_component :resource="dialogVideoResource"/></div>
            </el-dialog>
          </div>


          <div class="middle_right">


            <div>
              <div class="property_lable">包邮</div>
              <el-switch
                v-model="commodity.baoYou"
                active-color="#13ce66">
              </el-switch>
            </div>


            <div>
              <div class="property_lable">商品种类</div>
              <el-select v-model="commodity.sortId">
                <el-option
                  v-for="item in sortlist"
                  :label="item.sortName"
                  :value="item.sortId">
                </el-option>
              </el-select>
            </div>

            <div>
              <div class="property_lable">商品价格</div>
              <el-input-number
                :min="0"
                :max="999999"
                :precision="2"
                v-model="commodity.commodityPrice"
                :step="5">
              </el-input-number>
            </div>

            <div>
              <div class="property_lable">商品库存</div>
              <el-input-number
                :min="0"
                :max="100000000"
                :precision="0"
                v-model="commodity.commodityStock"
                :step="100">
              </el-input-number>
            </div>
          </div>

        </div>








<!--<div style="position: absolute;bottom: 50px"> {{commodity}}</div>-->

        
        
        <div style="position: absolute;bottom: 5px;right: 5px">
          <el-button v-if="flag_add" @click="submit_1" type="primary">添加商品</el-button>
          <el-button v-else @click="submit_1" type="success">修改商品</el-button>
        </div>


      </div>
    </div>
  </div>
</template>

<script>
  import Video_component from "@/components/video_component";
  export default {
    name: "commodity_popup",
    props:['refresh_popup_flag','commodityId'],
    components: {Video_component},
    data(){
      return{
        flag_commodity_popup:false,
        flag_close_card:false,
        //上传中
        flag_uploading:false,
        sortlist:undefined,

        img_fileList:[],
        video_fileList:[],

        commodity:{
          commodityName:undefined,
          commodityDescribe:undefined,
          sortId:undefined,
          baoYou:false,
          commodityPrice:0,
          commodityStock:0,
          commodityPhoto:undefined,
          commodityVideo:undefined,
        },

        flag_img_dialog:false,
        dialogImageUrl:undefined,

        flag_video_dialog:false,
        dialogVideoResource:{
          poster:undefined,
          src:undefined
        },

        domain:undefined,
        postData: {
          token: undefined,
          key:undefined
        },





      }
    },
    computed:{
        flag_add(){
          return 1 == parseInt(this.$route.query.ls);
        }
    },
    watch:{
      refresh_popup_flag(val){

        // -1 外面关闭
        if (-1 == val){
          this.leverOrderParent();
          return;
        }
        //获取种类列表
        if (undefined == this.sortlist){
          this.$axios.get('/commodity/listSort')
            .then(res=>{
            this.sortlist = res.data;
          });
        }

        //获取商品原本数据 在修改
        if (!this.flag_add){
            if (undefined == this.commodityId){
              alert("propsd 的 commodityId 为空")
              return;
            }
            this.$axios.get('/commodity/selCommodityByCommodityId',{
              params:{
                commodityId:this.commodityId
              }
            }).then(res=>{
              this.commodity = res.data;
              if ('' != this.commodity.commodityPhoto){
                this.img_fileList.push({name: '', url: this.commodity.commodityPhoto})
                this.flag_close_card = true;
              }
              var commodityVideo = this.commodity.commodityVideo;
              if ('' != commodityVideo)
                this.video_fileList.push({name: commodityVideo.substring(commodityVideo.lastIndexOf('/')+1), url: commodityVideo})
            });
        }
        this.flag_commodity_popup = true;
      }
    },
    methods:{


      leverOrderParent(){
        this.commodity={
          commodityName:undefined,
          commodityDescribe:undefined,
          commodityPrice:0,
          commodityStock:0,
        };
        this.img_fileList=[];
        this.video_fileList=[];
        this.flag_uploading = false;
        this.flag_commodity_popup = false;
        this.flag_close_card = false;
        this.$router.push({name:'store_center',query:{
            l:this.$route.query.l,
            ls:0
          }});
      },


      img_change(file,filelist){
        if (file.size > 2 * 1204 * 1024){
          filelist.splice(0,1);
          this.$message.error('上传的图片大小不能超过 2MB!');
        }
        if (0 < filelist.length)
          this.flag_close_card = true;
      },
      remove(){
        this.flag_close_card = false;
      },
      video_change(file,filelist) {
        if (file.size > 50*1024*1024){
          filelist.splice(0,1);
          this.$message.error('上传视频大小不能超过 50MB!');
        }
      },
      img_preview(file){
        // var tempThis = this ;
        // var reader = new FileReader();
        // reader.onload=function (e) {
        //   tempThis.dialogImageUrl = e.target.result;
        //   tempThis.flag_img_dialog = true;
        // }
        // reader.readAsDataURL(file.raw);

        this.dialogImageUrl = file.url;
        this.flag_img_dialog = true;
      },
      video_preview(file){
        if ("success" == file.status){
          this.dialogVideoResource.src = file.url;
          this.flag_video_dialog = true;
        }else {
          //这个没有file.url
          var tempThis = this ;
          var reader = new FileReader();
          reader.onload=function (e) {
            tempThis.dialogVideoResource.src = e.target.result;
            tempThis.flag_video_dialog = true;
          }
          reader.readAsDataURL(file.raw);
        }
      },
      img_beforeAvatarUpload(file){
        this.postData.key = 'commodity/images/'+ this.$store.state.user.userId + (-new Date()) + file.name.substring(file.name.lastIndexOf('.'))
      },
      video_beforeAvatarUpload(file){
        this.postData.key = 'commodity/video/'+ this.$store.state.user.userId + (-new Date()) + file.name.substring(file.name.lastIndexOf('.'))
      },





      //***************************点击右下角按钮**********************************************************

      is_all_success(){
        var img_filelist= this.$refs.img_upload.uploadFiles;
        for(var i=0; i <img_filelist.length;i++)
          if("success" != img_filelist[i].status)
            return false;

        var video_filelist = this.$refs.video_upload.uploadFiles;
        for(var i=0; i <video_filelist.length;i++)
          if("success" != video_filelist[i].status)
            return false;
        return true;
      },
      img_success_upload(res, file,fileList) {   //上传成功后在图片框显示图片
        this.commodity.commodityPhoto = this.domain +  res.key;

        //如果没有区别上传成功 就return 等下次
        if (this.is_all_success())
          this.submit_2();
      },
      video_success_upload(res, file,fileList) {   //上传成功后在图片框显示图片
        this.commodity.commodityVideo = this.domain +  res.key;

        //如果没有区别上传成功 就return 等下次
        if (this.is_all_success())
          this.submit_2();
      },
      submit_1(){

        if (undefined == this.commodity.commodityName || '' == this.commodity.commodityName){
          this.$message.error('请输入商品标题');
          return;
        }
        if (0 == this.$refs.img_upload.uploadFiles.length){
          this.$message.error('请添加商品图片');
          return;
        }
        this.flag_uploading = true;

        //需要先上传图片、视频获取图片视频 url
        if (this.is_all_success()){
          this.submit_2();
        }else {
          this.$axios.get('images/getQiniuUpTokenByUserId')
            .then(res=> {
              if (this.$store.getters.getResultDispose(res)) {
                this.postData.token = res.data.data.token;
                this.domain = res.data.data.domain;

                this.$refs.img_upload.submit();
                this.$refs.video_upload.submit();
              }
            });
        }
      },
      //第二步时 所有数据已齐全  可以axios
      submit_2(){

        if (this.flag_add){
          this.$axios.post('/commodity/addCommodityByUserId',this.commodity)
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)) {
                  this.$emit('refresh_commodity_list','商品提交成功')
              }
            })
        } else {
          this.$axios.post('/commodity/updCommodityByUserId',this.commodity)
            .then(res=>{
              if (this.$store.getters.getResultDispose(res)) {
                this.$emit('refresh_commodity_list','商品修改成功')
              }
            })
        }

      }





    }
  }
</script>

<style>

  #popupBoxBox .el-loading-spinner{
    top: 35%;
    font-size: 30px;
  }
  #popupBoxBox .el-loading-text{
    font-size: 25px;
  }
  

  #background {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0,0,0,0.5);
    z-index: 1000;
  }

  #popupBox {
    position: fixed;
    top: 60px;
    left: 52%;
    transform: translate(-50%);
    z-index: 1001;
  }

  #popup{
    box-shadow: 0px 0px 3px #ddd;
    border: #ebebeb solid 1px;
    background: #F8F8F8;
    border-radius: 5px;
    position: relative;
    width: 730px;
    height: 600px;
    margin: 0px auto;
    /*overflow:scroll;*/
    overflow-x: hidden;
  }
  #popup div{
    background: #F8F8F8;
  }


  .top{
    margin: 40px 20px 0px 25px;
    height: 220px;
    position: relative;
  }
  .top_right{
    position: absolute;
    right: 0px;
    top: 0px;
  }

  .commodityText{
    width: 450px;
    font-size: 18px;
  }
  .input_text{
    font-size: 18px;
    overflow: hidden;
  }
  .commodityName{
    height: 90px;
  }
  .commodityName .input_text{
    max-height: 65px;
  }

  .commodityDescribe{
    
  }
  .commodityDescribe .input_text{
    max-height: 95px;
  }
  
  .primary_commodityPrice{
    position: absolute;
    left: 0px;
  }

  .primary_commodityPrice .el-upload--picture-card,.primary_commodityPrice .is-ready,.primary_commodityPrice .is-success,primary_commodityPrice .is-uploading{
    width: 207px;
    height: 207px;
  }
  .primary_commodityPrice .el-icon-plus{
    margin-top: 80px;
  }

  .close_card .el-upload--picture-card{
    display: none;
  }


  .middle{
    margin: 15px 35px 0px 25px;
    position: relative;
  }

  .middle_left{
    position: absolute;
    left: 0px;
    width: 300px;
  }
  .el-upload-list__item-name{
    cursor: pointer;
  }


  .middle_right{
    position: absolute;
    right: 0px;
  }
  .middle_right div{
    margin-bottom: 4px;
  }
  .middle_right .property_lable{
    display: inline-block;
    width: 100px;
  }

</style>
