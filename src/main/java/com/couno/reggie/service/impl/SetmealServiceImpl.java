package com.couno.reggie.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.entity.Setmeal;
import com.couno.reggie.mapper.SetmealMapper;
import com.couno.reggie.service.SetmealService;
import org.springframework.stereotype.Service;

/**
 * ClassName: SetmealServiceImpl
 * date: 2022/11/12 14:16
 *
 * @author Couno
 * ProjectName: reggie
 */

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
