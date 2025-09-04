package com.example.controller;

import com.example.entity.Order;
import com.example.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * 订单控制器
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderServiceImpl orderService;
    
    /**
     * 创建订单
     */
    @PostMapping("createOrder")
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        Order createdOrder = orderService.createOrder(order);
        return ResponseEntity.ok(createdOrder);
    }
    
    /**
     * 根据订单ID查询订单
     */
//    @GetMapping("/{id}")
//    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
//        Order order = orderService.getOrderById(id);
//        if (order != null) {
//            return ResponseEntity.ok(order);
//        }
//        return ResponseEntity.notFound().build();
//    }
    
    /**
     * 根据订单号查询订单
     */
//    @GetMapping("/by-order-no/{orderNo}")
//    public ResponseEntity<Order> getOrderByOrderNo(@PathVariable String orderNo) {
//        Order order = orderService.getOrderByOrderNo(orderNo);
//        if (order != null) {
//            return ResponseEntity.ok(order);
//        }
//        return ResponseEntity.notFound().build();
//    }
    
    /**
     * 根据用户ID查询订单列表
     */
//    @GetMapping("/by-user/{userId}")
//    public ResponseEntity<List<Order>> getOrdersByUserId(@PathVariable Long userId) {
//        List<Order> orders = orderService.getOrdersByUserId(userId);
//        return ResponseEntity.ok(orders);
//    }
    
    /**
     * 查询所有订单
     */
    @GetMapping("getAllOrders")
    public ResponseEntity<List<Order>> getAllOrders(@RequestParam Long userId) {
        List<Order> orders = orderService.getAllOrders(userId);
        return ResponseEntity.ok(orders);
    }
    
    /**
     * 分页查询订单
     */
//    @GetMapping("/page")
//    public ResponseEntity<List<Order>> getOrdersByPage(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        List<Order> orders = orderService.getOrdersByPage(page, size);
//        return ResponseEntity.ok(orders);
//    }
    
    /**
     * 统计订单总数
     */
//    @GetMapping("/count")
//    public ResponseEntity<Integer> getOrderCount() {
//        int count = orderService.getOrderCount();
//        return ResponseEntity.ok(count);
//    }
    
    /**
     * 根据用户ID统计订单数量
     */
//    @GetMapping("/count/by-user/{userId}")
//    public ResponseEntity<Integer> getOrderCountByUserId(@PathVariable Long userId) {
//        int count = orderService.getOrderCountByUserId(userId);
//        return ResponseEntity.ok(count);
//    }
    
    /**
     * 更新订单状态
     */
//    @PutMapping("/{id}/status")
//    public ResponseEntity<Boolean> updateOrderStatus(
//            @PathVariable Long id,
//            @RequestParam String status) {
//        boolean success = orderService.updateOrderStatus(id, status);
//        if (success) {
//            return ResponseEntity.ok(true);
//        }
//        return ResponseEntity.notFound().build();
//    }

    
    /**
     * 删除订单
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok(true);
    }
    
    /**
     * 搜索订单
     */
//    @GetMapping("/search")
//    public ResponseEntity<List<Order>> searchOrders(
//            @RequestParam(required = false) Long userId,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) String orderNo) {
//        List<Order> orders = orderService.searchOrders(userId, status, orderNo);
//        return ResponseEntity.ok(orders);
//    }
    
    /**
     * 根据金额范围查询订单
     */
//    @GetMapping("/by-amount-range")
//    public ResponseEntity<List<Order>> getOrdersByAmountRange(
//            @RequestParam BigDecimal minAmount,
//            @RequestParam BigDecimal maxAmount) {
//        List<Order> orders = orderService.getOrdersByAmountRange(minAmount, maxAmount);
//        return ResponseEntity.ok(orders);
//    }
    
    /**
     * 批量创建订单（用于测试分片）
     */
//    @PostMapping("/batch")
//    public ResponseEntity<List<Order>> batchCreateOrders(@RequestBody List<Order> orders) {
//        List<Order> createdOrders = orderService.batchCreateOrders(orders);
//        return ResponseEntity.ok(createdOrders);
//    }
}
