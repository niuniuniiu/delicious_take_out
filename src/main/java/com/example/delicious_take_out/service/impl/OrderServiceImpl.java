package com.example.delicious_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.delicious_take_out.common.BaseContext;
import com.example.delicious_take_out.common.CustomException;
import com.example.delicious_take_out.common.R;
import com.example.delicious_take_out.config.emtity.*;
import com.example.delicious_take_out.mapper.OrderMapper;
import com.example.delicious_take_out.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.invoke.LambdaConversionException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Orders> implements OrderService {

    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private OrderDetailService orderDetailService;

    //    用户下单
    @Transactional
    public void submit(Orders orders) {
        // 获得当前用户 id
        int userId = Math.toIntExact(BaseContext.getCurrentId());

        // 查询当前用户购物车数据
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(wrapper);

        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("Корзина для покупок пуста"); // 购物车为空
        }

        // 查询用户数据
        User user = userService.getById(userId);

        // 查询地址数据
        int addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null) {
            throw new CustomException("Информация об адресе пользователя неверна, и заказ не может быть размещен."); // 地址为空
        }

        // 原子操作，保证线程安全
        AtomicInteger amount = new AtomicInteger(0);

        // 计算总金额并生成订单明细
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item) -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());
            // 累加金额：Amount * 数量
            amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());

        // 完成下单，向数据库插入一条数据
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(System.currentTimeMillis())); // 使用时间戳作为订单号
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        // 插入订单数据，数据库会自动生成 id
        this.save(orders);

        // 获取生成的订单 id
        Integer orderId = orders.getId();

        // 设置订单明细的 orderId
        orderDetails.forEach(orderDetail -> orderDetail.setOrderId(orderId));

        // 向订单明细表插入数据
        orderDetailService.saveBatch(orderDetails);

        // 清空购物车数据
        shoppingCartService.remove(wrapper);
    }

}
