package com.example.controller;

import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

    @Resource
    AdminService adminService;
    @Resource
    UserService userService;

    @GetMapping("/hello")
    public Result hello() {
        int a = 1 / 0;
        return Result.success("hello world");
    }

    @GetMapping("/admin")
    public Result admin(String name) {
        String admin = adminService.admin(name);
        return Result.success(admin);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    @PostMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if ("ADMIN".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if("USER".equals(account.getRole())) {
            dbAccount = userService.login(account);
        } else {
            throw new CustomerException("非法请求");
        }
        return Result.success(dbAccount);
    }
}
