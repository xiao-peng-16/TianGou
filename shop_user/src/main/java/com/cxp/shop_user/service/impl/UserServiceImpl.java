package com.cxp.shop_user.service.impl;

import com.cxp.shop_api.dto.UserMoneyChange;
import com.cxp.shop_api.entity.User;
import com.cxp.shop_api.result.ResultBean;
import com.cxp.shop_api.result.ResultFactory;
import com.cxp.shop_api.result.ResultStatus;
import com.cxp.shop_user.exception.MoneyInsufficientException;
import com.cxp.shop_user.exception.TransactionalException;
import com.cxp.shop_user.mapper.UserMapper;
import com.cxp.shop_user.pojo.ChangeUserPassword;
import com.cxp.shop_user.pojo.UserIdName;
import com.cxp.shop_user.service.UserService;
import com.cxp.shop_user.service.feignClient.ImagesFeignClient;
import com.cxp.shop_user.service.feignClient.ZuulFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    ZuulFeignClient zuulFeignClient;
    @Autowired
    ImagesFeignClient imagesFeignClient;

    static final ResultBean USER_Add_ERROR = ResultFactory.createFailResult(ResultStatus.USER_Add_ERROR);
    static final ResultBean USER_NAME_DISABLED = ResultFactory.createFailResult(ResultStatus.USER_NAME_DISABLED);
    static final ResultBean successResult = ResultFactory.createSuccessResult();


    public static final MoneyInsufficientException moneyInsufficientException = new MoneyInsufficientException();
    public static final TransactionalException TRANSACTIONAL_EXCEPTION = new TransactionalException();
    static final ResultBean USER_LOGIN_ERROR = ResultFactory.createFailResult(ResultStatus.USER_LOGIN_ERROR);

    @Override
    public boolean is_usable_userName(String userName) {
        return 0 == userMapper.countUserName(userName);
    }

    @Override
    public ResultBean addUserAndGetToken(User user) {
        try {
            userMapper.insUser(user);
            return ResultFactory.createSuccessResult(zuulFeignClient.getUserLoginToken(user.getUserId()));
        }catch (Exception e){
            if(e.getCause() instanceof java.sql.SQLIntegrityConstraintViolationException)
                return USER_NAME_DISABLED;
            return USER_Add_ERROR;
        }
    }



    @Override
    public ResultBean getTokenByPassword(User user) {
        Integer userId = userMapper.getUserIdByPassword(user);
        return userId==null? USER_LOGIN_ERROR : ResultFactory.createSuccessResult(zuulFeignClient.getUserLoginToken(userId));
    }

    @Override
    public User getUserByUserId(int userId) {
        return userMapper.getUserByUserId(userId);
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
    public String getUserNameByUserId(Integer userId) {
        return userMapper.getUserNameByUserId(userId);
    }

    @Override
    public String getUserPhotoByUserId(Integer userId) {
        return userMapper.getUserPhotoByUserId(userId);
    }

    @Override
    public boolean changeUserPasswordByUserId(Integer userId, ChangeUserPassword changeUserPassword) {
        return 0 != userMapper.changeUserPasswordByUserId(userId, changeUserPassword.getOldUserPassword(), changeUserPassword.getNewUserPassword());
    }

    @Override
    public boolean changeUserPhotoByUserId(Integer userId, String newUserPhoto) {
        String oldUserPhotoURL = userMapper.getUserPhotoByUserId(userId);
        boolean success = 0 != userMapper.changeUserPhotoByUserId(userId, newUserPhoto);
        if (success)
            if (null != oldUserPhotoURL && !"".equals(oldUserPhotoURL))
                imagesFeignClient.dlelQiniuImages(oldUserPhotoURL);
        return success;
    }


    // 转账  第一个是付钱方  剩下的都是收钱方  ，付钱方要判断余额是否足够付款
    @Transactional(rollbackFor = {MoneyInsufficientException.class,TransactionalException.class})
    @Override
    public void shopTransferByUserId(Integer userId, LinkedList<UserMoneyChange> userMoneyChangeList) throws MoneyInsufficientException, TransactionalException {

        double sumMoneyChange = userMoneyChangeList.stream().mapToDouble(e -> e.getMoneyChange()).sum();
        userMoneyChangeList.addFirst(new UserMoneyChange(userId, -sumMoneyChange));

        //如果付钱方余额 和 变化（负数） 相加 小于0
        if (userMapper.getMoneyByUserId(userId) < sumMoneyChange)
            throw moneyInsufficientException;

        if (userMoneyChangeList.size() != userMapper.addMoneyByUserId(userMoneyChangeList))
            throw  TRANSACTIONAL_EXCEPTION;
    }




}
