package com.couno.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.couno.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * ClassName: EmployeeMapper
 * date: 2022/11/10 10:56
 *
 * @author Couno
 * ProjectName: reggie
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
