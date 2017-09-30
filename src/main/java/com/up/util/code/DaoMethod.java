package com.up.util.code;

/**
 * Created by jinhaishan on 17/9/27.
 */
public class DaoMethod {

    private String entityName;

    private String entityType;

    public DaoMethod(String tableName) {
        entityName = EntityBuildUtil.getEntityName(tableName);
        entityType = EntityBuildUtil.getEntityType(tableName);
    }

    public StringBuilder getListMethod() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n");
        stringBuilder.append("     * 获取实体列表\n");
        stringBuilder.append("     * @param parameterMap").append("\n");
        stringBuilder.append("     * @return").append("\n");
        stringBuilder.append("     */\n");
        stringBuilder.append("     List<").append(entityType).append("> ").append(DaoBuildUtil.getListMethodName(entityType)).append("(").append("Map<String, ? extends Object> parameterMap);\n\n");
        return stringBuilder;
    }


    public StringBuilder getGetMethod() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n");
        stringBuilder.append("     * 根据ID获取实体\n");
        stringBuilder.append("     * @param id").append("\n");
        stringBuilder.append("     * @return").append("\n");
        stringBuilder.append("     */\n");
        stringBuilder.append("    ").append(entityType).append(" ").append(DaoBuildUtil.getGetMethodName(entityType)).append("(").append("String id);\n\n");
        return stringBuilder;
    }



    public StringBuilder getDeleteMethod() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n");
        stringBuilder.append("     * 根据ID删除对应记录\n");
        stringBuilder.append("     * @param id").append("\n");
        stringBuilder.append("     */\n");
        stringBuilder.append("    void ").append(DaoBuildUtil.getDeleteMethodName(entityType)).append("(").append("String id);\n\n");
        return stringBuilder;
    }



    public StringBuilder getUpdateMethod() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n");
        stringBuilder.append("     * 更新操作，ID不能为空\n");
        stringBuilder.append("     * @param ").append(entityName).append("\n");
        stringBuilder.append("     */\n");
        stringBuilder.append("    void ").append(DaoBuildUtil.getUpdateMethodName(entityType)).append("(").append(entityType).append(" ").append(entityName).append(");\n\n");
        return stringBuilder;
    }



    public StringBuilder getInsertMethod() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n");
        stringBuilder.append("     * 写入实体\n");
        stringBuilder.append("     * @param ").append(entityName).append("\n");
        stringBuilder.append("     */\n");
        stringBuilder.append("    void ").append(DaoBuildUtil.getInsertMethodName(entityType)).append("(").append(entityType).append(" ").append(entityName).append(");\n\n");
        return stringBuilder;
    }


    public StringBuilder getTotalCountMethod() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    /**\n");
        stringBuilder.append("     * 获取总数\n");
        stringBuilder.append("     * @param parameterMap").append("\n");
        stringBuilder.append("     * @return").append("\n");
        stringBuilder.append("     */\n");
        stringBuilder.append("     Integer").append(" ").append(DaoBuildUtil.getTotalCountMethodName(entityType)).append("(").append("Map<String, Object> parameterMap);\n\n");
        return stringBuilder;
    }


}
