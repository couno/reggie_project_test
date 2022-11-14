package com.couno.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.Dish;
import com.couno.reggie.entity.dto.DishDto;

public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品和口味数据
    void saveWithFlavor(DishDto dishDto);

    // 根据id菜品信息和对应口味信息
    DishDto getByIdWithFlavor(Long id);

    // 修改菜品
    void updateWithFlavor(DishDto dishDto);
}
