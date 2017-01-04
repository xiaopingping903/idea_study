package com.test.xjs.demo.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.test.xjs.demo.domain.User;
import com.test.xjs.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by xujiashuai on 2016/6/18.
 */
@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @RequestMapping("/query/{page}/{pageSize}")
    public PageInfo query(@PathVariable Integer page, @PathVariable Integer pageSize) {
        if(page!= null && pageSize!= null){
            PageHelper.startPage(page, pageSize);
        }
        List<User> users = userMapper.list();
        return new PageInfo(users);
    }
}

