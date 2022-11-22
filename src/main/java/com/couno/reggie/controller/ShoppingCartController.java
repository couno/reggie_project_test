package com.couno.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.couno.reggie.common.BaseContext;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.ShoppingCart;
import com.couno.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * ClassName: ShoppingCartController
 * date: 2022/11/22 20:21
 *
 * @author Couno
 * ProjectName: reggie
 */

@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 向购物车添加商品
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Rmg<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        // 1.获取下单人的userId
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);


        // 2.查询这个菜品是否已经存在购物车中
        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);

        if (dishId != null){
            // 2.1 添加到购物车的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            // 2.2 添加到购物车的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());

        }
        // 3 查询当前菜品是否在购物车中
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        if (cartServiceOne != null){
            // 3.1 如果存在则数量加一
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            // 3.2 如果不存在则添加到购物车
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }
        return Rmg.success(cartServiceOne);
    }

    @PostMapping("/sub")
    public Rmg<ShoppingCart> sub(@RequestBody Map<String,String> subInfo){

        String dishId = subInfo.get("dishId");
        String setmealId = subInfo.get("setmealId");

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();

        if (dishId != null){
            queryWrapper.eq(ShoppingCart::getDishId,dishId);
        }
        if (setmealId != null){
            queryWrapper.eq(ShoppingCart::getSetmealId,setmealId);
        }
        ShoppingCart cart = shoppingCartService.getOne(queryWrapper);

        log.info("cart:{}",cart);

        if (cart != null){
            Integer number = cart.getNumber();
            if (number > 0){
                number = number - 1;
                cart.setNumber(number);
                shoppingCartService.updateById(cart);
                if (number == 0){
                    shoppingCartService.remove(queryWrapper);
                }
                return Rmg.success(cart);
            }
        }
        return Rmg.success(cart);
    }


    @GetMapping("/list")
    public Rmg<List<ShoppingCart>> list(){

        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);

        return Rmg.success(list);
    }

    @DeleteMapping("/clean")
    public Rmg<String> delete(){
        Long currentId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,currentId);
        shoppingCartService.remove(queryWrapper);
        return Rmg.success("清除成功");
    }
}
