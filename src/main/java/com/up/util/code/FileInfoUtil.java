package com.up.util.code;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.io.File;

/**
 * Created by jinhaishan on 17/9/27.
 */
public class FileInfoUtil {

    /**
     * 获取文件注释
     *
     * @param comment 注释内容
     * @return
     */
    public static String getFileComment(String comment) {
        StringBuilder stringBuilder = new StringBuilder("/**");
        stringBuilder.append("\n");
        stringBuilder.append(" * Created by ");
        stringBuilder.append(System.getProperty("user.name"));
        stringBuilder.append(" on ");
        stringBuilder.append(DateTime.now().toString("yy/mm/dd"));
        stringBuilder.append("\n");
        if(StringUtils.isNotBlank(comment))
        {
            stringBuilder.append(" * ");
            stringBuilder.append(comment);
            stringBuilder.append("\n");
        }
        stringBuilder.append(" */");
        return stringBuilder.toString();
    }


    /**
     * 获取待生成的目标文件的文件名
     * @param tableName
     * @param targetFileTypeEnum
     * @return
     */
    public static String getTargetFileName(String tableName, TargetTypeEnum targetFileTypeEnum) {
        String part1 = targetFileTypeEnum.getClassPreFix() + CamelCaseUtil.getCamelCase(tableName, targetFileTypeEnum != TargetTypeEnum.MAPPER ? true : false);
        return part1 + targetFileTypeEnum.getFilePostfix();
    }

    /**
     * 根据表名和类型获取类型
     *
     * @param targetFileTypeEnum
     * @param tableName
     * @return
     */
    public static String getClassName(TargetTypeEnum targetFileTypeEnum, String tableName) {
        return targetFileTypeEnum.getClassPreFix() + CamelCaseUtil.getCamelCase(tableName, true) + targetFileTypeEnum.getClassPostfix();
    }

    /**
     * 获取类文件的结尾
     *
     * @return
     */
    public static StringBuilder getClassFileTail() {
        return new StringBuilder("\n}");
    }

    public static StringBuilder getSerialVersion() {
        return new StringBuilder("    private static final long serialVersionUID = 1L;");
    }

    /**
     * 根据path生成文件夹
     *
     * @param path
     */
    public static void createFolder(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    public static String getPathByPackage(String packageName)
    {
        return packageName.replaceAll("\\.", "/") + "/";
    }

    public static String getEntityFolder() {
        String path =  Constant.BASE_PATH + FileInfoUtil.getPathByPackage(Constant.BASE_PACKAGE) +  TargetTypeEnum.ENTITY.getPackageName();
        createFolder(path);
        return path;
    }

    public static String getDaoFolder() {
        String path =  Constant.BASE_PATH + FileInfoUtil.getPathByPackage(Constant.BASE_PACKAGE) +  TargetTypeEnum.DAO.getPackageName();
        createFolder(path);
        return path;
    }

    public static String getMapperFolder() {
        String path =  Constant.BASE_PATH + TargetTypeEnum.MAPPER.getPackageName();
        createFolder(path);
        return path;
    }
}
