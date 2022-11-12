package com.couno.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.entity.Dish;
import com.couno.reggie.mapper.DishMapper;
import com.couno.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * ClassName: DishServiceImpl
 * date: 2022/11/12 14:11
 *
 * @author Couno
 * ProjectName: reggie
 */
@Slf4j
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {
}
