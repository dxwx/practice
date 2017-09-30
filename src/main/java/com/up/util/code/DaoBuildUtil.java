package com.up.util.code;


/**
 * Created by jinhaishan on 17/9/27.
 */
public class DaoBuildUtil {

    public static StringBuilder getDaoFileHead(String tableName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package " + Constant.BASE_PACKAGE+ "."+ TargetTypeEnum.DAO.getPackageName() +";");
        stringBuilder.append("\n\n");
        stringBuilder.append("import ").append(Constant.BASE_PACKAGE).append(".").append(TargetTypeEnum.ENTITY.getPackageName()).append(".").append(EntityBuildUtil.getEntityType(tableName)).append(";\n\n");
        stringBuilder.append("import java.util.List;\n");
        stringBuilder.append("import java.util.Map;\n");
        String fileComment = FileInfoUtil.getFileComment(null);
        stringBuilder.append(fileComment);
        stringBuilder.append("\n");
        stringBuilder.append("public interface ");
        stringBuilder.append(FileInfoUtil.getClassName(TargetTypeEnum.DAO, tableName));
        stringBuilder.append(" {");
        stringBuilder.append("\n\n");
        return stringBuilder;
    }

    public static StringBuilder getDaoFileBody(String tableName) {
        StringBuilder body = new StringBuilder();
        DaoMethod daoMethod = new DaoMethod(tableName);
        return body.append(daoMethod.getInsertMethod()).append(daoMethod.getUpdateMethod()).append(daoMethod.getDeleteMethod()).append(daoMethod.getGetMethod()).append(daoMethod.getListMethod()).append(daoMethod.getTotalCountMethod());
    }


    /**
     * 获取列表方法名
     * @return
     */
    public static String  getListMethodName(String entityType) {
        return "get" + entityType + "List";
    }


    /**
     * 获取查询实体方法名
     * @return
     */
    public static String getGetMethodName(String entityType) {
        return "get" + entityType;
    }


    /**
     * 获取更新方法名
     * @return
     */
    public static String getUpdateMethodName(String entityType) {
        return "update" + entityType;
    }


    /**
     * 获取写入方法名
     * @return
     */
    public static String getInsertMethodName(String entityType) {
        return "insert" + entityType;
    }


    /**
     * 获取总数方法名
     * @return
     */
    public static String getTotalCountMethodName(String entityType) {
        return "get" + entityType + "TotalCount";
    }

    /**
     * 获取更新方法名
     * @return
     */
    public static String getDeleteMethodName(String entityType) {
        return "delete" + entityType;
    }



}
