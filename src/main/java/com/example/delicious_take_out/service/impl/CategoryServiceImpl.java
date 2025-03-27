package com.example.delicious_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.delicious_take_out.common.CustomException;
import com.example.delicious_take_out.config.emtity.Category;
import com.example.delicious_take_out.config.emtity.Dish;
import com.example.delicious_take_out.mapper.CategoryMapper;
import com.example.delicious_take_out.service.CategoryService;
import com.example.delicious_take_out.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>  implements CategoryService {

    @Autowired
    private DishService dishService;
    /*
     * 根据id删除分类，删除之前判断是否关联菜品或套餐*/

    @Override
    public void remove(int id) {
        log.info("准备删除分类，ID: {}", id);

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        Long count = dishService.count(dishLambdaQueryWrapper);

        if (count > 0) {
            throw new CustomException("The dishes are linked under the current category and cannot be deleted");
        }


        log.info("执行删除操作，ID: {}", id);
        super.removeById(id);
    }

}
