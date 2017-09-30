package com.up.util.code;

import java.util.List;

/**
 * Created by jinhaishan on 17/9/27.
 */
public class EntityBuildUtil {

    public static StringBuilder getEntityFileHead(TableInfo tableInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package " + Constant.BASE_PACKAGE + "." + TargetTypeEnum.ENTITY.getPackageName() + ";");
        stringBuilder.append("\n\n");
        stringBuilder.append("import lombok.Data;");
        stringBuilder.append("\n\n");
        stringBuilder.append("import java.io.Serializable;");
        stringBuilder.append("\n\n");
        String fileComment = FileInfoUtil.getFileComment(tableInfo.getTableComment());
        stringBuilder.append(fileComment);
        stringBuilder.append("\n");
        stringBuilder.append("@Data\n");
        stringBuilder.append("public class ");
        stringBuilder.append(FileInfoUtil.getClassName(TargetTypeEnum.ENTITY, tableInfo.getTableNanme()));
        stringBuilder.append(" implements Serializable {");
        stringBuilder.append("\n\n").append(FileInfoUtil.getSerialVersion());
        stringBuilder.append("\n\n");
        return stringBuilder;
    }

    public static StringBuilder getEntityFields(List<ColumnInfo> columnInfoList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ColumnInfo columnInfo : columnInfoList) {
            stringBuilder.append("    /**");
            stringBuilder.append("\n");
            stringBuilder.append("     * ").append(columnInfo.getComment());
            stringBuilder.append("\n");
            stringBuilder.append("     */");
            stringBuilder.append("\n");
            stringBuilder.append("    private ").append(TypeUtil.getJavaTypeByMysql(columnInfo.getType())).append(" ").append(CamelCaseUtil.getCamelCase(columnInfo.getName(), false)).append(";");
            stringBuilder.append("\n\n");

        }
        return stringBuilder;
    }

    public static String getEntityType(String tableName) {
        return CamelCaseUtil.getCamelCase(tableName, true) + TargetTypeEnum.ENTITY.getClassPostfix();
    }

     public static String getEntityName(String tableName) {
        return CamelCaseUtil.getCamelCase(tableName, false) + TargetTypeEnum.ENTITY.getClassPostfix();
    }





}
