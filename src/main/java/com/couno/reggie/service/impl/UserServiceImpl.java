package com.couno.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.entity.User;
import com.couno.reggie.mapper.UserMapper;
import com.couno.reggie.service.UserService;
import org.springframework.stereotype.Service;

/**
 * ClassName: UserServiceImpl
 * date: 2022/11/22 9:47
 *
 * @author Couno
 * ProjectName: reggie
 */

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
