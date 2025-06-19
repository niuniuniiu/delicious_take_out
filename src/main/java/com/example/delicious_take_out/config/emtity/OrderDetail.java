package com.example.delicious_take_out.config.emtity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 订单明细
 */
@Data
public class OrderDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)  // 自增
    private Integer id;  // id 类型为 int 或 Integer

    //名称
    private String name;

    //订单id
    private Integer orderId;


    //菜品id
    private Integer dishId;


    //数量
    private Integer number;

    //金额
    private BigDecimal amount;

    //图片
    private String image;
}
