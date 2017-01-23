/*
 * Copyright (c) 2014 杭州端点网络科技有限公司
 */

package com.haier.adp.config.service;

import com.haier.adp.config.model.Config;

/**
 * Mail: bandd@haier.com<br>
 * Date: 2015-03-27 12:00 PM  <br>
 * Author: bandiandian
 */
public interface ConfigReadService {

    /**
     * 根据配置名获取配置项
     * @param key 配置名
     * @return 配置项
     */
    Config getByKey(String key);



}
