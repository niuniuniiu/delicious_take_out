package com.example.delicious_take_out.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.delicious_take_out.config.emtity.Category;
import org.springframework.beans.factory.annotation.Autowired;

public interface CategoryService extends IService<Category> {
    public void remove(int id);
}
