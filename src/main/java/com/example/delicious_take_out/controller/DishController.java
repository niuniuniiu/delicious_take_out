package com.example.delicious_take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.delicious_take_out.common.R;
import com.example.delicious_take_out.config.emtity.Category;
import com.example.delicious_take_out.config.emtity.Dish;
import com.example.delicious_take_out.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @PostMapping
    public R<Dish> save(@RequestBody Dish dish){
        log.info("Dish:{}", dish);
        dishService.save(dish);
        return R.success(dish);
    }
        /*
        * 分页查询
        * */
    @Transactional
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        //条件构造器，根据sort排序
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Dish::getSort);

        //调用service进行分页查询
        dishService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam int id) {
        // 继续后续的逻辑
        log.info("删除菜品，id:{}", id);
        dishService.removeById(id);
        return R.success("Deleted successfully");
    }

//  根据id查询菜品信息
    @GetMapping("/{id}")
    public R<Dish> getDishById(@PathVariable("id") Integer id){
        Dish dish = dishService.getById(id);
        if (dish == null) {
            return R.error("该菜品不存在");
        }
        return R.success(dish);
    }


    //根据id修改分类信息
    @PutMapping
    public R<String> update(@RequestBody Dish dish) {
        log.info("修改菜品信息:{}", dish);
        dishService.updateById(dish);
        return R.success("Updated successfully");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        //构造查询条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
//        queryWrapper.select(Dish::getId, Dish::getName, Dish::getPrice);
//        //添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = dishService.list(queryWrapper);
        return R.success(dishList);

    }

}
