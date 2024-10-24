package com.cykj.service.impl;

import com.cykj.mapper.OrderMainDbOrderMapper;
import com.cykj.pojo.MainDbOrder;
import com.cykj.service.MainDbOrderService;
import com.cykj.utils.ResponseDTO;
import com.cykj.vo.NewMainDbOrder;
import com.cykj.vo.OrderVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/10/10 10:53
 * @description:
 */
@Service
public class MainDbOrderOrderServiceImpl implements MainDbOrderService {

    @Autowired
    OrderMainDbOrderMapper mapper;

//    根据条件获得订单
    @Override
    public ResponseDTO getAllOrder(OrderVo vo) {
        PageHelper.startPage(vo.getCurrentPage(), vo.getPageSize());

        List<NewMainDbOrder> allOrder = mapper.getAllOrder(vo);

        PageInfo<NewMainDbOrder> pageInfo = new PageInfo<>(allOrder);

        return ResponseDTO.default_success(pageInfo);
    }

    @Override
    public ResponseDTO saveAlipayOrder(List<MainDbOrder> orders) {
        int i = mapper.addOrder(orders);
        if (i > 0){
            return ResponseDTO.default_success("保存成功");
        }else {
            return ResponseDTO.default_fail("保存失败");
        }

    }


}
