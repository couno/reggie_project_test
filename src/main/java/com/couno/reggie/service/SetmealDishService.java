package com.couno.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.couno.reggie.entity.SetmealDish;
import com.couno.reggie.entity.dto.SetmealDto;


public interface SetmealDishService extends IService<SetmealDish> {
    // 保存添加套餐（其中有添加setmeal与SetmealDish操作）
    void saveSetMealDish(SetmealDto setmealDto);
}
