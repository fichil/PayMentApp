package com.cykj.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户发送信息
     */
    private String message;

    /**
     * 发送日期
     * 消息时间格式(yyyy-MM-dd)
     */
    private String createDate;

    /**
     * 发送时间
     * 消息时间格式(yyyy-MM-dd HH:mm:ss)
     */
    private String createTime;
}
