import defaultHead from "@/assets/defaultHead.jpg";

export default {
  // Vue.js的插件应当有一个公开方法 install。这个方法的第一个参数是 Vue 构造器，第二个参数是一个可选的选项对象。
  install: function (Vue) {

    Vue.prototype.user_Login = (token) => user_Login(token);
    Vue.prototype.getUserPhotoURL = (user) => getUserPhotoURL(user);
  }
}



  function user_Login (token) {
    localStorage.setItem("token",token);
  }

  function getUserPhotoURL(user) {
    if (undefined == user || undefined == user.userPhoto || '' == user.userPhoto)
      return defaultHead;
    else
      return user.userPhoto;
  }
