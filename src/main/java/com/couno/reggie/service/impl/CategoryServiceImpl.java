package com.couno.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.entity.Category;
import com.couno.reggie.mapper.CategoryMapper;
import com.couno.reggie.service.CategoryService;
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
}
