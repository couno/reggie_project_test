package com.couno.reggie.controller;

import com.couno.reggie.common.Rmg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

/**
 * ClassName: CommonController
 * date: 2022/11/12 16:34
 *
 * @author Couno
 * ProjectName: reggie
 */

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public Rmg<String> upload(MultipartFile file){
        log.info(file.toString());
        // 原始文件名
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        //使用uuid重新生成文件名，防止文件名称重复，造成文件覆盖
        String fileName =  UUID.randomUUID() + suffix;

        File dir = new File(basePath);
        if (!dir.exists()){
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Rmg.success(fileName);
    }


    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            // 1.输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            response.setContentType("image/jpeg");
            // 2.输出流，通过输出流将文件写回浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            // 3.这是一个读取文件和写文件的过程
            int len = 0;
            byte[] bytes = new byte[1024];
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            // 4.关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (FileNotFoundException s){
            log.info("系统找不到指定的文件");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
