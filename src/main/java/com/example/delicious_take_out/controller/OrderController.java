package com.example.delicious_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.delicious_take_out.common.BaseContext;
import com.example.delicious_take_out.common.R;
import com.example.delicious_take_out.config.emtity.Dish;
import com.example.delicious_take_out.config.emtity.OrderDetail;
import com.example.delicious_take_out.config.emtity.Orders;
import com.example.delicious_take_out.config.emtity.OrdersDto;
import com.example.delicious_take_out.service.OrderDetailService;
import com.example.delicious_take_out.service.OrderService;
import com.example.delicious_take_out.service.impl.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;

    /**
     * 用户下单
     * @param orders
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        log.info("订单数据：{}",orders);
        orderService.submit(orders);
        return R.success("Заказ успешно размещен");
    }

    @PutMapping
    public R<String> updateOrderStatus(@RequestBody Orders orders){
        Integer orderId = orders.getId();
        Integer status = orders.getStatus();

        if(orderId == null || status == null){
            return R.error("У этого пользователя нет заказов");
        }
        Orders order = orderService.getById(orderId);
        order.setStatus(status);
        orderService.updateById(order);

        return R.success("Статус успешно изменен");
    }

    public List<OrderDetail> getOrderDetailListByOrderId(Integer orderId){
        LambdaQueryWrapper<OrderDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OrderDetail::getOrderId, orderId);
        List<OrderDetail> orderDetailList = orderDetailService.list(queryWrapper);
        return orderDetailList;
    }

    @GetMapping("/userPage")
    public R<Page> page(int page, int pageSize){
        //分页构造器对象
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        Page<OrdersDto> pageDto = new Page<>(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        //这里是直接把当前用户分页的全部结果查询出来，要添加用户id作为查询条件，否则会出现用户可以查询到其他用户的订单情况
        //添加排序条件，根据更新时间降序排列
        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo,queryWrapper);

        //通过OrderId查询对应的OrderDetail
        LambdaQueryWrapper<OrderDetail> queryWrapper2 = new LambdaQueryWrapper<>();

        //对OrderDto进行需要的属性赋值
        List<Orders> records = pageInfo.getRecords();
        List<OrdersDto> orderDtoList = records.stream().map((item) ->{
            OrdersDto orderDto = new OrdersDto();
            //此时的orderDto对象里面orderDetails属性还是空 下面准备为它赋值
            int orderId = item.getId();//获取订单id
            List<OrderDetail> orderDetailList = this.getOrderDetailListByOrderId(orderId);
            BeanUtils.copyProperties(item,orderDto);
            //对orderDto进行OrderDetails属性的赋值
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        }).collect(Collectors.toList());

        //使用dto的分页有点难度.....需要重点掌握
        BeanUtils.copyProperties(pageInfo,pageDto,"records");
        pageDto.setRecords(orderDtoList);
        return R.success(pageDto);
    }


    @GetMapping("/{id}")
    public R<OrdersDto> getOrderWithDetails(@PathVariable Integer id) {
        Orders order = orderService.getById(id);
        if (order == null) {
            return R.error("Заказ не найден");
        }

        OrdersDto ordersDto = new OrdersDto();
        BeanUtils.copyProperties(order, ordersDto);

        // 查询菜品详情
        List<OrderDetail> details = getOrderDetailListByOrderId(order.getId());
        ordersDto.setOrderDetails(details);

        return R.success(ordersDto);
    }



    @GetMapping("/page")
    public R<Page<Orders>> page(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam(required = false) String number
    ) {
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        // 如果传入了 number，添加查询条件
        if (number != null && !number.isEmpty()) {
            queryWrapper.eq(Orders::getNumber, number);
        }

        queryWrapper.orderByDesc(Orders::getOrderTime);
        orderService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

}
