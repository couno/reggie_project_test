package com.couno.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.Category;
import com.couno.reggie.entity.Dish;
import com.couno.reggie.entity.DishFlavor;
import com.couno.reggie.entity.dto.DishDto;
import com.couno.reggie.service.CategoryService;
import com.couno.reggie.service.DishFlavorService;
import com.couno.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    @Autowired
    private CategoryService categoryService;


    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Rmg<Page<DishDto>> page(int page, int pageSize, String name){
        log.info("page:{},pageSize:{},name :{}",page,pageSize,name);
        Page<Dish> pageInfo = new Page<>(page,pageSize);
        Page<DishDto> pageDishDto = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);

        queryWrapper.orderByDesc(Dish::getUpdateTime);

        dishService.page(pageInfo,queryWrapper);

        // 对象拷贝，接下来的主要目的是给页面返回的数据中添加分类名称
        // 1.页面信息在pageInfo当中就有，所有拷贝pageInfo,但忽略records里的内容
        // (因为Record里是每条数据的信息，里面没有包含菜品类名)
        BeanUtils.copyProperties(pageInfo,pageDishDto,"records");

        // 2.获取原先pageInfo中的Record信息(主要目的在里面添加菜品名)
        List<Dish> records = pageInfo.getRecords();
        // 3.向每一项中添加菜品名
        List<DishDto> list = records.stream().map((item) ->{
            // 3.1 创建一个dishDto类，因为它继承了dish类，而且添加了菜品类名字段
            DishDto dishDto = new DishDto();
            // 3.2 将原先的每项添加当dishDto类里
            BeanUtils.copyProperties(item,dishDto);
            // 3.3 添加菜品类名到对象里
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList()); // 这里是将它重新转换成list

        pageDishDto.setRecords(list);

        return Rmg.success(pageDishDto);
    }

    @PostMapping
    public Rmg<String> dish(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return Rmg.success("新增菜品成功");
    }

    @GetMapping("/{id}")
    public Rmg<DishDto> get(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Rmg.success(dishDto);
    }

    @PutMapping
    public Rmg<String> update(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.updateWithFlavor(dishDto);
        return Rmg.success("修改菜品成功");
    }

    @DeleteMapping
    public Rmg<String> delete(@RequestParam("ids") Long[] id){
        log.info("这里的ids={}",id);
        for (Long aLong : id) {
            dishFlavorService.removeById(aLong);
            dishService.removeById(aLong);
        }
        return Rmg.success("删除成功");
    }

    @PostMapping("/status/{Num}")
    public Rmg<String> status(@PathVariable Integer Num,@RequestParam("ids") Long[] id){
        log.info("-----------{}",Num);
        for (Long aLong : id) {
            Dish dish = dishService.getById(aLong);
            dish.setStatus(Num);
            dishService.updateById(dish);
        }
        return Rmg.success("成功");
    }

    @GetMapping("/list")
    public Rmg<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> dishList = dishService.list(queryWrapper);

        List<DishDto> dishDtoList = dishList.stream().map((item) ->{
            DishDto dishDto = new DishDto();

            BeanUtils.copyProperties(item,dishDto);

            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }

            // 当前菜品id
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(DishFlavor::getDishId,dishId);

            List<DishFlavor> dishFlavorList = dishFlavorService.list(lambdaQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());

        return Rmg.success(dishDtoList);
    }
}
