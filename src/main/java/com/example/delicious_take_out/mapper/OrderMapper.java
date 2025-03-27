package com.example.delicious_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.delicious_take_out.config.emtity.Orders;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
