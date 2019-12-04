package com.cxp.shop_user.controller;

import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.User;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_api.util.TokenUtil;
import com.cxp.shop_user.exception.MoneyInsufficientException;
import com.cxp.shop_user.exception.TransactionalException;
import com.cxp.shop_user.pojo.ChangeUserPassword;
import com.cxp.shop_user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@CrossOrigin(allowCredentials = "true")
@RestController
public class UserController {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    TokenUtil tokenUtil;
    static final ResultBean successResult = ResultFactory.createSuccessResult();
    static final ResultBean USER_NAME_DISABLED = ResultFactory.createFailResult(ResultStatus.USER_NAME_DISABLED);
    static final ResultBean USER_Add_ERROR = ResultFactory.createFailResult(ResultStatus.USER_Add_ERROR);
    static final ResultBean USER_LOGIN_ERROR = ResultFactory.createFailResult(ResultStatus.USER_LOGIN_ERROR);
    static final ResultBean USER_CHANGE_PASSWORD_ERROR = ResultFactory.createFailResult(ResultStatus.USER_CHANGE_PASSWORD_ERROR);
    static final ResultBean USER_ID_ERROR = ResultFactory.createFailResult(ResultStatus.USER_ID_ERROR);
    static final ResultBean USER_MONEY_INSUFFICIENT = ResultFactory.createFailResult(ResultStatus.USER_MONEY_INSUFFICIENT);
    static final ResultBean USER_MONEY_CHANGE_ERROE = ResultFactory.createFailResult(ResultStatus.USER_MONEY_CHANGE_ERROE);


    //判断用户名是否可以注册 (防止已经有人注册过了)
    @RequestMapping("/is_usable_userName")
    public ResultBean is_usable_userName(String userName) {
        return userService.is_usable_userName(userName) ? successResult : USER_NAME_DISABLED;
    }

    //先判断用户名是否可以注册 后创建用户
    @PostMapping("/addUser")
    public ResultBean addUser(@RequestBody User user) {
        ResultBean userNameResult = is_usable_userName(user.getUserName());
        return userNameResult.isSuccess()? (userService.insUser(user) ? successResult : USER_Add_ERROR) :userNameResult;
    }

    //创建用户 并登录用户返回token
    @PostMapping("/addUserAndGetToken")
    public ResultBean addUserAndGetToken(@RequestBody User user) {
        ResultBean  addUserResult = addUser(user);
        return addUserResult.isSuccess() ? getTokenByPassword(user) : addUserResult;
    }

    //账号 密码登录
    @PostMapping("/getTokenByPassword")
    public ResultBean getTokenByPassword(@RequestBody User userSrc) {
        Integer userId = userService.selUserByPassword(userSrc);
        return userId==null? USER_LOGIN_ERROR : ResultFactory.createSuccessResult(tokenUtil.addToken(userId.toString()));
    }
    //根据session 的id 返回用户
    @RequestMapping("/selUserByUserId")
    public ResultBean selUserById(Integer userId) {
        User user = userService.selUserById(userId);
        return user == null ? USER_ID_ERROR :  ResultFactory.createSuccessResult(user);
    }

    //查询一组用户名 根据id
    @PostMapping("/mapUserNameByUserId")
    public Map<Integer,String> mapUserNameByUserId(@RequestBody List<Integer> userIdList){
        return userService.mapUserNameByUserId(userIdList);
    }

    //查询用户名 根据id
    @RequestMapping("/selUserNameByUserId")
    public String  selUserNameByUserId(Integer userId){
        return userService.selUserNameByUserId(userId);
    }

    //修改密码
    @PostMapping("/changeUserPasswordByUserId")
    public ResultBean  changeUserPasswordByUserId(Integer userId, @RequestBody ChangeUserPassword changeUserPassword){
        return userService.changeUserPasswordByUserId(userId, changeUserPassword)? successResult : USER_CHANGE_PASSWORD_ERROR;
    }

    //退出登录
    @RequestMapping("/outLoginByToken")
    public ResultBean outLoginByToken(HttpServletRequest request){
        tokenUtil.deleteToken(request);
        return successResult;
    }

    // 购物转账  第一个是付钱方  剩下的都是收钱方
    @PostMapping("/shopTransferByUserId")
    public ResultBean shopTransferByUserId(@RequestBody List<MoneyChange> moneyChangeList){
        try {
            userService.shopTransferByUserId(moneyChangeList);
        } catch (MoneyInsufficientException e) {
            return USER_MONEY_INSUFFICIENT;
        } catch (TransactionalException e) {
            return USER_MONEY_CHANGE_ERROE;
        }
        return successResult;
    }


}