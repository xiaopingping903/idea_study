package com.haier.adp.service;

import com.alibaba.fastjson.JSON;
import com.haier.adp.sla.service.SlaTOPaasInterfaceService;
import com.haier.interconn.rmq.common.model.MessageWarp;
import com.haier.interconn.rmq.consumer.IMQCallback;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/22.
 */
public class updateStoryReleaseTimeIMQCallbackImpl implements IMQCallback {

    @Autowired
    SlaTOPaasInterfaceService slaTOPaasInterfaceService=new SlaToPaasInterfaceServiceImpl();
    @Override
    public boolean consume(MessageWarp messageWarp) {
        try {
            System.out.println(messageWarp.getString());
            Map<String,String> map= JSON.parseObject(messageWarp.getString(),Map.class);
            long releaseTime=Long.parseLong(map.get("releaseTime").toString());
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            String time=sdf.format(new Date(releaseTime));
            System.out.println(map.get("taskIds")+"------消息体");
            String[] strs=map.get("taskIds").toString().split(",");
            Map<String,String> pp=new HashMap<String, String>();
            for (int i = 0; i <strs.length ; i++) {
                pp.put(strs[i],time);
            }
            slaTOPaasInterfaceService.updateStoryReleaseTime(pp);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
