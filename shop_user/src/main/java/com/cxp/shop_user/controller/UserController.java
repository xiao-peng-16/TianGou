package com.cxp.shop_user.controller;

import com.cxp.shop_api.dto.UserMoneyChange;
import com.cxp.shop_api.entity.User;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_user.exception.MoneyInsufficientException;
import com.cxp.shop_user.exception.TransactionalException;
import com.cxp.shop_user.pojo.ChangeUserPassword;
import com.cxp.shop_user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@CrossOrigin(allowCredentials = "true")
@RestController
public class UserController {

    @Autowired
    UserServiceImpl userService;
    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean USER_PASSWORD_CHANGE_ERROR = ResultFactory.createFailResult(ResultStatus.USER_PASSWORD_CHANGE_ERROR);
    static final ResultBean USER_MONEY_INSUFFICIENT = ResultFactory.createFailResult(ResultStatus.USER_MONEY_INSUFFICIENT);
    static final ResultBean USER_MONEY_CHANGE_ERROE = ResultFactory.createFailResult(ResultStatus.USER_MONEY_CHANGE_ERROE);
    static final ResultBean USER_PHOTO_CHANGE_ERROE = ResultFactory.createFailResult(ResultStatus.USER_PHOTO_CHANGE_ERROE);


    //判断用户名是否可以注册 (防止已经有人注册过了)
    @RequestMapping("/is_usable_userName")
    public boolean is_usable_userName(String userName) {
        return userService.is_usable_userName(userName);
    }


    //创建用户 并登录用户返回token
    @PostMapping("/addUserAndGetToken")
    public ResultBean addUserAndGetToken(@RequestBody User user) {
        return userService.addUserAndGetToken(user);
    }

    //账号 密码登录
    @PostMapping("/getTokenByPassword")
    public ResultBean getTokenByPassword(@RequestBody User userSrc) {
        return userService.getTokenByPassword(userSrc);
    }

    //根据session 的id 返回用户
    @RequestMapping("/getUserByUserId")
    public User getUserByUserId(Integer userId) {
        return userService.getUserByUserId(userId);
    }

    //查询一组用户名 根据id
    @PostMapping("/mapUserNameByUserId")
    public Map<Integer,String> mapUserNameByUserId(@RequestBody List<Integer> userIdList){
        return userService.mapUserNameByUserId(userIdList);
    }

    //查询用户名 根据id
    @RequestMapping("/getUserNameByUserId")
    public String  getUserNameByUserId(Integer userId){
        return userService.getUserNameByUserId(userId);
    }

    //修改密码
    @PostMapping("/changeUserPasswordByUserId")
    public ResultBean  changeUserPasswordByUserId(Integer userId, @RequestBody ChangeUserPassword changeUserPassword){
        return userService.changeUserPasswordByUserId(userId, changeUserPassword)? successResult : USER_PASSWORD_CHANGE_ERROR;
    }


    //查询用户头像
    @RequestMapping("/getUserPhotoByUserId")
    public String  getUserPhotoByUserId(Integer userId){
        return userService.getUserPhotoByUserId(userId);
    }

    //更换头像
    @RequestMapping("/changeUserPhotoByUserId")
    public boolean  changeUserPhotoByUserId(Integer userId, String userPhoto){
        return userService.changeUserPhotoByUserId(userId, userPhoto);
    }



    // 购物转账  第一个是付钱方  剩下的都是收钱方
    @PostMapping("/shopTransferByUserId")
    public ResultBean shopTransferByUserId(Integer userId, @RequestBody LinkedList<UserMoneyChange> userMoneyChangeList){
        try {
            userService.shopTransferByUserId(userId, userMoneyChangeList);
        } catch (MoneyInsufficientException e) {
            return USER_MONEY_INSUFFICIENT;
        } catch (TransactionalException e) {
            return USER_MONEY_CHANGE_ERROE;
        }
        return successResult;
    }


}