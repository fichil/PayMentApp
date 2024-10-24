package com.cykj.mapper;

import com.cykj.pojo.MainDbOrder;
import com.cykj.vo.NewMainDbOrder;
import com.cykj.vo.OrderVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 李璟瑜
 * @date 2024/9/26 14:48
 * @description:
 */
@Mapper
public interface OrderMainDbOrderMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MainDbOrder record);

    int insertSelective(MainDbOrder record);

    MainDbOrder selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MainDbOrder record);

    int updateByPrimaryKey(MainDbOrder record);
    List<NewMainDbOrder> getAllOrder(OrderVo vo);

    public int addOrder(@Param("orders") List<MainDbOrder> orders);

}