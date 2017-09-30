package com.up.util.code;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.sun.corba.se.impl.io.ObjectStreamClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.joda.time.DateTime;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by jinhaishan on 17/9/25.
 * 根据数据库生成实体类、
 */
public class CodeGenerator {


    private Connection connection = null;

    private Map<String, String> tableCommentMap = null;

    /**
     * @param databaseUser     数据库用户名
     * @param databasePassword 数据库密码
     * @param databaseHost     数据库 ip
     * @param databasePort     数据库端口
     * @param database         数据库名
     */
    public CodeGenerator(String databaseUser, String databasePassword, String databaseHost, Integer databasePort, String database) {
        connection = ConnectionUtil.createMysqlConnection(databaseHost, databasePort, database, databaseUser, databasePassword);
        tableCommentMap = getTableCommentMap();
    }

    public static void main(String[] args) throws Exception {
        CodeGenerator codeGenerator = new CodeGenerator("dev", "458kT*!W", "10.37.253.42", 3307, "qding_neighbor");
        codeGenerator.generate(null);
    }


    /**
     * 根据传入的表名生成相应的文件，如果表名集合为空，表示生成该db中所有表的文件
     *
     * @param tableNames 表名集合
     * @throws SQLException
     */
    public void generate(List<String> tableNames) throws SQLException, IOException, ClassNotFoundException {
        Collection<String> targetTableNames = tableNames;
        if (CollectionUtils.isEmpty(targetTableNames)) {
            targetTableNames = tableCommentMap.keySet();
        }
        List<TableInfo> tableInfos = getTableInfos(targetTableNames);
        String entityFolder = FileInfoUtil.getEntityFolder();
        String daoFolder = FileInfoUtil.getDaoFolder();
        String mapperFolder = FileInfoUtil.getMapperFolder();
        for(TableInfo tableInfo: tableInfos)
        {
            generateEntity(tableInfo, entityFolder);
            generateDao(tableInfo.getTableNanme(), daoFolder);
            generateMapper(tableInfo, mapperFolder);
        }

    }

    /**
     * 生成dao
     * @param tableName
     * @param folder
     * @throws IOException
     */
    private void generateDao(String tableName, String folder) throws IOException {
        StringBuilder head = DaoBuildUtil.getDaoFileHead(tableName);
        StringBuilder body = DaoBuildUtil.getDaoFileBody(tableName);
        StringBuilder tail = FileInfoUtil.getClassFileTail();
        File file = new File(folder, FileInfoUtil.getTargetFileName(tableName, TargetTypeEnum.DAO));
        Files.write(head.append(body).append(tail), file, Charset.defaultCharset());
    }

    /**
     * 生成mapper
     * @param tableInfo
     * @param folder
     * @throws IOException
     */
    private void generateMapper(TableInfo tableInfo, String folder) throws IOException {
        File file = new File(folder, FileInfoUtil.getTargetFileName(tableInfo.getTableNanme(), TargetTypeEnum.MAPPER));
        Files.write(MapperBuildUtil.getMapperContent(tableInfo), file, Charset.defaultCharset());

    }

    /**
     * 生成entity
     * @param tableInfo
     * @param folder
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void generateEntity(TableInfo tableInfo, String folder) throws IOException, ClassNotFoundException {
        StringBuilder head = EntityBuildUtil.getEntityFileHead(tableInfo);
        StringBuilder fields = EntityBuildUtil.getEntityFields(tableInfo.getColumnInfos());
        StringBuilder tail = FileInfoUtil.getClassFileTail();
        File file = new File(folder, FileInfoUtil.getTargetFileName(tableInfo.getTableNanme(), TargetTypeEnum.ENTITY));
        Files.write(head.append(fields).append(tail), file, Charset.defaultCharset());
    }


    /**
     * 获取当前DB中所有表名和对应注释
     *
     * @return
     */
    private Map<String, String> getTableCommentMap() {
        String sql = "show table status";
        Map<String, String> tableCommentMap = Maps.newHashMap();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tableCommentMap.put(resultSet.getString("Name"), resultSet.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableCommentMap;

    }


    /**
     * 根据表名获取表信息
     *
     * @param tableNames
     * @return
     */
    private List<TableInfo> getTableInfos(Collection<String> tableNames) {

        List<TableInfo> tableInfos = Lists.newArrayList();
        for (String tableName : tableNames) {
            tableInfos.add(getTableInfo(tableName));
        }
        return tableInfos;
    }


    /**
     * 获取指定表名的表信息
     *
     * @param tableName
     * @return
     */
    private TableInfo getTableInfo(String tableName) {
        String sql = "show full fields from " + tableName;
        TableInfo tableInfo = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<ColumnInfo> columnInfoList = Lists.newArrayList();
            tableInfo = new TableInfo(tableName, tableCommentMap.get(tableName), columnInfoList);
            while (resultSet.next()) {
                ColumnInfo columnInfo = new ColumnInfo(resultSet.getString("Field"), resultSet.getString("Type"), resultSet.getString("Comment"));
                columnInfoList.add(columnInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tableInfo;
    }
}
