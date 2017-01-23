package com.haier.adp.sla.util; /**
 * Created by Administrator on 2016/12/26.
 */

import java.lang.annotation.*;

/**
 * 在方法上使用，用于指定使用哪个数据源
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}