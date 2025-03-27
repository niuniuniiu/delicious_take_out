package com.example.delicious_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.delicious_take_out.config.emtity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
