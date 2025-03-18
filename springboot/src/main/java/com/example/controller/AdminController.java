package com.example.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Admin;
import com.example.service.AdminService;
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
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Resource
    AdminService adminService;

    @GetMapping("/selectAll")
    public Result selectAll(Admin admin) {
        List<Admin> admins = adminService.selectAll(admin);
        return Result.success(admins);
    }

    @GetMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Admin admin) {
        PageInfo<Admin> adminPageInfo = adminService.selectPage(pageNum, pageSize, admin);
        return Result.success(adminPageInfo);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.add(admin);
        return Result.success();
    }

    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        adminService.deleteById(id);
        return Result.success();
    }

    @DeleteMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> list) {
        adminService.deleteBatch(list);
        return Result.success();
    }

    @GetMapping("/export")
    public void exportData(Admin admin, HttpServletResponse response) throws Exception{
        // 1. 拿到所有数据
        List<Admin> admins = adminService.selectAll(admin);
        // 2. 构建ExcelWriter对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(admins);
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
        List<Admin> list = adminService.SelectByIds(idList);

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
        List<Admin> list = reader.readAll(Admin.class); // 将表格数据转换为List<Admin>
        // 3. 将数据写到数据库
        for (Admin admin : list) {
            adminService.add(admin);
        }
        return Result.success();
    }
}
