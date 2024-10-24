package com.cykj.service.impl;

import com.cykj.mapper.OrderDbMapper;
import com.cykj.service.DbService;
import com.cykj.utils.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 李璟瑜
 * @date 2024/10/10 16:18
 * @description:
 */
@Service
public class DbServiceImpl implements DbService {

    @Autowired
    OrderDbMapper mapper;

    @Override
    public ResponseDTO createDb(String merchantNumber) {
        System.out.println("merchantNumber:"+merchantNumber);
        mapper.createDb("`db"+merchantNumber+"`");
        return ResponseDTO.default_success();
    }
}
