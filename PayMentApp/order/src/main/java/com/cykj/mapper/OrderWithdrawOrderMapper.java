package com.cykj.mapper;


import com.cykj.pojo.WithdrawOrder;
import com.cykj.vo.GetWithdrawOrder;
import com.cykj.vo.GetWithdrawOrdersByTwoAccountVo;
import com.cykj.vo.NewWithDrawOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: fichil
 * @Date: 2024-10-10 15:22
 * @Description: 提现订单Mapper
 */
@Mapper
public interface OrderWithdrawOrderMapper {

    //根据提现账号搜索提现订单
    List<NewWithDrawOrder> getWithdrawOrders(GetWithdrawOrder getWithdrawOrder);

    int deleteByPrimaryKey(Integer id);

    int insert(WithdrawOrder record);

    //新增提现订单
    int insertSelective(WithdrawOrder record);

    WithdrawOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WithdrawOrder record);

    int updateByPrimaryKey(WithdrawOrder record);

    //根据提现账户获取提现记录
    List<NewWithDrawOrder> getWithdrawOrdersByTwoAccount(GetWithdrawOrdersByTwoAccountVo getWithdrawOrdersByTwoAccountVo);
}
