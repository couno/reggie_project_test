package com.couno.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.entity.SetmealDish;
import com.couno.reggie.entity.dto.SetmealDto;
import com.couno.reggie.mapper.SetmealDishMapper;
import com.couno.reggie.mapper.SetmealMapper;
import com.couno.reggie.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: SetmealDishServiceImpl
 * date: 2022/11/15 0:59
 *
 * @author Couno
 * ProjectName: reggie
 */

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper,SetmealDish> implements SetmealDishService {
    @Override
    public void saveSetMealDish(SetmealDto setmealDto) {

    }
}
