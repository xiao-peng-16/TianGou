package com.cxp.shop_images.controller;

import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_images.config.ImagesUrlPrifixBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

/*
       图片
 */

@CrossOrigin(allowCredentials = "true")
@RestController
public class ImagesController {

    @Resource
    ImagesUrlPrifixBean imagesUrlPrifixBean;

    @Value("${UserPhotoLocalPath}")
    String UserPhotoLocalPath;

    @Value("${CommodityPhotoLocalPath}")
    String CommodityPhotoLocalPath;

    @Value("${CommodityVideoLocalPath}")
    String CommodityVideoLocalPath;

    static final ResultBean USER_PHOTO_UPLOAD_ERROE = ResultFactory.createFailResult(ResultStatus.USER_PHOTO_UPLOAD_ERROE);


    @RequestMapping("/addUserPhotoByUserId")
    public ResultBean  addUserPhotoByUserId(Integer userId,  MultipartFile file){

        System.out.println(file.getOriginalFilename());
        String targetFileName = uploadImages(file, UserPhotoLocalPath, userId);
        if (null == targetFileName)
            return USER_PHOTO_UPLOAD_ERROE;
        return ResultFactory.createSuccessResult(imagesUrlPrifixBean.getUserPhotoUrlPrifix() + targetFileName);
    }


    @RequestMapping("/addCommodityPhotoByUserId")
    public ResultBean  addCommodityPhotoByUserId(MultipartFile file){

        String targetFileName = uploadImages(file, CommodityPhotoLocalPath, UUID.randomUUID());
        if (null == targetFileName)
            return USER_PHOTO_UPLOAD_ERROE;
        return ResultFactory.createSuccessResult(imagesUrlPrifixBean.getCommodityPhotoUrlPrifix() + targetFileName);
    }




    public String uploadImages(MultipartFile file, String localPath, Object fileNamePrifix){
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        if ( !(".jpg".equals(suffixName) || ".jpeg".equals(suffixName) || ".png".equals(suffixName) || ".svg".equals(suffixName)) )
            return null;

        String targetFileName = fileNamePrifix + suffixName;
        File dest = new File(localPath + targetFileName);
        if (!dest.getParentFile().exists()){
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return targetFileName;
    }

}
