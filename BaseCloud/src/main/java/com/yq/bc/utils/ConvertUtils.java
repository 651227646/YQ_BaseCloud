package com.yq.bc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description
 * @date 2020/1/19
 */
public class ConvertUtils {

    static Logger LOGGER = LoggerFactory.getLogger(ConvertUtils.class);

    public ConvertUtils() {
    }

    public static Object convert2T(Class<?> type, Object obj) {
        Object result = null;
        if (obj != null && !type.equals(obj.getClass())) {
            if (!type.isAssignableFrom(Byte.TYPE) && !Byte.class.isAssignableFrom(type) && type != Byte.class && type != Byte.TYPE) {
                if (!Short.TYPE.isAssignableFrom(type) && !Short.class.isAssignableFrom(type) && type != Short.class && type != Short.TYPE) {
                    if (!Integer.TYPE.isAssignableFrom(type) && !Integer.class.isAssignableFrom(type) && type != Integer.class && type != Integer.TYPE) {
                        if (!Long.TYPE.isAssignableFrom(type) && !Long.class.isAssignableFrom(type) && type != Long.class && type != Long.TYPE) {
                            if (!Double.TYPE.isAssignableFrom(type) && !Double.class.isAssignableFrom(type) && type != Long.class && type != Long.TYPE) {
                                if (!type.isAssignableFrom(Float.TYPE) && !Float.class.isAssignableFrom(type) && type != Float.class && type != Float.TYPE) {
                                    if (!Boolean.TYPE.isAssignableFrom(type) && !Boolean.class.isAssignableFrom(type) && type != Boolean.class && type != Boolean.TYPE) {
                                        if (!Character.TYPE.isAssignableFrom(type) && !Character.class.isAssignableFrom(type) && type != Character.class && type != Character.TYPE) {
                                            if (!String.class.isAssignableFrom(type) && String.class != type) {
                                                result = type.cast(obj);
                                            } else {
                                                result = String.valueOf(obj);
                                            }
                                        } else {
                                            result = obj.toString().charAt(0);
                                        }
                                    } else if (!"0".equals(obj.toString()) && !obj.toString().startsWith("-")) {
                                        result = obj.toString() != null && ("true".equalsIgnoreCase(obj.toString()) || "yes".equals(obj.toString()) || "y".equalsIgnoreCase(obj.toString()) || "t".equalsIgnoreCase(obj.toString()));
                                    } else {
                                        result = false;
                                    }
                                } else {
                                    result = Float.parseFloat(obj.toString());
                                }
                            } else {
                                result = Double.parseDouble(obj.toString());
                            }
                        } else {
                            result = Long.parseLong(obj.toString());
                        }
                    } else {
                        result = !"true".equals(obj) && !Boolean.TRUE.equals(obj) ? (!"false".equals(obj) && !Boolean.FALSE.equals(obj) ? Integer.parseInt(obj.toString()) : 0) : 1;
                    }
                } else {
                    result = Short.parseShort(obj.toString());
                }
            } else {
                result = Byte.parseByte(obj.toString());
            }

            return result;
        } else {
            return obj;
        }
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClazz, String name) {
        return Enum.valueOf(enumClazz, name);
    }

    public static boolean isSameType(Class<?> type, Object obj) {
        if (obj != null && !type.equals(obj.getClass())) {
            if (type.isPrimitive()) {
                if (type != Byte.TYPE && !Byte.TYPE.isAssignableFrom(type)) {
                    if (type != Short.TYPE && !Short.TYPE.isAssignableFrom(type)) {
                        if (type != Integer.TYPE && !Integer.TYPE.isAssignableFrom(type)) {
                            if (type != Long.TYPE && !Long.TYPE.isAssignableFrom(type)) {
                                if (type != Double.TYPE && !Double.TYPE.isAssignableFrom(type)) {
                                    if (type != Float.TYPE && !Float.TYPE.isAssignableFrom(type)) {
                                        if (type != Boolean.TYPE && !Boolean.TYPE.isAssignableFrom(type)) {
                                            if (type != Character.TYPE && !Character.TYPE.isAssignableFrom(type)) {
                                                return String.class.isAssignableFrom(obj.getClass());
                                            } else {
                                                return Character.class.isAssignableFrom(obj.getClass()) || Character.TYPE.isAssignableFrom(obj.getClass());
                                            }
                                        } else {
                                            return Boolean.class.isAssignableFrom(obj.getClass()) || Boolean.TYPE.isAssignableFrom(obj.getClass());
                                        }
                                    } else {
                                        return Float.class.isAssignableFrom(obj.getClass()) || Float.TYPE.isAssignableFrom(obj.getClass());
                                    }
                                } else {
                                    return Double.class.isAssignableFrom(obj.getClass()) || Double.TYPE.isAssignableFrom(obj.getClass());
                                }
                            } else {
                                return Long.class.isAssignableFrom(obj.getClass()) || Long.TYPE.isAssignableFrom(obj.getClass());
                            }
                        } else {
                            return Integer.class.isAssignableFrom(obj.getClass()) || Integer.TYPE.isAssignableFrom(obj.getClass());
                        }
                    } else {
                        return Short.class.isAssignableFrom(obj.getClass()) || Short.TYPE.isAssignableFrom(obj.getClass());
                    }
                } else {
                    return Byte.class.isAssignableFrom(obj.getClass()) || Byte.TYPE.isAssignableFrom(obj.getClass());
                }
            } else {
                return obj == null || type.isAssignableFrom(obj.getClass());
            }
        } else {
            return true;
        }
    }

