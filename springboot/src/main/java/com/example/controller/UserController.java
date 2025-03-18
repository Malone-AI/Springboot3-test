package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    UserService userService;

    @GetMapping("/selectAll")
    public Result selectAll(User user) {
        List<User> users = userService.selectAll(user);
        return Result.success(users);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             User user) {
        PageInfo<User> userPageInfo = userService.selectPage(pageNum, pageSize, user);
        return Result.success(userPageInfo);
    }

    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        userService.add(user);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.update(user);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<User> list) {
        userService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/export")
    public void exportData(User user, HttpServletResponse response) throws Exception{
        // 1. 拿到所有数据
        List<User> users = userService.selectAll(user);
        // 2. 构建ExcelWriter对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(users);
        // 5. 设置输出的文件的名称以及输出流的头信息
        String fileName = URLEncoder.encode("管理员信息", StandardCharsets.UTF_8);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6. 写出到输出流 并关闭 writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }

    @GetMapping("/exportBatch")
    public void exportBatch(@RequestParam String ids, HttpServletResponse response) throws Exception {
        // 将字符串ids转为整数列表
        String[] idArray = ids.split(",");
        List<Integer> idList = new ArrayList<>();
        for (String id : idArray) {
            idList.add(Integer.parseInt(id));
        }

        // 根据id列表查询数据
        List<User> list = userService.SelectByIds(idList);

        // 2. 构建ExcelWriter对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(list);
        // 5. 设置输出的文件的名称以及输出流的头信息
        String fileName = URLEncoder.encode("管理员信息", StandardCharsets.UTF_8);
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6. 写出到输出流 并关闭 writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }

    @PostMapping("/import")
    public Result importData(@RequestParam("file") MultipartFile file) throws Exception {
        // 1. 获取输入流，构建 Reader 对象
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        // 2. 通过Reader 读取 excel 里面的数据
        reader.addHeaderAlias("账号", "username");
        reader.addHeaderAlias("名称", "name");
        reader.addHeaderAlias("电话", "phone");
        reader.addHeaderAlias( "邮箱", "email");
        List<User> list = reader.readAll(User.class); // 将表格数据转换为List<User>
        // 3. 将数据写到数据库
        for (User user : list) {
            userService.add(user);
        }
        return Result.success();
    }
}
