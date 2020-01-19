package com.yq.bc.utils;

import java.util.*;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description 数组、集合、map处理工具包
 * @date 2020/1/19
 */
public class ArraysUtils {
    public ArraysUtils() {
    }

    public static String[] distinctArray(String[] array) {
        if (array != null && array.length >= 1) {
            List<String> list = new ArrayList();

            for(int i = 0; i < array.length; ++i) {
                String tmp = array[i];
                if (!list.contains(tmp) && !StringUtils.isBlank(tmp)) {
                    list.add(tmp);
                }
            }

            String[] arrayResult = new String[list.size()];

            for(int i = 0; i < list.size(); ++i) {
                arrayResult[i] = (String)list.get(i);
            }

            return arrayResult;
        } else {
            return array;
        }
    }

    public static <T> List<T> add2List(List<T> targetList, T obj) {
        if (targetList != null && obj != null) {
            targetList.add(obj);
            return targetList;
        } else {
            return targetList;
        }
    }

    public static <T> List<T> add2List4id(List<T> targetList, T obj) {
        if (targetList != null && obj != null) {
            if (obj.getClass().isArray()) {
                T[] objs = (T[]) obj;

                for(int i = 0; i < objs.length; ++i) targetList.add(objs[i]);
            } else if (obj instanceof List) {
                List<T> objs = (List)obj;
                targetList.addAll(objs);
            } else {
                targetList.add(obj);
            }

            return targetList;
        } else {
            return targetList;
        }
    }

    public static <T> List<T> add2List(List<T> targetList, List<T> obj) {
        if (targetList != null && obj != null) {
            targetList.addAll(obj);
            return targetList;
        } else {
            return targetList;
        }
    }

    public static String array2string(Object[] array, String separator) {
        String result = "";
        if (array != null && array.length > 0) {
            for(int i = 0; i < array.length; ++i) {
                Object obj = array[i];
                String objStr = "NULL";
                if (obj != null) {
                    objStr = obj.toString();
                }

                result = StringUtils.isBlank(result) ? objStr : result + separator + objStr;
            }
        }

        return result;
    }

    public static String[] column2fieldName(String[] columns) {
        String[] fieldArray = new String[columns.length];

        for(int i = 0; i < columns.length; ++i) {
            fieldArray[i] = StringUtils.getVarName(columns[i]);
        }

        return fieldArray;
    }

    public static Object[] list2array(List<Object> list) {
        AssertUtils.hasItem(list, "待转换列表不能为空");
        Object[] array = new Object[list.size()];
        array = list.toArray();
        return array;
    }

    public static <T> List<T> distinctNullItem(List<T> list) {
        if (list != null && !list.isEmpty()) {
            List<T> newList = new ArrayList();
            list.forEach((item) -> {
                if (item != null && !newList.contains(item)) {
                    newList.add(item);
                }

            });
            return newList;
        } else {
            return null;
        }
    }

    public static List<String> collectionItem2capitalize(List<String> collection, boolean capitalize) {
        if (!hasItems((Collection)collection)) {
            return collection;
        } else {
            collection = distinctNullItem(collection);

            for(int i = 0; i < collection.size(); ++i) {
                String tmpItem = capitalize ? ((String)collection.get(i)).toUpperCase() : ((String)collection.get(i)).toLowerCase();
                collection.set(i, tmpItem);
            }

            return collection;
        }
    }

    public static List<String> str2StrList4distinct(String str, String separator, boolean distinct) {
        String[] arr = str.split(separator);
        List<String> result = new ArrayList();
        if (!hasNotBlankItems(arr)) {
            return result;
        } else {
            for(int i = 0; i < arr.length; ++i) {
                if (distinct) {
                    if (!result.contains(arr[i])) {
                        result.add(arr[i]);
                    }
                } else {
                    result.add(arr[i]);
                }
            }

            return result;
        }
    }

    public static List<String> str2StringList(String str, String separator) {
        return str2StrList4distinct(str, separator, false);
    }

    public static boolean hasItems(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean hasItems(Map<?, ?> map) {
        return map != null && !map.isEmpty();
    }

    public static boolean hasItems(Object... obj) {
        return obj != null && obj.length > 0 && obj[0] != null;
    }

    public static boolean hasNotNullItems(Object... obj) {
        if (!hasItems(obj)) {
            return false;
        } else {
            Object[] var1 = obj;
            int var2 = obj.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Object o = var1[var3];
                if (o != null) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean hasNotBlankItems(String... strs) {
        if (strs == null) {
            return false;
        } else {
            String[] var1 = strs;
            int var2 = strs.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                String str = var1[var3];
                if (!StringUtils.isBlank(str)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static int[] strArray2intArray(Integer defaultValue, String... arr) {
        if (!hasNotBlankItems(arr)) {
            return new int[0];
        } else {
            arr = distinctArray(arr);
            int[] result = new int[arr.length];

            for(int i = 0; i < arr.length; ++i) {
                if (defaultValue == null) {
                    result[i] = Integer.valueOf(arr[i]);
                } else {
                    result[i] = ConvertUtils.getInteger(arr[i], defaultValue);
                }
            }

            return result;
        }
    }

    public static int[] strArray2intArray(String... arr) {
        return strArray2intArray((Integer)null, arr);
    }

    public static Map<String, Object> removeNullOrEmptyData(Map<String, Object> datas) {
        if (!hasItems(datas)) {
            return datas;
        } else {
            Iterator<String> dataKeyItr = datas.keySet().iterator();
            String key = null;

            while(dataKeyItr.hasNext()) {
                key = (String)dataKeyItr.next();
                if (datas.get(key) == null) {
                    dataKeyItr.remove();
                } else if (datas.get(key) instanceof Collection) {
                    if (!hasItems((Collection)datas.get(key))) {
                        dataKeyItr.remove();
                    }
                } else if (datas.get(key).getClass().isArray()) {
                    if (!hasItems((Object[])((Object[])datas.get(key)))) {
                        dataKeyItr.remove();
                    }
                } else if (datas.get(key) instanceof Map && !hasItems((Map)datas.get(key))) {
                    dataKeyItr.remove();
                }
            }

            return datas;
        }
    }

}
