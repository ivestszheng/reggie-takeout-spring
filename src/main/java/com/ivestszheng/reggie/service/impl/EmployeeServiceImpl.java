package com.ivestszheng.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivestszheng.reggie.entity.Employee;
import com.ivestszheng.reggie.mapper.EmployeeMapper;
import com.ivestszheng.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
