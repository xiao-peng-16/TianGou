package com.cxp.shop_user.service.impl;

import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.User;
import com.cxp.shop_user.exception.MoneyInsufficientException;
import com.cxp.shop_user.exception.TransactionalException;
import com.cxp.shop_user.mapper.UserMapper;
import com.cxp.shop_user.pojo.ChangeUserPassword;
import com.cxp.shop_user.pojo.UserIdName;
import com.cxp.shop_user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    public static final MoneyInsufficientException moneyInsufficientException = new MoneyInsufficientException();
    public static final TransactionalException TRANSACTIONAL_EXCEPTION = new TransactionalException();

    @Override
    public Boolean is_usable_userName(String userName) {
        return userMapper.is_usable_userName(userName)==0;
    }

    @Override
    public Boolean insUser(User user) {
        return userMapper.insUser(user)>0;
    }



    @Override
    public Integer selUserByPassword(User user) {
        List<Integer> userList = userMapper.selUserByPassword(user);
        if (userList.size()!=0){
            return userMapper.selUserByPassword(user).get(0);
        }else {
            return null;
        }

    }

    @Override
    public User selUserById(int userId) {
        User user = userMapper.selUserByUserId(userId);
        return user;
    }

    @Override
    public Map<Integer, String> mapUserNameByUserId(List<Integer> userIdList) {
        List<UserIdName> userIdNameList = userMapper.mapUserNameByUserId(userIdList);
        HashMap<Integer, String> map = new HashMap<>();
        for (UserIdName userIdName : userIdNameList)
            map.put(userIdName.getUserId(), userIdName.getUserName());
        return map;
    }

    @Override
    public String selUserNameByUserId(Integer userId) {
        return userMapper.selUserNameByUserId(userId);
    }

    @Override
    public boolean changeUserPasswordByUserId(Integer userId, ChangeUserPassword changeUserPassword) {
        return 0 != userMapper.changeUserPasswordByUserId(userId, changeUserPassword.getOldUserPassword(), changeUserPassword.getNewUserPassword());
    }


    // 转账  第一个是付钱方  剩下的都是收钱方  ，付钱方要判断余额是否足够付款
    @Transactional(rollbackFor = {MoneyInsufficientException.class,TransactionalException.class})
    @Override
    public void shopTransferByUserId(List<MoneyChange> moneyChangeList) throws MoneyInsufficientException, TransactionalException {

        System.out.println(moneyChangeList);
        MoneyChange firstItem = moneyChangeList.get(0); //付钱方

        //如果付钱方余额 和 变化（负数） 相加 小于0
        if (userMapper.getMoneyByUserId(firstItem.getUserId()) + firstItem.getUserMoneyChange()<0)
            throw moneyInsufficientException;

        if (moneyChangeList.size() != userMapper.addMoneyByUserId(moneyChangeList))
            throw  TRANSACTIONAL_EXCEPTION;
    }




}
