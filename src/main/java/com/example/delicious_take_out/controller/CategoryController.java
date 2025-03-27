package com.example.delicious_take_out.controller;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.delicious_take_out.common.R;
import com.example.delicious_take_out.config.emtity.Category;
import com.example.delicious_take_out.service.CategoryService;
import com.example.delicious_take_out.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
* 分类管理
* 套餐和菜品分类仅仅通过type区分
* */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
/*
* 新增分类
* 因为是JSON格式，需要注解@RequestBody
* */

    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("Category:{}", category);
        categoryService.save(category);
        return R.success("Added successfully");
    }

    /*分页查询
    * 注解@RequestParam接收的参数是来自HTTP请求体或请求url的QueryString中。
    * */
    @Transactional
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        //分页构造器
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //条件构造器，根据sort排序
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Category::getSort);

        //调用service进行分页查询
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(@RequestParam int id) {
        // 继续后续的逻辑
        log.info("删除分类，id:{}", id);
//        categoryService.removeById(ids);
        categoryService.remove(id);
        return R.success("Deleted successfully");
    }

    //根据id修改分类信息
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        log.info("修改分类信息:{}", category);
        categoryService.updateById(category);
        return R.success("Updated successfully");
    }

    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 添加条件
        queryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
       // 添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> categoryList = categoryService.list(queryWrapper);
        return R.success(categoryList);
    }
}
