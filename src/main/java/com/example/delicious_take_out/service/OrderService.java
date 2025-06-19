package com.example.delicious_take_out.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.delicious_take_out.common.CustomException;
import com.example.delicious_take_out.config.emtity.OrderDetail;
import com.example.delicious_take_out.config.emtity.Orders;
import org.springframework.core.annotation.Order;

import java.util.List;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     */
    public void submit(Orders orders);
    Orders getOrderWithDetails(Integer orderId);

}

