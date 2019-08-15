package tech.washmore.autocodeplus.util;

public class Strings {
    public static final char UNDERLINE = '_';

    /**
     * 下划线格式字符串转换为驼峰格式字符串
     *
     * @param param
     * @return
     */
    public static String underlineToCamel(String param, boolean firstUpper) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        //如果不含下划线,直接只处理首字母大小写
        if (!param.contains(String.valueOf(UNDERLINE))) {
            if (firstUpper) {
                return param.substring(0, 1).toUpperCase().concat(param.substring(1));
            }
            return param;
        }

        param = param.toLowerCase();
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                if (++i < len) {
                    sb.append(Character.toUpperCase(param.charAt(i)));
                }
            } else {
                sb.append(c);
            }
            if (sb.length() == 1 && firstUpper) {
                sb = new StringBuilder(sb.toString().toUpperCase());
            }
        }
        return sb.toString();
    }
}
