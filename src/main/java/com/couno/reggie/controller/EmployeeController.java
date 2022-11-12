package com.couno.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.couno.reggie.common.BaseContext;
import com.couno.reggie.common.Rmg;
import com.couno.reggie.entity.Employee;
import com.couno.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDateTime;

/**
 * ClassName: EmployeeController
 * date: 2022/11/10 11:02
 *
 * @author Couno
 * ProjectName: reggie
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public Rmg<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        // 将提交的密码进行MD5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));

        // 根据页面提交的用户名查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 如果没有查询到则返回登录失败
        if (emp == null){
            return Rmg.error("登录失败!没有此账号");
        }

        // 密码比对，如果不一致则返回失败
        if(!emp.getPassword().equals(password)){
            return Rmg.error("登录失败!密码错误");
        }

        // 查看员工状态,是否被禁用,禁用返回失败
        if(emp.getStatus() == 0){
            return Rmg.error("账号已经被禁用!");
        }

        // 登录成功将员工id存入session中并返回成功
        request.getSession().setAttribute("employee",emp.getId());
        return Rmg.success(emp);
    }


    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public Rmg<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return Rmg.success("退出成功");
    }

    /**
     * 新增员工，插入新员工
     */
    @PostMapping
    public Rmg<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工,员工信息:{}",employee.toString());

        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));

        // employee.setCreateTime(LocalDateTime.now());
        // employee.setUpdateTime(LocalDateTime.now());
        //
        // Long empId = (Long)request.getSession().getAttribute("employee");
        //
        // employee.setCreateUser(empId);
        // employee.setUpdateUser(empId);

        employeeService.save(employee);

        return Rmg.success("新增员工成功");
    }

    /**
     * 分页查询
     */
    @GetMapping("/page")
    public Rmg<Page<Employee>> page(int page, int pageSize, String name){
        log.info("page:{},pageSize:{},name :{}",page,pageSize,name);
        Page<Employee> pageInfo = new Page<>(page,pageSize);

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);

        queryWrapper.orderByDesc(Employee::getUpdateTime);

        employeeService.page(pageInfo,queryWrapper);

        return Rmg.success(pageInfo);
    }

    /**
     * 用户信息更新
     */
    @PutMapping
    public Rmg<String> update(HttpServletRequest request,@RequestBody Employee employee){
        // 打印传入信息
        log.info(employee.toString());

        Long empId = (Long) request.getSession().getAttribute("employee");
        BaseContext.setCurrentId(empId);
        // employee.setUpdateTime(LocalDateTime.now());
        // employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return Rmg.success("员工信息修改成功");
    }

    @GetMapping("/{id}")
    public Rmg<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息.....");
        Employee employee = employeeService.getById(id);
        if (employee != null){
            return Rmg.success(employee);
        }
        return Rmg.error("没有查询到对应员工信息");
    }

}
