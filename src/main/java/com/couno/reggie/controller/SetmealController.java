package com.couno.reggie.controller;

import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.dto.SetmealDto;
import com.couno.reggie.service.SetmealDishService;
import com.couno.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    // 添加菜品实体
    @PostMapping
    public Rmg<String> save(@RequestBody SetmealDto setmealDto){
        System.out.println(setmealDto);

        return null;
    }

}
