package com.up.util.code;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by jinhaishan on 17/9/27.
 */
public class TypeUtil {

    private static Map<String, String> typeMap = Maps.newHashMap();

    static {
        typeMap.put("varchar", "String");
        typeMap.put("char", "String");
        typeMap.put("int", "Integer");
        typeMap.put("tinyint", "Integer");
        typeMap.put("bigint", "Long");
        typeMap.put("double", "Double");
    }

    /**
     * 根据mysqlType获取对应的javaType
     * @param mysqlType
     * @return
     */
    public static String getJavaTypeByMysql(String mysqlType)
    {
        String key = mysqlType;
        if(mysqlType.contains("("))
        {
            key = mysqlType.substring(0, key.indexOf("("));
        }
        return typeMap.get(key);
    }
}
