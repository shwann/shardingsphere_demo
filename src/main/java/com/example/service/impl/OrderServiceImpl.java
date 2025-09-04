package com.example.service.impl;

import com.example.entity.Order;
import com.example.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 订单服务实现类
 */
@Service
public class OrderServiceImpl  {
    
    @Autowired
    private OrderMapper orderMapper;
    
    @Transactional
    public Order createOrder(Order order) {
        orderMapper.insert(order);
        return order;
    }

//    public List<Order> getOrdersByUserId(Long userId) {
//       return orderMapper.findByUserId(userId);
//        return null;
//    }
    
    public List<Order> getAllOrders(Long userId) {
        return orderMapper.selectAll(userId);
    }
    public void deleteOrder(Long id) {
         orderMapper.delete(id);
    }
    

}
