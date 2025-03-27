package com.example.delicious_take_out.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.delicious_take_out.config.emtity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
