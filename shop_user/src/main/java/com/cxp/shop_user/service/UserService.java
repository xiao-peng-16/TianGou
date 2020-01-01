package com.cxp.shop_user.service;

import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.User;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_user.exception.MoneyInsufficientException;
import com.cxp.shop_user.exception.TransactionalException;
import com.cxp.shop_user.pojo.ChangeUserPassword;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface UserService {

    //注册
    public Boolean insUser(User user);
    public Boolean is_usable_userName(String userName);

    //登录
    public ResultBean getTokenByPassword(User user);

    // 根据id
    public User selUserById(int userId);

    //查询一组用户名 根据id
    public Map<Integer,String> mapUserNameByUserId(List<Integer> userIdList);

    //查询用户名 根据id
    public String  selUserNameByUserId(Integer userId);

    //查询用户头像
    public String  selUserPhotoByUserId(Integer userId);

    //修改密码
    public boolean changeUserPasswordByUserId(Integer userId, ChangeUserPassword changeUserPassword);

    //更换头像
    public boolean changeUserPhotoByUserId(Integer userId, String userPhoto);

    // 转账  第一个是付钱方  剩下的都是收钱方  ，付钱方要判断余额是否足够付款
    public void shopTransferByUserId(List<MoneyChange> moneyChangeList) throws MoneyInsufficientException, TransactionalException;


}
