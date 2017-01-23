/*
 * Copyright (c) 2015 杭州端点网络科技有限公司
 */

package com.haier.adp.common.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Map;

/**
 * 日期区间，单位为天
 *
 * <p>默认格式 yyyy-MM-dd
 *
 * @author Effet
 * @since 1.0.0
 */
public final class DayRange {

    private static final DateTimeFormatter DTF = DateTimeFormat.forPattern("yyyy-MM-dd");

    /**
     * 时间左区间
     */
    private DateTime startAt;

    /**
     * 时间右区间(开区间)
     */
    private DateTime endAt;

    public DayRange( String startAtText,  String endAtText) {
        if (!Strings.isNullOrEmpty(startAtText)) {
            startAt = DTF.parseDateTime(startAtText).withTimeAtStartOfDay();
        }
        if (!Strings.isNullOrEmpty(endAtText)) {
            endAt = DTF.parseDateTime(endAtText).withTimeAtStartOfDay().plusDays(1);

            if (startAt != null && !startAt.isBefore(endAt)) {
                // 时间错乱时，修正 endAt
                endAt = startAt.plusDays(1);
            }
        }
    }

    public static DayRange of( String startAtText,  String endAtText) {
        return new DayRange(startAtText, endAtText);
    }

    public static DayRange from( String startAtText,  String endAtText) {
        return new DayRange(startAtText, endAtText);
    }

    public Map<String, Object> toMap( String startAtKey,  String endAtKey) {
        Map<String, Object> map = Maps.newHashMap();
        if (!Strings.isNullOrEmpty(startAtKey) && startAt != null) {
            map.put(startAtKey, startAt.toDate());
        }
        if (!Strings.isNullOrEmpty(endAtKey) && endAt != null) {
            map.put(endAtKey, endAt.toDate());
        }
        return map;
    }

    public Date start() {
        return startAt == null ? null : startAt.toDate();
    }

    public Date end() {
        return endAt == null ? null : endAt.toDate();
    }
}
