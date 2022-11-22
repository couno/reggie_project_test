package com.couno.reggie.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.common.CustomException;
import com.couno.reggie.entity.Setmeal;
import com.couno.reggie.entity.SetmealDish;
import com.couno.reggie.entity.dto.SetmealDto;
import com.couno.reggie.mapper.SetmealMapper;
import com.couno.reggie.service.SetmealDishService;
import com.couno.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ClassName: SetmealServiceImpl
 * date: 2022/11/12 14:16
 * 套餐的管理
 * @author Couno
 * ProjectName: reggie
 */
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    SetmealDishService setmealDishService;

    /**
     * 添加套餐
     * @param setmealDto
     */
    @Transactional
    public void saveSetMealDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }


    /**
     * 删除套餐操作
     * @param ids
     */
    @Transactional
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        log.info("--------------------{}",count);
        if (count > 0){
            throw new CustomException("商品正在售卖，不能删除！");
        }
        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);
    }


}
