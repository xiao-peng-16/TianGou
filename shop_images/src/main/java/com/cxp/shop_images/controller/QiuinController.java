package com.cxp.shop_images.controller;



/*
       图片  保存 七牛云
 */

import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_images.pojo.QiniuUpToken;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(allowCredentials = "true")
@RestController
public class QiuinController {



    @Value("${bucket}")
    String bucket;

    @Value("${domain}")
    String domain;

    //身份验证
    @Autowired
    Auth auth;
    //删除
    @Autowired
    BucketManager bucketManager;

    //获取上传凭证
    @RequestMapping("/getQiniuUpTokenByUserId")
    @ResponseBody
    public ResultBean getQiniuUpTokenByUserId(){

        //生成凭证
        String upToken = auth.uploadToken(bucket);
        return ResultFactory.createSuccessResult(new QiniuUpToken(upToken, domain));
    }


    //删除图片
    @RequestMapping("/dlelQiniuImages")
    public void  dlelQiniuImages(String imagesURl){

        if (imagesURl.contains(domain)){
            String key = imagesURl.replace(domain,"");
            try {
                bucketManager.delete(bucket, key);
            } catch (QiniuException e) {
                e.printStackTrace();
                Response r = e.response;
                System.out.println(r.toString());
            }
        }
    }


}
