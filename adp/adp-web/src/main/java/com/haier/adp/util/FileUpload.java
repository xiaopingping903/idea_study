package com.haier.adp.util;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/16.
 */
public class FileUpload {
    public Map upload(MultipartFile file, HttpServletRequest request,String serverName,String serverPort){
        Map map=new HashMap();
        System.out.println(file.getName()+":"+file.getOriginalFilename());
        boolean flag=false;
        String msg="";
        String msgcontent="";
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();// 文件原名称
                // 判断文件类型
                String type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
                if (type != null) {// 判断文件类型是否为空
                    // 自定义的文件名称
                    String trueFileName = String.valueOf(System.currentTimeMillis()) + file.getOriginalFilename();
                    // 设置存放文件的路径
                    String realPath = request.getSession().getServletContext().getRealPath("/");
                    String path = realPath + trueFileName;
                    System.out.println("存放文件的路径:" + path);
                    msgcontent=msgcontent+"存放文件的路径:" + path+";";
                    String basePath = request.getScheme() + "://" + serverName+ ":" + serverPort+ "/";
                    map.put("attachment", file.getOriginalFilename());
                    map.put("url", basePath + trueFileName);
                    System.out.println("文件下载的路径:" + basePath + trueFileName);
                    msgcontent=msgcontent+"文件下载的路径:" + basePath + trueFileName+";";
                    byte[] bytes = file.getBytes();
                    BufferedOutputStream stream =
                            new BufferedOutputStream(new FileOutputStream(new File(path)));
                    stream.write(bytes);
                    stream.close();
                    System.out.println("文件成功上传到指定目录下");
                    msgcontent=msgcontent+"文件成功上传到指定目录下";
                    flag = true;
                    msg = "文件上传成功";
                } else {
                    System.out.println("文件类型为空");
                    msg = "文件类型不可以为空";
                }
            } catch (Exception e) {
                e.printStackTrace();
                msg = "服务器异常";
            }
        }else{
            msg="文件为空";
        }
        map.put("msgcontent",msgcontent);
        map.put("msg",msg);
        map.put("flag",flag);
        return map;
    }

}
