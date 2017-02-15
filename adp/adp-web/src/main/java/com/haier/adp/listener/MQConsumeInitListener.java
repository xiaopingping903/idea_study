package com.haier.adp.listener;

import com.haier.adp.service.createJenkinsJobIMQCallbackImpl;
import com.haier.adp.service.executeJenkinsJobIMQCallbackImpl;
import com.haier.adp.service.updateStoryReleaseTimeIMQCallbackImpl;
import com.haier.interconn.rmq.consumer.DefaultSubscriber;
import com.haier.interconn.rmq.consumer.IMQCallback;
import com.haier.interconn.rmq.consumer.ISubscriber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

@Slf4j
public class MQConsumeInitListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            ISubscriber subscribeService = new DefaultSubscriber();
            //新建消息处理对象
            IMQCallback callback1 = new createJenkinsJobIMQCallbackImpl();
            IMQCallback callback2 = new executeJenkinsJobIMQCallbackImpl();
            IMQCallback callback3 = new updateStoryReleaseTimeIMQCallbackImpl();
            //启动消息接收设置
            subscribeService.start();
            subscribeService.subscribe("paas.sla.createJenkinsJob",callback1);
            subscribeService.subscribe("paas.sla.executeJenkinsJob",callback2);
            subscribeService.subscribe("paas.sla.updateStoryReleaseTime",callback3);
            //缺少close（）
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
