package com.cykj.util;

public enum ChatTypeEnum {

    adminListChatType("admin_list", "管理员列表"),
    adminChatType("admin_chat", "管理员聊天"),
    userChatType("user_chat", "用户聊天"),
    chatCountType("chat_count","消息数目");


    private String key;

    public String value;

    ChatTypeEnum() {
    }

    ChatTypeEnum(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
