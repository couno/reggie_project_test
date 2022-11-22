package com.couno.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.Category;
import com.couno.reggie.entity.Setmeal;
import com.couno.reggie.entity.dto.SetmealDto;
import com.couno.reggie.service.CategoryService;
import com.couno.reggie.service.SetmealDishService;
import com.couno.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: SetmealController
 * date: 2022/11/15 1:02
 *
 * @author Couno
 * ProjectName: reggie
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    SetmealService setmealService;
    @Autowired
    SetmealDishService setmealDishService;
    @Autowired
    CategoryService categoryService;


    // 添加菜品实体
    @PostMapping
    public Rmg<String> save(@RequestBody SetmealDto setmealDto){
        log.info("传进来的实体：{}",setmealDto);

        setmealService.saveSetMealDish(setmealDto);

        return Rmg.success("添加套餐成功");
    }

    @GetMapping("page")
    public Rmg<Page<SetmealDto>> page(int page,int pageSize,String name){

        // 分页构造器
        Page<Setmeal> pageInfo = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();
        // 条件查询
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 模糊查询
        queryWrapper.like(name != null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo,queryWrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo,setmealDtoPage,"records");

        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> setmealDtos = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(setmealDtos);
        return Rmg.success(setmealDtoPage);
    }


    @DeleteMapping
    public Rmg<String> delete(@RequestParam List<Long> ids){
        log.info("ids的值{}",ids);
        setmealService.removeWithDish(ids);
        return Rmg.success("删除成功");
    }

    @PostMapping("/status/{Num}")
    public Rmg<String> status(@PathVariable Integer Num,@RequestParam("ids") Long[] id){
        log.info("-----------{}",Num);
        for (Long aLong : id) {
            Setmeal setmeal = setmealService.getById(aLong);
            setmeal.setStatus(Num);
            setmealService.updateById(setmeal);
        }
        return Rmg.success("成功");
    }

    @GetMapping("/{id}")
    public Rmg<Setmeal> update(@PathVariable Long id){
        Setmeal setmeal = setmealService.getById(id);
        return Rmg.success(setmeal);
    }

    @GetMapping("/list")
    public Rmg<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(Setmeal::getStatus,1);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);

        return Rmg.success(list);
    }

}
