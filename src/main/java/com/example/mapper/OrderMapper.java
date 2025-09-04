package com.example.mapper;

import com.example.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单数据访问接口
 */
@Mapper
public interface OrderMapper {

    void insert(Order order);

    void delete(long orderId);

    List<Order> selectAll(Long userId);
//    List<Order> findByUserId(Long userId);
}

