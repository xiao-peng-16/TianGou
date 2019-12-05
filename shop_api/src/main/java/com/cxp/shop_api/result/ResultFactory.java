package com.cxp.shop_api.result;

import java.util.HashMap;
import java.util.Map;

public class ResultFactory {

    private static final ResultBean successResult = new ResultBean(true);
    private static final ResultBean templateSuccessResult = new ResultBean(true);
    private static final Map<Integer,ResultBean> ResultBeanMap = new HashMap<>();





    public static ResultBean createFailResult(int status, Object data){
        return new ResultBean(false,status,data);
    }

    public static ResultBean createSuccessResult(){
        return successResult;
    }

    public static ResultBean createSuccessResult(Object data){
        templateSuccessResult.setData(data);
        return templateSuccessResult;
    }



    public static ResultBean createFailResult(int status){
        ResultBean resultBean = ResultBeanMap.get(status);
        if (null == resultBean){
            resultBean = new ResultBean(false,status);
            ResultBeanMap.put(status, resultBean);
        }
        return resultBean;
    }
}
