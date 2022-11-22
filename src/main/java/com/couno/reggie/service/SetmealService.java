package com.couno.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.couno.reggie.entity.Setmeal;
import com.couno.reggie.entity.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    // 保存添加套餐（其中有添加setmeal与SetmealDish操作）
    void saveSetMealDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
