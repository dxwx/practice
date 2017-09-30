package com.up.util.code;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by jinhaishan on 17/9/28.
 */
public class MapperInfo {


    private String tableName;

    private StringBuilder fullEntityName;

    private List<FieldInfo> fieldInfoList;

    private String entityType;


    public MapperInfo(TableInfo tableInfo) {
        tableName = tableInfo.getTableNanme();
        fieldInfoList = Lists.transform(tableInfo.getColumnInfos(), new Function<ColumnInfo, FieldInfo>() {
            public FieldInfo apply(ColumnInfo input) {
                return new FieldInfo(input.getName(), CamelCaseUtil.getCamelCase(input.getName(), false));
            }
        });
        fullEntityName = new StringBuilder(Constant.BASE_PACKAGE).append(".").append(TargetTypeEnum.ENTITY.getPackageName()).append(".").append(EntityBuildUtil.getEntityType(tableName));
        entityType = EntityBuildUtil.getEntityType(tableName);
    }

    public StringBuilder buildMapperHeader() {
        return new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
                "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
                "\n");
    }

    public StringBuilder buildMapperBegin() {
        StringBuilder mapper = new StringBuilder();
        mapper.append("<mapper namespace=\"").append(Constant.BASE_PACKAGE).append(".").append(TargetTypeEnum.DAO.getPackageName()).append(".").append(FileInfoUtil.getClassName(TargetTypeEnum.DAO, tableName)).append("\">\n");
        return mapper;
    }

    public StringBuilder buildInsert() {
        StringBuilder stringBuilder = new StringBuilder("    <insert id=\"");
        stringBuilder.append(DaoBuildUtil.getInsertMethodName(entityType)).append("\" keyProperty=\"id\" parameterType=\"").append(fullEntityName).append("\">\n");
        stringBuilder.append("        insert into activity(<include refid=\"notNullColumns\"/>)\n" + "        values (<include refid=\"notNullValues\"/>)\n" + "    </insert>\n\n");
        return stringBuilder;
    }

    public StringBuilder buildSqls() {
        StringBuilder notNullColumns = new StringBuilder("    <sql id=\"notNullColumns\">\n" + "        <trim suffixOverrides=\",\">\n");
        StringBuilder notNullvalues = new StringBuilder("    <sql id=\"notNullValues\">\n" + "        <trim suffixOverrides=\",\">\n");
        for (FieldInfo fieldInfo : fieldInfoList) {
            notNullColumns.append("            <if test=\"").append(fieldInfo.getName()).append(" != null\">").append(fieldInfo.getColumnName()).append(",</if>\n");
            notNullvalues.append("            <if test=\"").append(fieldInfo.getName()).append(" != null\">#{").append(fieldInfo.getName()).append("},</if>\n");
        }
        notNullColumns.append("        </trim>\n" + "    </sql>\n\n");
        notNullvalues.append("        </trim>\n" + "    </sql>\n\n");
        return notNullColumns.append(notNullvalues);
    }

    public StringBuilder buildResultMap() {
        StringBuilder resultMapBegin = getResultMapBegin();
        StringBuilder resultMapContent = getResultMapContent();
        StringBuilder resultMapEnd = getResultMapEnd();
        return resultMapBegin.append(resultMapContent).append(resultMapEnd);
    }

    private StringBuilder getResultMapEnd() {
        return new StringBuilder("    </resultMap>\n\n");
    }

    private StringBuilder getResultMapContent() {
        StringBuilder stringBuilder = new StringBuilder();
        for (FieldInfo fieldInfo : fieldInfoList) {
            stringBuilder.append("        <result column=\"").append(fieldInfo.getColumnName()).append("\" property=\"").append(fieldInfo.getName()).append("\"/>\n");
        }
        return stringBuilder;
    }

    private StringBuilder getResultMapBegin() {
        return new StringBuilder("    <resultMap id=\"baseResultMap\" type=\"").append(fullEntityName).append("\">\n");
    }

    public StringBuilder buildUpdate() {
        StringBuilder stringBuilder = new StringBuilder("    <update id=\"");
        stringBuilder.append(DaoBuildUtil.getUpdateMethodName(entityType)).append("\" parameterType=\"").append(fullEntityName).append("\">\n");
        stringBuilder.append("        update ").append(tableName).append("\n");
        stringBuilder.append("        <set>\n");
        for(FieldInfo fieldInfo: fieldInfoList)
        {
            if(fieldInfo.getColumnName().equals("id"))
            {
                continue;
            }
            stringBuilder.append("            <if test=\"").append(fieldInfo.getName()).append(" != null\">\n");
            stringBuilder.append("                ").append(fieldInfo.getColumnName()).append(" = #{").append(fieldInfo.getName()).append("},\n");
            stringBuilder.append("            </if>\n");
        }
        stringBuilder.append("        </set>\n");
        stringBuilder.append("        where id = #{id}\n");
        stringBuilder.append("    </update>\n\n");
        return stringBuilder;
    }

    public StringBuilder buildDelete() {
        StringBuilder stringBuilder = new StringBuilder("    <delete id=\"");
        stringBuilder.append(DaoBuildUtil.getDeleteMethodName(entityType)).append("\" parameterType=\"java.lang.String\">\n");
        stringBuilder.append("        delete from ").append(tableName).append("\n");
        stringBuilder.append("        where id = #{id}\n");
        stringBuilder.append("    </delete>\n\n");
        return stringBuilder;
    }

    public StringBuilder buildGet() {
        StringBuilder stringBuilder = new StringBuilder("    <select id=\"");
        stringBuilder.append(DaoBuildUtil.getGetMethodName(entityType)).append("\" resultMap=\"baseResultMap\" parameterType=\"java.lang.String\">\n");
        stringBuilder.append("      select * from ").append(tableName).append(" where id = #{id}\n");
        stringBuilder.append("    </select>\n\n");
        return stringBuilder;
    }

    public StringBuilder buildList() {
        StringBuilder stringBuilder = new StringBuilder("    <select id=\"");
        stringBuilder.append(DaoBuildUtil.getListMethodName(entityType)).append("\"  parameterType=\"java.util.Map\" resultMap=\"baseResultMap\">\n");
        stringBuilder.append("      select * from ").append(tableName).append("\n");
        stringBuilder.append("      <include refid=\"pageWhere\"/>\n");
        stringBuilder.append("    </select>\n\n");
        return stringBuilder;
    }

    public StringBuilder buildTotalCount() {
        StringBuilder stringBuilder = new StringBuilder("    <select id=\"");
        stringBuilder.append(DaoBuildUtil.getTotalCountMethodName(entityType)).append("\" parameterType=\"java.util.HashMap\" resultType=\"java.lang.Integer\">\n");
        stringBuilder.append("        select count(id) from ").append(tableName).append("\n");
        stringBuilder.append("      <include refid=\"pageWhere\"/>\n");
        stringBuilder.append("    </select>\n\n");
        return stringBuilder;

    }

    public StringBuilder builPageWhere() {
        StringBuilder stringBuilder = new StringBuilder("    <sql id=\"pageWhere\">\n");
        stringBuilder.append("        <where>\n");
        for(FieldInfo fieldInfo:fieldInfoList)
        {
            stringBuilder.append("            <if test=\"").append(fieldInfo.getName()).append(" != null\">\n");
            stringBuilder.append("                AND ").append(fieldInfo.getColumnName()).append(" = #{").append(fieldInfo.getName()).append("}\n");
            stringBuilder.append("            </if>\n");
        }
        stringBuilder.append("            limit #{start}, #{size}\n");
        stringBuilder.append("        </where>\n");
        stringBuilder.append("    </sql>\n\n");
        return stringBuilder;
    }

    public StringBuilder builTail() {
        return new StringBuilder("</mapper>\n");
    }
}
