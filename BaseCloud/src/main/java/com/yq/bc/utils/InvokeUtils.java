package com.yq.bc.utils;

import com.yq.bc.exception.ArgumentException;
import com.yq.bc.exception.ReflectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.security.AccessControlException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description 反射工具类
 * @date 2020/1/19
 */
public class InvokeUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvokeUtils.class);

    public InvokeUtils() {
    }

    public static <T> Class<T> getClass(Type type, int i) {
        if (type instanceof ParameterizedType) {
            return getGenericClass((ParameterizedType)type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable)type).getBounds()[0], 0);
        } else if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)type;
            Type[] types = wildcardType.getLowerBounds();
            return (Class)types[i];
        } else {
            return (Class)type;
        }
    }

    public static <T> Class<T> getGenericClass(ParameterizedType parameterizedType, int i) {
        Object genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) {
            return (Class)((ParameterizedType)genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) {
            return (Class)((GenericArrayType)genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) {
            return getClass(((TypeVariable)genericClass).getBounds()[0], 0);
        } else if (genericClass instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType)genericClass;
            if (wildcardType.getUpperBounds() != null && wildcardType.getUpperBounds().length > 1) {
                throw new ArgumentException("暂不支持多重范型解析");
            } else {
                return (Class)wildcardType.getUpperBounds()[0];
            }
        } else {
            return (Class)genericClass;
        }
    }

    public static <T> T getInstance(Class<T> clazz) throws ReflectionException {
        try {
            return clazz.newInstance();
        } catch (InstantiationException var9) {
            throw new ReflectionException(var9.getMessage(), var9.getCause());
        } catch (IllegalAccessException var10) {
            LOGGER.trace("获取clazz[{}]实例对象失败,默认构造函数不可访问，尝试解析静态方法获取实例对象", clazz, var10);
            Method[] methods = clazz.getMethods();
            Method m = null;
            boolean var4 = false;

            try {
                int i = 0;

                for(int length = methods.length; i < length; ++i) {
                    m = methods[i];
                    int modify = m.getModifiers();
                    if (Modifier.isPublic(m.getModifiers()) && Modifier.isStatic(modify) && clazz.isAssignableFrom(m.getReturnType())) {
                        if (m.isVarArgs()) {
                            Object value = Array.newInstance(m.getParameterTypes()[0], 0);
                            return (T) m.invoke((Object)null, value);
                        }

                        return (T) m.invoke((Object)null);
                    }
                }

                throw new ReflectionException("未找到匹配的static方法来获取实例对象");
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NegativeArraySizeException var8) {
                throw new ReflectionException(var10.getMessage(), var10.getCause());
            }
        }
    }

    public static <T> T getInstance(String className) throws ReflectionException {
        try {
            Class<?> clazz = ClassLoaderUtils.loadClass(className);
            return (T) getInstance(clazz);
        } catch (ClassNotFoundException var2) {
            throw new ReflectionException(var2.getMessage(), var2.getCause());
        }
    }

    public static Object invokeStaticMethod(final Class<?> clazz, final String methodName, final Class<?>[] paraTypes, final Object[] values) throws ReflectionException {
        if (clazz == null) {
            return null;
        } else {
            Object result = null;

            try {
                Method staticMethod = clazz.getMethod(methodName, paraTypes);
                if (staticMethod != null) {
                    result = staticMethod.invoke((Object)null, values);
                }

                return result;
            } catch (Exception var6) {
                throw new ReflectionException(var6);
            }
        }
    }

    public static Object invokeStaticMethod(final Class<?> clazz, final String methodName) throws ReflectionException {
        return invokeStaticMethod((Class)clazz, methodName, (Class[])null, (Object[])null);
    }

    public static Object invokeStaticMethod(final String clazzName, final String methodName, final Class<?>[] paraTypes, final Object[] values) {
        Object result = null;

        try {
            Class<?> clazz = ClassLoaderUtils.loadClass(clazzName);
            if (clazz == null) {
                LOGGER.error(String.format("待加载类[%s]不存在", clazzName));
                return null;
            }

            result = invokeStaticMethod(clazz, methodName, paraTypes, values);
        } catch (Exception var6) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error(String.format("调用[%s]static方法[%s]失败", clazzName, methodName), var6);
            }
        }

        return result;
    }

    public static Object invokeStaticMethod(final String clazzName, final String methodName) {
        return invokeStaticMethod((String)clazzName, methodName, (Class[])null, (Object[])null);
    }

    public static Object invokeMethod(final Object object, final String methodName, final Class<?>[] parameterTypes, final Object[] parameters) throws IllegalArgumentException {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException(String.format("对象[%s]中未找到方法[%s(%s)]", object, methodName, parameterTypes));
        } else {
            method.setAccessible(true);

            try {
                return method.invoke(object, parameters);
            } catch (Exception var6) {
                throw convertReflectionExceptionToUnchecked(var6);
            }
        }
    }

    public static Object invokeMethodIfExist(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Object result = null;

        try {
            if (object != null && object.getClass() != null) {
                if (ArraysUtils.hasNotNullItems(object.getClass().getMethods())) {
                    LOGGER.debug(String.format("未能获取到类[%s]方法[%s]", object, methodName));
                }

                Method[] methods = object.getClass().getMethods();
                List<Method> methodList = (List) Arrays.asList(methods).parallelStream().filter((m) -> {
                    return methodName.equals(m.getName());
                }).collect(Collectors.toList());
                if (methodList != null && !methodList.isEmpty()) {
                    Method method = getMethod(object.getClass(), methodName, parameterTypes);
                    if (method != null) {
                        result = method.invoke(object, parameters);
                    }

                    return result;
                }

                LOGGER.debug(String.format("对象[%s]不存在方法[%s]", object.getClass().getCanonicalName(), methodName));
                return null;
            }

            LOGGER.debug(String.format("未能获取对象类[%s]", object == null ? "null" : object.toString()));
            return null;
        } catch (ArgumentException var8) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn(var8.getMessage(), var8);
            }
        } catch (NoSuchMethodException var9) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn(String.format("对象[%s]中无方法[%s]", object, methodName), var9);
            }
        } catch (Exception var10) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.warn("调用方法失败", var10);
            }
        }

        return result;
    }

    public static <T> Method getMethod(Class<T> cls, String methodName, Class<?>[] parameterTypes) throws NoSuchMethodException, SecurityException {
        return cls.getMethod(methodName, parameterTypes);
    }

    protected static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        AssertUtils.notNull(object, "反射object对象不能为空");
        return getDeclaredMethod(object.getClass(), methodName, parameterTypes);
    }

    protected static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        AssertUtils.notNull(clazz, "反射class对象不能为空");
        Method method = null;

        try {
            method = clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException var5) {
            if (clazz.getSuperclass() != Object.class) {
                method = getDeclaredMethod(clazz.getSuperclass(), methodName, parameterTypes);
            }

            if (method == null && LOGGER.isDebugEnabled()) {
                LOGGER.error(String.format("找不到对应的方法[%s]", methodName), var5);
            }
        }

        return method;
    }

    public static Object getFieldValueIfExist(final Object object, final String fieldName) {
        return getFieldValueIfExist(object, fieldName, (Object)null);
    }

    public static Object getFieldValueIfExist(final Object object, final String fieldName, Object defaultValue) {
        Object result = null;

        try {
            Field field = getDeclaredField(object, fieldName);
            if (field == null) {
                return defaultValue;
            }

            result = getFieldValue(object, field);
            if (result == null) {
                result = defaultValue;
            }
        } catch (Exception var5) {
            if (LOGGER.isTraceEnabled()) {
                LOGGER.error(String.format("获取属性值失败(错误将被忽略):%s", var5.getLocalizedMessage()), var5);
            }
        }

        return result;
    }

    public static Object getFieldValue(final Object object, final Field field) throws IllegalArgumentException, IllegalAccessException {
        Object result = null;
        makeAccessible(field);
        result = field.get(object);
        return result;
    }

    public static Object getFieldValue(final Object object, final String fieldName) throws IllegalArgumentException, IllegalAccessException {
        Field field = getDeclaredField(object, fieldName);
        AssertUtils.notNull(field, String.format("对象[%s]中未找到属性[%s]", object, fieldName));
        makeAccessible(field);
        Object result = null;
        result = field.get(object);
        return result;
    }

    public static Map<String, Object> getGetterFields(final Object object, List<String> excludeFieldNames) {
        return getGetterFields(object, (Class)null, excludeFieldNames);
    }

    public static Map<String, Object> getGetterFields(final Object object) {
        return getGetterFields(object, (Class)null, (List)null);
    }

    public static Map<String, Object> getGetterFields(final Object object, final Class<?> topParentClass) {
        return getGetterFields(object, topParentClass, (List)null);
    }

    public static Map<String, Object> getGetterFields(final Object object, final Class<?> topParentClass, List<String> excludeFieldNames) {
        Map<String, Object> fieldMap = new HashMap();
        if (object != null) {
            try {
                Class<?> clazz = object.getClass();
                List<Field> fields = getDeclaredField(clazz, topParentClass);
                if (ArraysUtils.hasItems(fields)) {
                    for(int i = 0; i < fields.size(); ++i) {
                        Field field = (Field)fields.get(i);
                        String fieldName = field.getName();
                        if (!ArraysUtils.hasItems(excludeFieldNames) || !excludeFieldNames.contains(fieldName)) {
                            Method getter = null;
                            String getterMethodName = "";
                            if (field.getType() == Boolean.TYPE || field.getType() == Boolean.TYPE) {
                                if (fieldName.length() > 2 && "is".equals(fieldName.substring(0, 2))) {
                                    getterMethodName = fieldName;
                                } else {
                                    getterMethodName = "is" + StringUtils.getNo1IsUpperCharString(fieldName);
                                }

                                getter = getDeclaredMethod((Object)object, getterMethodName, (Class[])null);
                            }

                            if (getter == null) {
                                getterMethodName = "get" + StringUtils.getNo1IsUpperCharString(fieldName);
                                getter = getDeclaredMethod((Object)object, getterMethodName, (Class[])null);
                                if (getter == null) {
                                    continue;
                                }
                            }

                            fieldMap.put(fieldName, getter.invoke(object));
                        }
                    }
                }
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | SecurityException var11) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.error(String.format("获取类[%s]具有getter方法的字段失败", object.getClass().getCanonicalName()), var11);
                }
            }
        }

        return fieldMap;
    }

    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        setFieldValue(object, fieldName, value, false);
    }

    public static void setFieldValue(final Object object, final String fieldName, final Object value, boolean ignoreNotExist) {
        Field field = getDeclaredField(object, fieldName);
        if (ignoreNotExist) {
            if (field == null) {
                return;
            }
        } else {
            AssertUtils.notNull(field, String.format("对象[%s]中未找到属性[%s]", object, fieldName));
        }

        try {
            makeAccessible(field);
            if (ConvertUtils.isSameType(field.getType(), value)) {
                field.set(object, value);
            } else {
                field.set(object, ConvertUtils.convert2T(field.getType(), value));
            }
        } catch (IllegalAccessException var6) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.error("设置属性值失败(不可能抛出的异常)", var6);
            }
        } catch (RuntimeException var7) {
            throw var7;
        }

    }

    public static void setFieldValueIfExist(final Object object, final String fieldName, final Object value) {
        setFieldValue(object, fieldName, value, true);
    }

    public static <T> Class<T> getSuperClassGenricType(final Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    public static Class getSuperClassGenricType(final Class clazz, final int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            LOGGER.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    LOGGER.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                LOGGER.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }
    }

    protected static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }

    }

    public static Field getDeclaredField(final Object object, final String fieldName) {
        AssertUtils.notNull(object, "object不能为空");
        AssertUtils.hasText(fieldName, "参数[fieldName]不能为空");
        Class superClass = object.getClass();

        while(superClass != Object.class) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException var4) {
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.warn(String.format("对象[%s]中未找到属性[%s]", object, fieldName));
                }

                superClass = superClass.getSuperclass();
            }
        }

        return null;
    }

    public static Map<String, Field> getDeclaredFieldHaveGetter(final Class<?> clazz) {
        return getDeclaredFieldHaveGetter(clazz, Object.class);
    }

    public static Map<String, Field> getDeclaredFieldHaveGetter(final Class<?> clazz, Class<?> topParentClass) {
        List<Field> fieldList = getDeclaredField(clazz, topParentClass);
        Map<String, Field> result = new HashMap();
        if (ArraysUtils.hasItems(fieldList)) {
            for(int i = 0; i < fieldList.size(); ++i) {
                Field field = (Field)fieldList.get(i);
                String fieldName = field.getName();
                if (!"serialVersionUID".equals(fieldName) && !"LOGGER".equals(fieldName) && !"logger".equals(fieldName)) {
                    Method getter = null;
                    String getterMethodName = "";
                    getterMethodName = "get" + StringUtils.getNo1IsUpperCharString(fieldName);
                    getter = getDeclaredMethod((Class)clazz, getterMethodName, (Class[])null);
                    if (getter == null) {
                        if (field.getType() == Boolean.TYPE || field.getType() == Boolean.TYPE) {
                            getterMethodName = "is" + StringUtils.getNo1IsUpperCharString(fieldName);
                            getter = getDeclaredMethod((Class)clazz, getterMethodName, (Class[])null);
                            if (getter == null) {
                                getter = getDeclaredMethod((Class)clazz, fieldName, (Class[])null);
                            }
                        }

                        if (getter == null) {
                            LOGGER.warn("未获取到属性[{}]的getter方法，将其中属性列表中移除", fieldName);
                            fieldList.remove(i--);
                            continue;
                        }
                    }

                    if (result.containsKey(field.getName())) {
                        if (clazz == field.getDeclaringClass()) {
                            result.put(field.getName(), field);
                        }
                    } else {
                        result.put(field.getName(), field);
                    }
                }
            }
        }

        return result;
    }

    public static List<Field> getDeclaredField(final Class<?> clazz) {
        return getDeclaredField(clazz, Object.class);
    }

    public static List<Field> getDeclaredField(Class<?> clazz, Class<?> topParentClass) {
        return getDeclaredField(clazz, topParentClass, true);
    }

    public static List<Field> getDeclaredField(Class<?> clazz, Class<?> topParentClass, boolean distinct) {
        AssertUtils.notNull(clazz, "Class对象不能为null");
        topParentClass = topParentClass == null ? Object.class : topParentClass;
        List<Field> fieldList = new ArrayList();
        Field[] fields = clazz.getFields();
        if (distinct) {
            fieldList = addDistinctFields(clazz, (List)fieldList, Arrays.asList(fields));
        } else {
            ((List)fieldList).addAll(Arrays.asList(fields));
        }

        fields = clazz.getDeclaredFields();
        ((List)fieldList).addAll(Arrays.asList(fields));
        if (clazz != topParentClass) {
            Class superClass = clazz.getSuperclass();

            while(true) {
                List<Field> tmpFieldList = getDeclaredField(superClass, topParentClass, distinct);
                if (distinct) {
                    fieldList = addDistinctFields(clazz, (List)fieldList, tmpFieldList);
                } else {
                    ((List)fieldList).addAll(tmpFieldList);
                }

                if (superClass == topParentClass) {
                    break;
                }

                clazz = superClass;
                superClass = superClass.getSuperclass();
            }
        }

        return (List)fieldList;
    }

    public static List<Field> addDistinctFields(Class<?> clazz, List<Field> fields, List<Field> sourceFields) {
        if (ArraysUtils.hasItems(fields) && ArraysUtils.hasItems(sourceFields)) {
            sourceFields.forEach((field) -> {
                if (field.getDeclaringClass() != clazz && fields.stream().noneMatch((t) -> {
                    return t.getName().equals(field.getName());
                })) {
                    fields.add(field);
                }

            });
            return fields;
        } else {
            return fields;
        }
    }

    public static List<Field> addDistinctField(Class<?> clazz, List<Field> fields, Field field) {
        if (!ArraysUtils.hasItems(fields)) {
            return fields;
        } else {
            if (field.getDeclaringClass() != clazz && fields.stream().noneMatch((t) -> {
                return t.getName().equals(field.getName());
            })) {
                fields.add(field);
            }

            return fields;
        }
    }

    public static List<Field> getFieldWithAnnotation(Class cls, Class annotation) {
        List<Field> fieldList = new ArrayList();
        if (cls.getDeclaredFields() != null) {
            Field[] var3 = cls.getDeclaredFields();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Field field = var3[var5];
                if (field.isAnnotationPresent(annotation)) {
                    fieldList.add(field);
                }
            }
        }

        return fieldList;
    }

    public static void invokeSetterMethod(Object target, String propertyName, Object value) {
        invokeSetterMethod(target, propertyName, value, (Class)null);
    }

    public static Object invokeGetterMethod(Object target, String propertyName) throws ReflectionException {
        try {
            return getFieldValue(target, propertyName);
        } catch (IllegalAccessException | IllegalArgumentException var3) {
            throw new ReflectionException(var3);
        }
    }

    public static void invokeSetterMethod(Object target, String propertyName, Object value, Class<?> propertyType) {
        Class<?> type = propertyType != null ? propertyType : value.getClass();
        String setterMethodName = (propertyName.startsWith("set") ? "" : "set") + StringUtils.capitalize(propertyName, true);
        invokeMethod(target, setterMethodName, new Class[]{type}, new Object[]{value});
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        return convertReflectionExceptionToUnchecked((String)null, e);
    }

    public static RuntimeException convertReflectionExceptionToUnchecked(String desc, Exception e) {
        String memo = desc == null ? "Unexpected Checked Exception." : desc;
        if (!(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException) && !(e instanceof NoSuchMethodException)) {
            if (e instanceof InvocationTargetException) {
                return new RuntimeException(memo, ((InvocationTargetException)e).getTargetException());
            } else {
                return e instanceof RuntimeException ? (RuntimeException)e : new RuntimeException(memo, e);
            }
        } else {
            return new IllegalArgumentException(memo, e);
        }
    }

    public static void setAccessible(AccessibleObject obj) {
        if (!obj.isAccessible()) {
            try {
                obj.setAccessible(true);
            } catch (AccessControlException var2) {
                LOGGER.error("");
            }

        }
    }
}
