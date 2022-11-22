package com.couno.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.entity.ShoppingCart;
import com.couno.reggie.mapper.ShoppingCartMapper;
import com.couno.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * ClassName: ShoppingCartServiceImpl
 * date: 2022/11/22 20:15
 *
 * @author Couno
 * ProjectName: reggie
 */

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
