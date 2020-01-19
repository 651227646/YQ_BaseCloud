package com.yq.bc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description
 * @date 2020/1/19
 */
public class ClassLoaderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassLoaderUtils.class);

    private ClassLoaderUtils() {
    }

    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        AssertUtils.notBlank(className, "要加载的类名不能为空");
        return Class.forName(className);
    }

    public static <T> List<T> scanClassWithAnnotation(List<File> clazFilezList, ClassLoaderUtils.ClassFileFilter filter) {
        List<T> clazzs = null;
        if (clazFilezList != null && !clazFilezList.isEmpty()) {
            List<File> newClazFilezList = (List)clazFilezList.stream().filter((filex) -> {
                return filter.filter(filex);
            }).collect(Collectors.toList());
            clazzs = new ArrayList();
            Iterator var4 = newClazFilezList.iterator();

            while(var4.hasNext()) {
                File file = (File)var4.next();
                String clazzName = FileUtils.getClassName((File)null, file);

                try {
                    T objInstance = InvokeUtils.getInstance(clazzName);
                    clazzs.add(objInstance);
                } catch (Exception var8) {
                    LOGGER.warn(String.format("从文件[%s]加载类[%s]失败", file.getAbsoluteFile(), clazzName), var8);
                }
            }
        }

        return clazzs;
    }

    @FunctionalInterface
    public interface ClassFileFilter {
        boolean filter(File file);
    }
}
