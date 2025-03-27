package com.example.delicious_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.delicious_take_out.common.R;
import com.example.delicious_take_out.config.emtity.Employee;
import com.example.delicious_take_out.config.emtity.User;
import com.example.delicious_take_out.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public R<User> login(HttpServletRequest request, @RequestBody User user) {
        //根据页面提交的Email查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,user.getEmail());
        User u = userService.getOne(queryWrapper);//数据库中的用户名做了unique约束
        if (u == null) {
            return R.error("Login failed");
        }

//        比对密码
        if (!u.getPassword().equals(user.getPassword())) {
            return R.error("Login failed");
        }

//        登录成功
        request.getSession().setAttribute("user", u.getId());
        return R.success(user);
    }
}
