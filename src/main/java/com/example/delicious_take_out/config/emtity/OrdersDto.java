package com.example.delicious_take_out.config.emtity;

import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Date 2025/5/14 11:58
 * @Version 1.0
 */
@Data
public class OrdersDto  extends Orders{
        private List<OrderDetail> orderDetails;

}

