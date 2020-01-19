package com.yq.bc.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 成都一方思致科技有限公司
 *
 * @author yuanqi
 * @description 字符串工具包，用于字符串相关处理
 * @date 2020/1/19
 */
public class StringUtils {

    private static Logger logger = LoggerFactory.getLogger(StringUtils.class);
    public static final String EMPTY = "";
    private static String KEY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()1234567890{}:\"';,./<>?-=";
    private static final char[] intToBase64 = new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'};
    private static final char[] intToAltBase64 = new char[]{'!', '"', '#', '$', '%', '&', '\'', '(', ')', ',', '-', '.', ':', ';', '<', '>', '@', '[', ']', '^', '`', '_', '{', '|', '}', '~', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '?'};

    private StringUtils() {
    }

    public static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer(KEY);
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        int range = buffer.length();

        for(int i = 0; i < length; ++i) {
            sb.append(buffer.charAt(random.nextInt(range)));
        }

        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isBlank(String str) {
        return str == null || "".equals(str) || "".equals(str.trim());
    }

    public static boolean notBlank(String str) {
        return !isBlank(str);
    }

    public static String stringList2String(List<String> strList) {
        return stringList2String(strList, "");
    }

    public static boolean inLength(String str, int minLength, int maxLength) {
//        AssertUtils.isLessThan(minLength, maxLength, "最小长度不能大于最大长度");
        // 最小长度不能大于最大长度
        if (isBlank(str)) {
            return minLength > 0;
        } else {
            int length = length(str);
            return length >= minLength && length <= maxLength;
        }
    }

    public static boolean inLengthMore(String str, int minLength) {
        return inLength(str, minLength, 2147483647);
    }

    public static boolean inLengthLess(String str, int maxLength) {
        return inLength(str, -2147483648, maxLength);
    }

