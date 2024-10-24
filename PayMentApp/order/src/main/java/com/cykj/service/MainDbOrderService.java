package com.cykj.service;

import com.cykj.pojo.MainDbOrder;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.OrderVo;

import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/10/10 10:53
 * @description:
 */
public interface MainDbOrderService {
    ResponseDTO getAllOrder(OrderVo vo);
    public ResponseDTO saveAlipayOrder(List<MainDbOrder> orders);

}
