package com.up.util.code;

import org.apache.commons.lang3.StringUtils;

/**
 * @author jinhaishan
 * @create 17/9/11 19:22
 */
public class CamelCaseUtil {

    public static String getCamelCase(String name, boolean isFirstUpCase) {
        if (StringUtils.isNoneBlank(name)) {
            String[] parts = name.split("_");
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i< parts.length; i++)
            {
                if(isFirstUpCase || i != 0)
                {
                    stringBuilder.append(parts[i].substring(0, 1).toUpperCase());
                    stringBuilder.append(parts[i].substring(1));
                }
                else
                {
                    stringBuilder.append(parts[i]);
                }
            }
            return stringBuilder.toString();
        }
        return name;
    }

    public static void main(String[] args) {
        System.out.println(getCamelCase("test_ma_lu", true));
    }
}
