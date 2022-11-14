package com.couno.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.couno.reggie.common.BaseContext;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.Category;
import com.couno.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * ClassName: CategoryController
 * date: 2022/11/12 12:52
 *
 * @author Couno
 * ProjectName: reggie
 */

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * 通过id删除一个菜品/套餐分类
     * @param id
     * @return
     */
    @DeleteMapping
    public Rmg<String> delete(@RequestParam("ids") Long id){
        log.info("开始删除id:{}。。。。。",id);
        categoryService.remove(id);
        return Rmg.success("删除成功!");
    }


    /**
     * 更新菜品分类
     * @param request
     * @param category
     * @return
     */
    @PutMapping
    public Rmg<String> update(HttpServletRequest request,@RequestBody Category category){
        log.info("更新的菜品分类：{}",category);

        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        categoryService.updateById(category);

        return Rmg.success("修改成功");
    }



    /**
     * 新增菜品分类信息
     * @param request
     * @param category
     * @return
     */
    @PostMapping
    public Rmg<String> save(HttpServletRequest request, @RequestBody Category category){
        log.info("新增菜品的信息：{}",category);
        Long empId = (Long) request.getSession().getAttribute("employee");
        log.info("新增菜品人:{}",empId);
        // // 自动注入公共字段
        // BaseContext.setCurrentId(empId);
        categoryService.save(category);
        return Rmg.success("添加成功");
    }


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Rmg<Page<Category>> page(int page, int pageSize){
        log.info("page:{},pageSize:{}",page,pageSize);
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        // 排序
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo,queryWrapper);

        return Rmg.success(pageInfo);
    }


    /**
     * 新建菜品时下拉选项框选项
     * @param category
     * @return
     */
    @GetMapping("/list")
    public Rmg<List<Category>> list(Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null,Category::getType,category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getCreateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return Rmg.success(list);
    }


}
