package com.ivestszheng.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ivestszheng.common.R;
import com.ivestszheng.reggie.entity.Employee;
import com.ivestszheng.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        // String password = employee.getPassword();
        // password = DigestUtils.md5DigestAsHex(password.getBytes());
        //
        // LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<Employee>();
        // queryWrapper.eq(Employee::getUsername, employee.getUsername());
        // Employee emp = employeeService.getOne(queryWrapper);
        //
        // if(emp == null || !emp.getPassword().equals(password)){
        //     return R.error("登陆失败");
        // } else if(emp.getStatus() == 0){
        //     return R.error("账号已禁用");
        // }
        //
        // request.getSession().setAttribute("employee", emp.getId());
        // return R.success(emp);
        Employee emp = employeeService.login(employee.getUsername(), employee.getPassword());

        if (emp == null) {
            return R.error("登录失败");
        }

        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工登出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 清理 session 中保存的当前员工的 id
        // HttpServletRequest 对象中，可以通过 getSession() 方法获取当前请求的会话对象。
        // request.getSession().removeAttribute("employee");
        employeeService.logout(request);
        return R.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping()
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工，员工信息：{}", employee.toString());
        // 设置初始密码123456，需要进行 md5 加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        // 获得当前登录用户的 id
        Long empId = (Long) request.getSession().getAttribute("employee");

        employee.setCreateUser(empId);
        employee.setUpdateUser(empId);

        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 员工信息分布查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},name = {}", page, pageSize, name);

        // 构造分页构造器
        Page pageInfo = new Page(page, pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }
}
