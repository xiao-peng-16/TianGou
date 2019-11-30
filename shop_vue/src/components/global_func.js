import globalVariable from '@/components/global_variable'
export default {
  // Vue.js的插件应当有一个公开方法 install。这个方法的第一个参数是 Vue 构造器，第二个参数是一个可选的选项对象。
  install: function (Vue) {

    Vue.prototype.addimagePrefixUrl = (obj) => addimagePrefixUrl(obj);
    Vue.prototype.returnCommodityImageURL = (commodityPhotoname) => returnCommodityImageURL(commodityPhotoname);
    Vue.prototype.user_Login = (token) => user_Login(token);
  }
}

  //这两个不要同时用
  function addimagePrefixUrl(obj) {
    obj.commodityPhotoname = globalVariable.commodityImagesUrl + obj.commodityPhotoname;
  }
  function returnCommodityImageURL(commodityPhotoname){
    return globalVariable.commodityImagesUrl + commodityPhotoname;
  }


  function user_Login (token) {
    localStorage.setItem("token",token);
  }

