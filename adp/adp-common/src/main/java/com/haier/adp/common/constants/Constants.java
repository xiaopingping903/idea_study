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
    //角色合集
    public interface SYS_ROLES{
        //企业成员
        String ORG_MEMBER = "orgMember";
        //系统管理员
        String SYSTEM_ADMIN = "systemAdmin";
        //企业管理员
        String ORG_ADMIN = "orgAdmin";
        //项目经理
        String PROJECT_MANAGER = "projectManager";
        //项目开发者
        String PROJECT_DEV = "projectDeveloper";
        //项目测试人员
        String PROJECT_TEST = "projectTester";
    }
}
