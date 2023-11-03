package com.ivestszheng.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ivestszheng.reggie.entity.Employee;

import javax.servlet.http.HttpServletRequest;

public interface EmployeeService extends IService<Employee> {
    Employee login(String username,String password);

    void logout(HttpServletRequest request);

}
