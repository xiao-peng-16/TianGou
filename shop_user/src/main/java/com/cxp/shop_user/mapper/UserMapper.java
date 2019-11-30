package com.cxp.shop_user.mapper;

import com.cxp.shop_api.dto.MoneyChange;
import com.cxp.shop_api.entity.User;
import com.cxp.shop_user.pojo.UserIdName;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserMapper {

    //注册
    @Insert("insert user(user_name,user_password) values(#{userName},#{userPassword})")
    public int insUser(User user);

    @Select("select count(*) from user where user_name = #{userName}")
    public int  is_usable_userName(@Param("userName") String userName);


    //登录
    @Select("select user_id from user where user_name = #{userName} and user_password = #{userPassword}")
    public List<Integer> selUserByPassword(User user);


    //id登录
    @Select("select * from user where user_id = #{userId}")
    public User selUserByUserId(@Param("userId") int userId);

    //查余额 根据userId
    @Select("select user_money from user where user_id = #{userId}")
    public Integer getMoneyByUserId(@Param("userId") int userId);

    //查询一组用户名 根据id
    public List<UserIdName> mapUserNameByUserId(List<Integer> userIdList);

    //查询一组用户名 根据id
    @Select("select user_name from user where user_id = #{userId}")
    public String  selUserNameByUserId(@Param("userId") Integer userId);

    // 增加余额   负数代表扣钱
    public int addMoneyByUserId(List<MoneyChange> moneyChangeList);
}
