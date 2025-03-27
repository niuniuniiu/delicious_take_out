package com.example.delicious_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.delicious_take_out.config.emtity.User;
import com.example.delicious_take_out.mapper.UserMapper;
import com.example.delicious_take_out.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
