/*
 *
 *  * Copyright (c) 2014 杭州端点网络科技有限公司
 *
 */

package com.haier.adp.common.util;

import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * Author: haolin
 * On: 12/1/14
 */
public final class Params {

    private Params(){}

    /**
     * 过滤Map中的NULL或""值
     */
    public static Map<String, Object> filterNullOrEmpty(Map<String, Object> criteria) {
        return Maps.filterEntries(criteria, new Predicate<Map.Entry<String, Object>>() {
            @Override
            public boolean apply(Map.Entry<String, Object> entry) {
                Object v = entry.getValue();
                if (v instanceof String) {
                    return !Strings.isNullOrEmpty((String) v);
                }
                return v != null;
            }
        });
    }

    public static  String trimToNull( String str) {
        return str != null ? Strings.emptyToNull(str.replace('\u00A0',' ').trim()) : null;
    }

    public static  String trimToNull( Object obj) {
        return obj != null ? trimToNull(obj.toString()) : null;
    }
}
