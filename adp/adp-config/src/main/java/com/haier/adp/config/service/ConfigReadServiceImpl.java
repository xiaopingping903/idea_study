/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package com.haier.adp.config.service;

import com.haier.adp.config.dao.ConfigDao;
import com.haier.adp.config.model.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mail: xiao@terminus.io <br>
 * Date: 2015-03-27 2:51 PM  <br>
 * Author: xiao
 */
@Slf4j
@Service
public class ConfigReadServiceImpl implements ConfigReadService {




    @Autowired
    private ConfigDao configDao;




    @Override
    public Config getByKey(String key) {
       // Response<Config> res = new Response<Config>();
        Config config = null;
        try {
             config = configDao.findByKey(key);
            if (config == null) {
               // res.setError("sla.not.found");
                return config;
            }

           // res.setResult(sla);
        } catch (Exception e) {
            log.error("fail to load sla, cause:{}", e);
           // res.setError("sla.load.fail");
        }
        return config;
    }



}
