package com.example.delicious_take_out.controller;

/*
* 文件上传和下载
* */

import com.example.delicious_take_out.common.R;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${file.uploadPath}")
    private String basePath;


    /*文件上传*/
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {//MultipartFile  表示你当前上传文件的对象
        //现在的file是一个临时文件,需要转存到指定位置，否则本次请求完成后临时文件会删除
        log.info(file.toString());

        try{
            //将临时文件转存到指定位置
            file.transferTo(new File(basePath + file.getOriginalFilename()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(file.getOriginalFilename());
    }

    /*文件下载*/
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response, OutputStream outputStream) {
        //输入流，通过输入流读取文件内容
        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fis = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流将文件写回浏览器，在浏览器中展示图片
            ServletOutputStream out = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            //只要长度不为-1，就一直读取bytes数组
            while ((len = fis.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                out.flush();
            }
            //关闭资源
            fis.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
