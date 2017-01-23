/*
 * Copyright (c) 2015 杭州端点网络科技有限公司
 */

package com.haier.adp.common.util;

import com.google.common.base.Optional;
import io.terminus.common.exception.ServiceException;

import javax.annotation.Nullable;

/**
 * @author Effet
 */
public final class ServiceUtils {

    public static <T> T throwz(String error) {
        throw new ServiceException(String.valueOf(error));
    }

    public static ServiceException error(String error) {
        return new ServiceException(String.valueOf(error));
    }

    public static void checkResult(boolean expression,  String error) {
        if (!expression) {
            throw new ServiceException(String.valueOf(error));
        }
    }

    // same
    public static <T> T getResult(Optional<T> optional,  String error) {
        return unwrap(optional, error);
    }

    public static <T> T unwrap(Optional<T> optional,  String error) {
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new ServiceException(String.valueOf(error));
    }
}
