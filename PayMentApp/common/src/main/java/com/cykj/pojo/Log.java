package com.cykj.pojo;

import java.util.Date;
import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:46
 * @description:
 */
@Data
public class Log {
    /**
    * 日志id
    */
    private Integer logId;

    /**
    * 管理员id
    */
    private Integer adminId;

    /**
    * 日志内容
    */
    private String logs;

    /**
    * 日志创建时间
    */
    private Date logTime;

    public Log() {
    }

    public Log(Integer adminId, String logs) {
        this.adminId = adminId;
        this.logs = logs;
    }
}