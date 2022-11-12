package com.couno.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.common.CustomException;
import com.couno.reggie.entity.Category;
import com.couno.reggie.entity.Dish;
import com.couno.reggie.entity.Setmeal;
import com.couno.reggie.mapper.CategoryMapper;
import com.couno.reggie.service.CategoryService;
import com.couno.reggie.service.DishService;
import com.couno.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ClassName: CategoryServiceImpl
 * date: 2022/11/12 12:48
 *
 * @author Couno
 * ProjectName: reggie
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService  {

    @Autowired
    DishService dishService;

    @Autowired
    SetmealService setmealService;



    /**
     * 在删除之前，对此分类查看是否有关联的菜品
     * 如果有关联的菜品则不能删除
     * 返回信息
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0){
            throw new CustomException("当下分类关联了菜品，不能删除");
        }
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            throw new CustomException("当下分类关联了套餐，不能删除");
        }
        super.removeById(id);
    }
}
