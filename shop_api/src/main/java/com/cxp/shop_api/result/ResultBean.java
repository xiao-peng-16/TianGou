package com.cxp.shop_api.result;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Getter
@ToString
@NoArgsConstructor
public class ResultBean {
    private boolean success;    //操作是否成功
    private int status;         //操作失败对应的类型
    private Object data;        //操作成功后台返回的响应数据

    public ResultBean(boolean success) {
        this.success = success;
    }


    public ResultBean(boolean success, int status) {
        this.success = success;
        this.status = status;
    }

    public ResultBean(boolean success,Object data) {
        this.success = success;
        this.data = data;
    }

    public ResultBean(boolean success, int status,Object data) {
        this.success = success;
        this.status = status;
        this.data = data;
    }



}
