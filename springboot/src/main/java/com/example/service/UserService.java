package com.example.service;

import cn.hutool.core.util.StrUtil;
import com.example.entity.Account;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.mapper.UserMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Resource
    UserMapper userMapper;

    public String user(String name) {
        if ("user".equals(name)) {
            return "user";
        } else {
            throw new CustomerException("账号错误");
        }
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.selectAll(user);
        return PageInfo.of(userList);
    }

    public void add(User user) {
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser != null) {
            throw new CustomerException("账号重复");
        }
        if (StrUtil.isBlank(user.getPassword())) {
            user.setPassword("user");
        }
        if (StrUtil.isBlank(user.getName())) {
            user.setName(user.getUsername());
        }
        if (StrUtil.isBlank(user.getEmail())) {
            user.setEmail(user.getUsername() + "@user.com");
        }
        userMapper.insert(user);
    }

    public void update(User user) {
        userMapper.updateById(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            userMapper.deleteById(user.getId());
        }
    }

    public List<User> SelectByIds(List<Integer> ids) {
        return userMapper.selectByIds(ids);
    }

    public User login(Account account) {
        // 1. 验证账号是否存在
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (dbUser == null) {
            throw new CustomerException("账号不存在");
        }

        // 2. 验证密码
        if (!dbUser.getPassword().equals(account.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }

        return dbUser;
    }

    public void register(User user) {
        this.add(user);
    }

    public User selectById(String userId) {
        return userMapper.selectByid(userId);
    }
}