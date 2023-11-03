package com.ivestszheng.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ivestszheng.common.R;
import com.ivestszheng.reggie.entity.Employee;
import com.ivestszheng.reggie.mapper.EmployeeMapper;
import com.ivestszheng.reggie.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Autowired  // 或者使用 @Resource
    private EmployeeMapper employeeMapper;
    @Override
    public Employee login(String username, String password) {
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, username);
        Employee emp = employeeMapper.selectOne(queryWrapper);

        if (emp == null || !emp.getPassword().equals(password)) {
            return null;
        } else if (emp.getStatus() == 0) {
            return null;
        }

        return emp;
    }

    @Override
    public void logout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
    }
}
