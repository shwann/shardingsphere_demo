package com.example.controller;

import com.example.entity.Order;
import com.example.service.impl.OrderServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ShardingSphere 分片测试控制器
 */
@RestController
@RequestMapping("/api/sharding-test")
public class ShardingTestController {
    
    @Autowired
    private OrderServiceImpl orderService;
    
    /**
     * 生成测试数据并插入到不同分片
     */
    @PostMapping("/generate-test-data")
    public ResponseEntity<String> generateTestData(@RequestParam(defaultValue = "100") int count) {
        try {
            List<Order> orders = new ArrayList<>();
            Random random = new Random();
            
            for (int i = 1; i <= count; i++) {
                Order order = new Order();
                order.setUserId(random.nextInt(1000) + 1); // 随机用户ID
                order.setStatus("PENDING");
                orders.add(order);
                orderService.createOrder( order);
            }
            

            return ResponseEntity.ok("成功生成 " + count + " 条测试数据");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("生成测试数据失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试分片查询 - 查询所有订单
     */
    @GetMapping("/test-query-all")
    public ResponseEntity<String> testQueryAll() {
        try {
            long startTime = System.currentTimeMillis();
            List<Order> orders = orderService.getAllOrders(1L);
            long endTime = System.currentTimeMillis();
            
            String result = String.format("查询所有订单成功，共 %d 条记录，耗时 %d ms", 
                    orders.size(), (endTime - startTime));
            
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("查询失败: " + e.getMessage());
        }
    }
    
    /**
     * 测试分片查询 - 根据用户ID查询
     */
//    @GetMapping("/test-query-by-user/{userId}")
//    public ResponseEntity<String> testQueryByUser(@PathVariable Long userId) {
//        try {
//            long startTime = System.currentTimeMillis();
//            List<Order> orders = orderService.getOrdersByUserId(userId);
//            long endTime = System.currentTimeMillis();
//
//            String result = String.format("查询用户 %d 的订单成功，共 %d 条记录，耗时 %d ms",
//                    userId, orders.size(), (endTime - startTime));
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("查询失败: " + e.getMessage());
//        }
//    }
    
    /**
     * 测试分片查询 - 分页查询
     */
//    @GetMapping("/test-query-page")
//    public ResponseEntity<String> testQueryPage(
//            @RequestParam(defaultValue = "1") int page,
//            @RequestParam(defaultValue = "20") int size) {
//        try {
//            long startTime = System.currentTimeMillis();
//            List<Order> orders = orderService.getOrdersByPage(page, size);
//            long endTime = System.currentTimeMillis();
//
//            String result = String.format("分页查询成功，第 %d 页，每页 %d 条，实际返回 %d 条，耗时 %d ms",
//                    page, size, orders.size(), (endTime - startTime));
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("查询失败: " + e.getMessage());
//        }
//    }
    
    /**
     * 测试分片查询 - 统计总数
     */
//    @GetMapping("/test-count")
//    public ResponseEntity<String> testCount() {
//        try {
//            long startTime = System.currentTimeMillis();
//            int count = orderService.getOrderCount();
//            long endTime = System.currentTimeMillis();
//
//            String result = String.format("统计订单总数成功，共 %d 条记录，耗时 %d ms",
//                    count, (endTime - startTime));
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("统计失败: " + e.getMessage());
//        }
//    }
    
    /**
     * 测试分片查询 - 金额范围查询
     */
//    @GetMapping("/test-query-by-amount")
//    public ResponseEntity<String> testQueryByAmount(
//            @RequestParam(defaultValue = "1000") BigDecimal minAmount,
//            @RequestParam(defaultValue = "5000") BigDecimal maxAmount) {
//        try {
//            long startTime = System.currentTimeMillis();
//            List<Order> orders = orderService.getOrdersByAmountRange(minAmount, maxAmount);
//            long endTime = System.currentTimeMillis();
//
//            String result = String.format("金额范围查询成功，金额范围 %s-%s，共 %d 条记录，耗时 %d ms",
//                    minAmount, maxAmount, orders.size(), (endTime - startTime));
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("查询失败: " + e.getMessage());
//        }
//    }
    
    /**
     * 测试分片查询 - 条件搜索
     */
//    @GetMapping("/test-search")
//    public ResponseEntity<String> testSearch(
//            @RequestParam(required = false) Long userId,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) String orderNo) {
//        try {
//            long startTime = System.currentTimeMillis();
//            List<Order> orders = orderService.searchOrders(userId, status, orderNo);
//            long endTime = System.currentTimeMillis();
//
//            String result = String.format("条件搜索成功，条件：userId=%s, status=%s, orderNo=%s，共 %d 条记录，耗时 %d ms",
//                    userId, status, orderNo, orders.size(), (endTime - startTime));
//
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("搜索失败: " + e.getMessage());
//        }
//    }
    
    /**
     * 清理测试数据
     */
    @DeleteMapping("/clean-test-data")
    public ResponseEntity<String> cleanTestData() {
        try {
            // 这里可以实现清理逻辑，或者直接删除所有订单
            // 为了安全起见，这里只是返回提示信息
            return ResponseEntity.ok("请手动清理测试数据，或实现清理逻辑");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("清理失败: " + e.getMessage());
        }
    }
}
