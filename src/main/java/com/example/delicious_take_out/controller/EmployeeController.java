package com.example.delicious_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.delicious_take_out.common.R;
import com.example.delicious_take_out.config.emtity.Employee;
import com.example.delicious_take_out.service.EmployeeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/employee")//登陆页面中的放松的请求路径包含/empoyee
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);//数据库中的用户名做了unique约束
        if (emp == null) {
            return R.error("Login failed");
        }

//        比对密码
        if (!emp.getPassword().equals(employee.getPassword())) {
            return R.error("Login failed");
        }

//        登录成功
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
       //清理Session中保存的员工id
        request.getSession().removeAttribute("employee");
        return R.success("Logout success");
    }
}