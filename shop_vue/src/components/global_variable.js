

export default {

  baseURL: "http://localhost:7010",
  userPhotonameUrl: "http://localhost/TianGou/resource/userPhotoname/",
  commodityImagesUrl: "http://localhost/TianGou/resource/commodity/images/",
  commodityVideoUrl: "http://localhost/TianGou/resource/commodity/video/",

  store_center_background: '#F8F8F8',


  ResultStatus: {
    //user 2xx
    USER_NAME_DISABLED : 201,           //用户名不可用  重复了
    USER_Add_ERROR : 202,               //用户注册失败
    USER_LOGIN_ERROR : 203,             //用户登录失败
    USER_CHANGE_PASSWORD_ERROR : 204,   //修改密码失败
    USER_ID_ERROR : 205,                //账号ID不存在
    USER_ID_LOGIN_OVERDUE : 206,        //用户过期或未登录
    USER_MONEY_INSUFFICIENT : 207,      //买家购物余额不足
    USER_MONEY_CHANGE_ERROE : 208,      //用户余额改变失败

    //店铺 3xx
    STORE_NOT_QUALIFICATION : 301,      //未开通 卖家功能
    STORE_ID_ERROR : 302,               //店铺ID不存在
    STORE_EQUAL_USER_ERROR : 303,       //用户买自己店铺的商品


    //商品 4xx
    COMMODITY_ID_ERROR : 401,           //商品ID不存在
    COMMODITY_STOCK_INSUFFICIENT : 402, //商品库存不足买家购买
    //订单5xx
    ORDER_List_ERROR : 501,             //订单添加失败
    ORDER_Add_ERROR : 502,              //订单添加失败
    ORDER_ID_ERROR : 503,               //订单id不对 或 改订单已经支付
    ORDER_ID_USER_ID_MISMATCHING : 504, //订单id 和 买家id不匹配
    ORDER_SUBMIT_ERROR : 505,           //订单支付错误


    //数据库添加失败  6xx
    COLLECT_Add_ERROR : 601,      //收藏夹添加失败
    SHOPCAR_Add_ERROR : 603,      //购物车添加失败


  }

}
