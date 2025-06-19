package com.example.delicious_take_out.config.emtity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 购物车
 */
@Data
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)  // 自增
    private Integer id;  // id 类型为 int 或 Integer

    //名称
    private String name;

    //用户id
    private int userId;

    //菜品id
    private int dishId;

    //数量
    private Integer number;

    //金额
    private BigDecimal amount;


    //图片
    private String image;

    private LocalDateTime createTime;
}
