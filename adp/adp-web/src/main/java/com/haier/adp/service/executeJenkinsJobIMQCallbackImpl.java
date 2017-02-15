package com.haier.adp.service;

import com.haier.adp.sla.service.SlaTOPaasInterfaceService;
import com.haier.interconn.rmq.common.model.MessageWarp;
import com.haier.interconn.rmq.consumer.IMQCallback;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Administrator on 2017/1/22.
 */
public class executeJenkinsJobIMQCallbackImpl implements IMQCallback {
    @Autowired
    SlaTOPaasInterfaceService slaTOPaasInterfaceService=new SlaToPaasInterfaceServiceImpl();
    @Override
    public boolean consume(MessageWarp messageWarp) {
        System.out.println(messageWarp.getBody());
        JSONObject jsonObject=JSONObject.fromObject(messageWarp.getBody());
        slaTOPaasInterfaceService.executeJenkinsJob(jsonObject.get("projectName")+"");
        return false;
    }
}
