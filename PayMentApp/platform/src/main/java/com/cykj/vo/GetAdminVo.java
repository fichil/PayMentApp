package com.cykj.vo;

import lombok.Data;

/**
 * @author 李璟瑜
 * @date 2024/10/10 9:05
 * @description:
 */
@Data
public class GetAdminVo {
    private int adminId;
    private int pageSize;
    private int currentPage;
    private Integer state;
    private String nickname;
}
