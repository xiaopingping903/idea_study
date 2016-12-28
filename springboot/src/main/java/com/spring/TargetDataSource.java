package com.spring; /**
 * Created by Administrator on 2016/12/26.
 */

import java.lang.annotation.*;

/**
 * 在方法上使用，用于指定使用哪个数据源
 *
 * @author   单红宇(365384722)
 * @myblog  http://blog.csdn.net/catoop/
 * @create    2016年1月23日
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String name();
}