    public static <T> boolean isNumber(T obj) {
        return isSameType(Number.class, obj) || isSameType(Integer.class, obj) || isSameType(Short.class, obj) || isSameType(Byte.class, obj) || isSameType(Long.class, obj) || isSameType(Double.class, obj) || isSameType(Float.class, obj);
    }

    public static int getInteger(String value, int defaultInt) {
        int result = defaultInt;
        if (!StringUtils.isBlank(value) && value.matches("^-?\\d+$")) {
            try {
                if (StringUtils.isBlank(value)) {
                    return defaultInt;
                }

                int pointIndex = value.indexOf(".");
                if (pointIndex != -1) {
                    value = value.substring(0, pointIndex);
                }

                result = Integer.valueOf(value);
            } catch (Exception var4) {
            }

            return result;
        } else {
            return defaultInt;
        }
    }

    public static byte getByte(String value, byte defaultByte) {
        byte result = defaultByte;
        if (StringUtils.isBlank(value)) {
            return defaultByte;
        } else {
            try {
                if (StringUtils.isBlank(value)) {
                    return defaultByte;
                }

                int pointIndex = value.indexOf(".");
                if (pointIndex != -1) {
                    value = value.substring(0, pointIndex);
                }

                result = Byte.valueOf(value);
            } catch (Exception var4) {
            }

            return result;
        }
    }

    public static short getShort(String value, byte defaultShort) {
        short result = (short) defaultShort;
        if (!StringUtils.isBlank(value) && value.matches("^-?\\d+$")) {
            try {
                if (StringUtils.isBlank(value)) {
                    return (short) defaultShort;
                }

                int pointIndex = value.indexOf(".");
                if (pointIndex != -1) {
                    value = value.substring(0, pointIndex);
                }

                result = Short.valueOf(value);
            } catch (Exception var4) {
            }

            return result;
        } else {
            return result;
        }
    }

    public static char getChar(String value, char defaultChar) {
        char result = defaultChar;
        if (StringUtils.isBlank(value)) {
            return defaultChar;
        } else {
            try {
                if (StringUtils.isBlank(value)) {
                    return defaultChar;
                }

                result = value.charAt(0);
            } catch (Exception var4) {
            }

            return result;
        }
    }

    public static double getDouble(String value, double defaultDbl) {
        double result = defaultDbl;
        if (StringUtils.isBlank(value)) {
            return defaultDbl;
        } else {
            try {
                result = Double.valueOf(value);
            } catch (Exception var6) {
            }

            return result;
        }
    }

    public static float getFloat(String value, float defaultFloat) {
        float result = defaultFloat;
        if (StringUtils.isBlank(value)) {
            return defaultFloat;
        } else {
            try {
                result = Float.valueOf(value);
            } catch (Exception var4) {
            }

            return result;
        }
    }

    public static long getLong(String value, long defaultLong) {
        long result = defaultLong;
        if (StringUtils.isBlank(value)) {
            return defaultLong;
        } else {
            try {
                result = Long.valueOf(value);
            } catch (Exception var6) {
            }

            return result;
        }
    }

    public static boolean getBoolean(String value, boolean defaultBl) {
        boolean result = defaultBl;
        if (StringUtils.isBlank(value)) {
            return defaultBl;
        } else {
            String[] trueValues = new String[]{"t", "true", "yes", "y", "ok", "1"};

            try {
                result = Arrays.asList(trueValues).contains(value.toLowerCase().trim());
            } catch (Exception var5) {
            }

            return result;
        }
    }

}
