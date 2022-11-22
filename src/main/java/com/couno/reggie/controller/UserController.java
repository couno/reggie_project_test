package com.couno.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.couno.reggie.Utils.ValidateCodeUtils;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.User;
import com.couno.reggie.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * ClassName: UserController
 * date: 2022/11/22 9:51
 *
 * @author Couno
 * ProjectName: reggie
 */

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {


    @Autowired
    UserService userService;

    /**
     * 1.接收用户手机号
     * 2.生成验证码
     * 3.返回验证码
     */
    @PostMapping("/sendMsg")
    public Rmg<String> sendMsg(@RequestBody User user,HttpSession session){
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {

            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("手机号：{},验证码：{}",phone,code);
            session.setAttribute(phone,code);

            return Rmg.success("手机验证码短信发送成功");
        };
        return Rmg.error("发送失败");
    }

    @PostMapping("/login")
    public Rmg<User> login(@RequestBody Map<String,String> user, HttpSession session){

        // 1.获取手机号与验证码
        String phone = user.get("phone");
        String code = user.get("code");
        log.info("phone:{},code:{}",phone,code);

        // 2.从session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
        log.info("codeInSession:{}",codeInSession);

        // 3.进行验证码的比对
        if(codeInSession != null && codeInSession.equals(code)){
            // 3.1 比对成功说明登录成功
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user1 = userService.getOne(queryWrapper);
            // 3.2 判断数据库中是否有此用户
            if (user1 == null){
                user1 = new User();
                user1.setPhone(phone);
                userService.save(user1);
            }
            session.setAttribute("user",user1.getId());
            return Rmg.success(user1);
        }
        return Rmg.error("验证码错误");
    }

}