    public static String stringList2String(List<String> strList, String append) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < strList.size(); ++i) {
            if (i == strList.size() - 1) {
                sb.append((String)strList.get(i));
            } else {
                sb.append((String)strList.get(i) + append);
            }
        }

        return sb.toString();
    }

    public static String StringArrays2String(String[] array, String regex) {
        StringBuilder sb = new StringBuilder();
        String[] var3 = array;
        int var4 = array.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String temp = var3[var5];
            if (!isBlank(temp)) {
                sb.append((isBlank(sb.toString()) ? "" : regex) + temp);
            }
        }

        return sb.toString();
    }

    public static String defaultString(Object obj) {
        return obj == null ? "" : obj.toString();
    }

    public static String join(String oldText, String concatText, String separator) {
        return isBlank(oldText) ? concatText : oldText + separator + concatText;
    }

    public static String join(String oldText, Object concatObj, String separator) {
        return isBlank(oldText) ? concatObj.toString() : oldText + separator + concatObj.toString();
    }

    public static String join(Collection<String> strList, String separator) {
        String text = "";

        for(Iterator strItr = strList.iterator(); strItr.hasNext(); text = join(text, (String)strItr.next(), separator)) {
        }

        return text;
    }

    public static String padding(char padStr, int length) {
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; ++i) {
            sb.append(padStr);
        }

        return sb.toString();
    }

    public static String remove(String str, String remove) {
        return !isBlank(str) && !isBlank(remove) ? replace(str, remove, "", -1) : str;
    }

    public static String replace(String text, String searchString, String replacement, int max) {
        if (!isBlank(text) && !isBlank(searchString) && replacement != null && max != 0) {
            int start = 0;
            int end = text.indexOf(searchString, start);
            if (end == -1) {
                return text;
            } else {
                int replLength = searchString.length();
                int increase = replacement.length() - replLength;
                increase = increase < 0 ? 0 : increase;
                increase *= max < 0 ? 16 : (max > 64 ? 64 : max);

                StringBuilder buf;
                for(buf = new StringBuilder(text.length() + increase); end != -1; end = text.indexOf(searchString, start)) {
                    buf.append(text.substring(start, end)).append(replacement);
                    start = end + replLength;
                    --max;
                    if (max == 0) {
                        break;
                    }
                }

                buf.append(text.substring(start));
                return buf.toString();
            }
        } else {
            return text;
        }
    }

    public static String replaceVariable(String text, String variableName, String value) {
        return isBlank(text) ? "" : text.replace("${" + variableName + "}", value).replace("{{" + variableName + "}}", value);
    }

    public static String replaceXmlContent(String xmlContent) {
        if (isBlank(xmlContent)) {
            return xmlContent;
        } else {
            xmlContent = replace(xmlContent, "&", "&amp;", -1);
            xmlContent = replace(xmlContent, "<", "&lt;", -1);
            xmlContent = replace(xmlContent, ">", "&gt;", -1);
            xmlContent = replace(xmlContent, "\"", "&quot;", -1);
            xmlContent = replace(xmlContent, "'", "&apos;", -1);
            return xmlContent;
        }
    }

    public static String capitalize(String value, boolean capitalize) {
        if (!isEmpty(value)) {
            StringBuilder sb = new StringBuilder(value.length());
            if (capitalize) {
                sb.append(Character.toUpperCase(value.charAt(0)));
            } else {
                sb.append(Character.toLowerCase(value.charAt(0)));
            }

            sb.append(value.substring(1));
            return sb.toString();
        } else {
            return value;
        }
    }

    public static String getVarName(String name, String separator) {
        name = getTuofeng(name, separator);
        name = getNo1IsLowerCharString(name);
        return name;
    }

    public static String getVarName(String name) {
        return getVarName(name, "_");
    }

    public static String getTuofeng(String name, String separator) {
        if (name.startsWith(separator)) {
            name = name.substring(1, name.length() - 1);
        }

        String[] word = name.split(separator);
        if (word != null) {
            String temp = "";
            name = "";

            for(int i = 0; i < word.length; ++i) {
                temp = word[i];
                if (!isBlank(temp)) {
                    temp = getNo1IsUpperCharString(temp.toLowerCase());
                    name = name + temp;
                }
            }
        }

        return name;
    }

    public static String getTuofeng(String name) {
        return getTuofeng(name, "_");
    }

    public static String getNo1IsUpperCharString(String name) {
        name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
        return name;
    }

    public static String getNo1IsLowerCharString(String name) {
        name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
        return name;
    }

    public static String getPropertyName(String name) {
        return getPropertyName(name, "_");
    }

    public static String getPropertyName(String name, String separator) {
        return getTuofeng(name, separator);
    }

    public static String getDbFieldName(String varName, String separator) {
        String columnName = "";
        //待转换为DB字段名的变量名不能为空
//        AssertUtils.notBlank(varName, "待转换为DB字段名的变量名不能为空");
        Character lastChar = ' ';

        for(int i = 0; i < varName.length(); ++i) {
            Character c = varName.charAt(i);
            if (i == 0) {
                c = Character.toLowerCase(c);
            }

            if (c >= 'A' && c <= 'Z') {
                columnName = columnName + (separator.equals(lastChar.toString()) ? "" : separator) + Character.toLowerCase(c);
            } else {
                columnName = columnName + c;
            }

            lastChar = c;
        }

        return columnName;
    }

    public static String getDbFieldName(String varName) {
        return getDbFieldName(varName, "_");
    }

    public static String distinctString(String sourceString, String distinctStr) {
        if (!isBlank(sourceString) && !isBlank(distinctStr)) {
            int index = sourceString.lastIndexOf(distinctStr);
            return index > 1 ? replace(sourceString.substring(0, index), distinctStr, "", -1) + sourceString.substring(index, sourceString.length()) : sourceString;
        } else {
            return sourceString;
        }
    }

    public static boolean containsVariable(String inStr, String varName) {
        if (!isBlank(inStr) && !isBlank(varName)) {
            return inStr.contains("${" + varName + "}") || inStr.contains("{{" + varName + "}}");
        } else {
            return false;
        }
    }

    public static boolean containsPojoVariable(String inStr, String varName) {
        if (!isBlank(inStr) && !isBlank(varName)) {
            return inStr.matches("([\\s\\S]*)(\\$\\{{1}" + varName + "\\..{1,100}\\}{1}|\\{{2}" + varName + "\\..{1,100}\\}{2})([\\s\\S]*)") || inStr.matches("([\\s\\S]*)(\\$\\{{1}" + varName + "\\}{1}|\\{{2}" + varName + "\\}{2})([\\s\\S]*)");
        } else {
            return false;
        }
    }

    public static boolean containsVariable(String inStr) {
        return !isBlank(inStr) && inStr.length() >= 3 ? inStr.matches("([\\s\\S]*)(\\$\\{{1}.{1,100}\\}{1}|\\{{2}.{1,100}\\}{2})([\\s\\S]*)") : false;
    }

    public static boolean containsVariableHavePoint(String inStr) {
        return !isBlank(inStr) && inStr.length() >= 3 ? inStr.matches("([\\s\\S]*)(\\$\\{{1}(\\w+\\..{1,100})\\}{1}|\\{{2}(\\w+\\..{1,100})\\}{2})([\\s\\S]*)") : false;
    }

    public static List<String> splitVariableName(String inStr, String reg) {
        List<String> vars = new ArrayList();
        if (!isBlank(inStr) && !isBlank(reg)) {
            Pattern pattern = null;

            try {
                pattern = Pattern.compile(reg);
                Matcher matcher = pattern.matcher(inStr);
                String tmpVar = "";

                while(matcher.find()) {
                    tmpVar = matcher.group().trim();
                    if (!isBlank(tmpVar) && !vars.contains(tmpVar.trim())) {
                        vars.add(tmpVar.trim());
                    }
                }
            } catch (Exception var6) {
                logger.error(String.format("从字符串[%s]中抽取变量名[regex=%s]失败", inStr.substring(0, 200), reg), var6);
            }

            return vars;
        } else {
            return vars;
        }
    }

    public static List<String> splitElVariableName(String inStr) {
        return splitVariableName(inStr, "\\$\\{{1}(\\w+\\.?.{1,100})\\}{1}");
    }

    public static List<String> splitBeetlVariableName(String inStr) {
        return splitVariableName(inStr, "\\{{2}.{1,100}\\}{2}");
    }

    public static List<String> splitVariableName(String inStr, Integer type) {
        if (isBlank(inStr)) {
            return new ArrayList();
        } else {
            switch(type) {
                case 0:
                    return splitElVariableName(inStr);
                case 1:
                    return splitBeetlVariableName(inStr);
                default:
                    List<String> vars = splitElVariableName(inStr);
                    vars.addAll(splitBeetlVariableName(inStr));
                    return vars;
            }
        }
    }

    public static boolean matchRegex(String inputStr, String regex, int flags) {
        Pattern pattern = null;
        if (flags != -1) {
            pattern = Pattern.compile(regex, flags);
        } else {
            pattern = Pattern.compile(regex);
        }

        Matcher m = pattern.matcher(inputStr);
        return m.find();
    }

    public static boolean isNetUrl(String url) {
        if (isBlank(url)) {
            return false;
        } else {
            int index = url.lastIndexOf("?");
            if (index != -1) {
                url = url.substring(0, index);
            }

            url = url.toLowerCase();
            return url.matches("^((https|http|ftp|rtsp|mms)?://)?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?(([0-9]{1,3}\\.){3}[0-9]{1,3}|([0-9a-z_!~*'()-]+\\.)*[a-z]{2,6})(:[0-9]{1,5})?((/?)|(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$");
        }
    }

    public static boolean matchRegexCaseInsensitive(String inputStr, String regex) {
        return matchRegex(inputStr, regex, 2);
    }

    public static boolean matchRegex(String inputStr, String regex) {
        return matchRegex(inputStr, regex, -1);
    }

    public static int length(String s) {
        if (s == null) {
            return 0;
        } else {
            char[] c = s.toCharArray();
            int len = 0;

            for(int i = 0; i < c.length; ++i) {
                ++len;
                if (!isLetter(c[i])) {
                    ++len;
                }
            }

            return len;
        }
    }

    public static boolean isLetter(char c) {
        int k = 128;
        return c / k == 0;
    }

    public static String randomUuid(boolean replaceMark) {
        String uuid = UUID.randomUUID().toString();
        if (replaceMark) {
            uuid = uuid.replace("-", "");
        }

        return uuid;
    }

    public static String randomUuid() {
        return randomUuid(true);
    }

    public static String getEncoding(String str) {
        String encode = "GB2312";

        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception var6) {
        }

        encode = "ISO-8859-1";

        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception var5) {
        }

        encode = "UTF-8";

        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception var4) {
        }

        encode = "GBK";

        try {
            if (str.equals(new String(str.getBytes(encode), encode))) {
                return encode;
            }
        } catch (Exception var3) {
        }

        return "";
    }

    public static String toString(byte[] bytes, int radix) {
        return (new BigInteger(1, bytes)).toString(radix);
    }

    public static String byteArrayToAltBase64(byte[] a) {
        return byteArrayToBase64(a, true);
    }

    private static String byteArrayToBase64(byte[] a, boolean alternate) {
        int aLen = a.length;
        int numFullGroups = aLen / 3;
        int numBytesInPartialGroup = aLen - 3 * numFullGroups;
        int resultLen = 4 * ((aLen + 2) / 3);
        StringBuilder result = new StringBuilder(resultLen);
        char[] intToAlpha = alternate ? intToAltBase64 : intToBase64;
        int inCursor = 0;

        int byte0;
        int byte1;
        for(byte0 = 0; byte0 < numFullGroups; ++byte0) {
            byte1 = a[inCursor++] & 255;
            int byte2 = a[inCursor++] & 255;
            result.append(intToAlpha[byte1 >> 2]);
            result.append(intToAlpha[byte1 << 4 & 63 | byte1 >> 4]);
            result.append(intToAlpha[byte1 << 2 & 63 | byte2 >> 6]);
            result.append(intToAlpha[byte2 & 63]);
        }

        if (numBytesInPartialGroup != 0) {
            byte0 = a[inCursor++] & 255;
            result.append(intToAlpha[byte0 >> 2]);
            if (numBytesInPartialGroup == 1) {
                result.append(intToAlpha[byte0 << 4 & 63]);
                result.append("==");
            } else {
                byte1 = a[inCursor++] & 255;
                result.append(intToAlpha[byte0 << 4 & 63 | byte1 >> 4]);
                result.append(intToAlpha[byte1 << 2 & 63]);
                result.append('=');
            }
        }

        return result.toString();
    }
}
