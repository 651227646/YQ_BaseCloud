package com.yq.bc.utils;

import com.yq.bc.exception.ArgumentException;
import com.yq.bc.exception.LogicException;
import com.yq.bc.pojo.nio.FileInfo;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description 断言工具包，用于校验并抛出相应异常
 * @date 2020/1/19
 */
public class AssertUtils {

    private AssertUtils() {
    }

    public static void notNull(Object obj, String errorMsg) {
        if (obj == null) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void isTrue(boolean expression, String errorMsg) {
        if (!expression) {
            throw new LogicException(errorMsg);
        }
    }

    public static void notNullFile(FileInfo file) {
        if (file == null) {
            throw new ArgumentException("文件对象不能为null");
        }
    }

    public static void notNullFile(File file) {
        if (file == null) {
            throw new ArgumentException("文件对象(java.io.File)不能为null");
        }
    }

    public static void isExistFile(File file) {
        notNullFile(file);
        if (!FileUtils.isFile(file)) {
            throw new ArgumentException(String.format("文件[%s]不存在或不是一个合法文件", file.getAbsolutePath()));
        }
    }

    public static void isNull(Object obj, String errorMsg) {
        if (obj != null) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void notEmpty(String str, String errorMsg) {
        if (StringUtils.isEmpty(str)) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void notBlank(String str, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void isBlank(String str, String errorMsg) {
        if (StringUtils.notBlank(str)) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void hasItem(Collection<?> collection, String errorMsg) {
        if (!ArraysUtils.hasItems(collection)) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void hasItem(Map<?, ?> map, String errorMsg) {
        if (!ArraysUtils.hasItems(map)) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void hasNotNullItem(Object[] arrays, String errorMsg) {
        if (!ArraysUtils.hasNotNullItems(arrays)) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void hasNotNullItem(String errorMsg, Object... arrays) {
        hasNotNullItem(arrays, errorMsg);
    }

    public static void hasItem(Object[] arrays, String errorMsg) {
        if (!ArraysUtils.hasItems(arrays)) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void isExistFile(String path) {
        if (!FileUtils.isFile(path)) {
            throw new ArgumentException(String.format("文件[%s]不存在或不是一个合法文件", path));
        }
    }

    public static void isDirectory(String path) {
        if (!FileUtils.isDirectory(path)) {
            throw new ArgumentException(String.format("地址[%s]不是一个合法的目录", path));
        }
    }

    public static void isUrl(String url) {
        if (!StringUtils.isNetUrl(url)) {
            throw new ArgumentException(String.format("字符串[%s]不是一个合法的url地址", url));
        }
    }

    public static void inRange(long numLong, long min, long max, String errorMsg) {
        if (numLong < min || numLong > max) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void isLessThan(int num, int maxValue, String errorMsg) {
        if (num >= maxValue) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void isLessThan(double num, double maxValue, String errorMsg) {
        if (num >= maxValue) {
            throw new ArgumentException(errorMsg);
        }
    }

    public static void hasText(String str, String errorMsg) {
        if (StringUtils.isBlank(str)) {
            throw new ArgumentException(errorMsg);
        }
    }

}
