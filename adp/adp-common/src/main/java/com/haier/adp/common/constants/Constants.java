package com.haier.adp.common.constants;

/**
 * Created by rsdeep on 2016/12/19.
 */
public class Constants {
    public interface POINT_RULE{
        int LOST_POINTS_PER_DAY = 10;
    }
    public interface MONEY_RULE{
        double DETAIL_LOST_MONEY_PARA = 1;
        int LIST_MONEY_EACH_POINT = 500;
    }
    public interface PROJECT_TYPE{
        //资源池类项目
        String RESOURCE_POOL_PROJECT = "res";
        //系统运维类项目
        String SYSTEM_OPERTAION_PROJECT = "sys";
    }
    public interface PAY_STATUS{
        //资源池类项目
        String PAID = "true";
        //系统运维类项目
        String NOT_PAID = "false";
    }
}
