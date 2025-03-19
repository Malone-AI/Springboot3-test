package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.exception.CustomerException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理文件上传和下载的接口
 */
@RestController
@RequestMapping("/files")
public class FileController {

    @GetMapping("/download/{filename}")
    public void download(@PathVariable String fileName, HttpServletResponse response) throws Exception {
        // 找到文件位置
        String filePath = System.getProperty("user.dir") + "/files/";// 获取当前项目的根路径 （java_project的绝对路径）
        String realPath = filePath + fileName;

        // 判断文件是否存在
        boolean exist = FileUtil.exist(realPath);
        if (!exist) {
            throw new CustomerException("文件不存在");
        }

        // 读取文件的字节流
        byte[] bytes = FileUtil.readBytes(realPath);
        ServletOutputStream os = response.getOutputStream();

        // 输出流对象把文件写出到客户端
        os.write(bytes);
        os.flush();
        os.close();
    }
}
