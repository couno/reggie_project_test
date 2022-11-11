package com.couno.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.couno.reggie.entity.Employee;
import com.couno.reggie.mapper.EmployeeMapper;
import com.couno.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * ClassName: EmployeeServiceImpl
 * date: 2022/11/10 10:59
 *
 * @author Couno
 * ProjectName: reggie
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
