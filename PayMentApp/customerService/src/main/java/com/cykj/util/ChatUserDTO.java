package com.cykj.util;

import com.cykj.pojo.CmsUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ChatUserDTO {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户名
     */
    private String userName;

    public ChatUserDTO convertUser(CmsUser user){
        ChatUserDTO chatUserDTO=new ChatUserDTO();
        chatUserDTO.setUserId(user.getId()+"")
                .setUserName(user.getUserName());
        return chatUserDTO;
    }
}
