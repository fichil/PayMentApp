package com.cykj.utils;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/7/17 9:34
 * @description: 响应类
 */
@Data
public class ResponseDTO {
    private int code;
    private String msg;
    private Object data;

    public ResponseDTO(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResponseDTO() {}

    public static ResponseDTO default_success(){
        return new ResponseDTO(1,"成功",null);
    }
    public static ResponseDTO default_success(Object context){
        return new ResponseDTO(200,"成功",context);
    }

    public static ResponseDTO default_fail(){
        return new ResponseDTO(-1,"失败",null);
    }
    public static ResponseDTO default_fail(Object context){
        return new ResponseDTO(-1,"失败",context);
    }


}
