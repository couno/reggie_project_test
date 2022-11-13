package com.couno.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.Dish;
import com.couno.reggie.service.DishFlavorService;
import com.couno.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ClassName: DishController
 * date: 2022/11/13 22:48
 *
 * @author Couno
 * ProjectName: reggie
 */


@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Rmg<Page<Dish>> page(int page, int pageSize, String name){
        log.info("page:{},pageSize:{},name :{}",page,pageSize,name);
        Page<Dish> pageInfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        return Rmg.success(pageInfo);
    }

    @PostMapping
    public Rmg<String> dish(){

        return null;
    }

}
