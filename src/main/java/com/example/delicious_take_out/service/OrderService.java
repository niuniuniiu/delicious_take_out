package com.example.delicious_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.delicious_take_out.config.emtity.Orders;
import org.springframework.core.annotation.Order;

public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     */
    public void submit(Orders orders);
}

