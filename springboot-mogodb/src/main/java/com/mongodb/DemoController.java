package com.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 */
@RestController
public class DemoController {
    @Autowired
    private DemoInfoRepository demoInfoRepository;
    @RequestMapping("save")
    public String save(){
        DemoInfo demoInfo = new DemoInfo();
        demoInfo.setName("张三");
        demoInfo.setAge(20);
        demoInfoRepository.save(demoInfo);

        demoInfo = new DemoInfo();
        demoInfo.setName("李四");
        demoInfo.setAge(30);
        demoInfoRepository.save(demoInfo);

        return "ok";
    }

    @RequestMapping("find")
    public List<DemoInfo> find(){
        return demoInfoRepository.findAll();
    }

    @RequestMapping("findByName")
    public DemoInfo findByName(){
        return demoInfoRepository.findByName("张三");
    }
}
