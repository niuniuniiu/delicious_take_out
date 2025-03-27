package com.example.delicious_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.delicious_take_out.common.BaseContext;
import com.example.delicious_take_out.common.R;
import com.example.delicious_take_out.config.emtity.ShoppingCart;
import com.example.delicious_take_out.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //设置用户id,指定当前时哪个用户的购物车数据
        Integer currentId = Math.toIntExact(BaseContext.getCurrentId());
        shoppingCart.setUserId(currentId);


        //查询如今添加的菜品是否在购物车中
        Integer dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, currentId);

        if (dishId != null) {
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }

        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        if (cartServiceOne != null) {
            //如果已经存在，就在原来基础之上+1
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number + 1);
            shoppingCartService.updateById(cartServiceOne);
        } else {
            //如不存在，则添加到购物车，数量默认1

            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }


        return R.success(cartServiceOne);
    }

    @PostMapping("/sub")
    @Transactional
    public R<ShoppingCart> delete(@RequestBody ShoppingCart shoppingCart) {
        //得到菜品id
        Integer dishId = shoppingCart.getDishId();
        //在数据库中对比用户id
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        //代表数量减少的是菜品数量
        if (dishId != null) {
            //通过dishId查出购物车对象
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
            //这里必须要加两个条件，否则会出现用户互相修改对方与自己购物车中相同套餐或者是菜品的数量
            queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
            ShoppingCart cart = shoppingCartService.getOne(queryWrapper);
            cart.setNumber(cart.getNumber() - 1);
            Integer LatestNumber = cart.getNumber();
            if (LatestNumber > 0) {
                //对数据进行更新操作
                shoppingCartService.updateById(cart);
            } else if (LatestNumber == 0) {
                //如果购物车的菜品数量减为0，那么就把菜品从购物车删除
                shoppingCartService.removeById(cart.getId());
            } else if (LatestNumber < 0) {
                return R.error("操作异常");
            }

            return R.success(cart);
        }

        return R.error("操作异常");
    }



    //查看购物车
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);

        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }
//清空购物车
    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }

}
