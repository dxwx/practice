//package com.up.util.code;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.OutputStreamWriter;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.text.MessageFormat;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import sun.nio.cs.FastCharsetProvider;
//
///**
// * @author chengwei
// * @date 2017年3月10日 下午6:11:02
// * @version V1.0
// * @Description: 根据数据库生成实体类,Mapper,mapper.xml,Service等文件
// */
//public class EntityUtil {
//	// 定义数据库常用类型
//	private static final String TYPE_CHAR = "char";
//
//	private static final String TYPE_DATE = "date";
//
//	private static final String TYPE_TIMESTAMP = "timestamp";
//
//	private static final String TYPE_INT = "int";
//
//	private static final String TYPE_BIGINT = "bigint";
//
//	private static final String TYPE_TEXT = "text";
//
//	private static final String TYPE_BIT = "bit";
//
//	private static final String TYPE_DECIMAL = "decimal";
//
//	private static final String TYPE_BLOB = "blob";
//
//	// 配置文件存放地址
//	private static final String PACKAGEPATH = "D:\\workspace\\entity\\";
//
//	private static final String BEAN_PATH = PACKAGEPATH + "entity_bean";
//
//	private static final String DTO_PATH = PACKAGEPATH + "entity_vo";
//
//	private static final String SERVICE_PATH = PACKAGEPATH + "entity_service";
//
//	private static final String MAPPER_PATH = PACKAGEPATH + "entity_mapper";
//
//	private static final String XML_PATH = PACKAGEPATH + "entity_mapper/xml";
//
//	// 配置文件包名称 , 这些值需要根据各自的项目配置
//	private static final String MODULENAME = "com.goldmantis.appjia";
//
//	private static final String BEAN_PACKAGE = MODULENAME + ".model.wms";
//
//	private static final String DTO_PACKAGE = MODULENAME + ".model.vo";
//
//	private static final String MAPPER_PACKAGE = MODULENAME + ".dao.wms";
//
//	private static final String SERVICE_PACKAGE = MODULENAME + ".service.wms";
//
//	private static final String SERVICEIMPL_PACKAGE = MODULENAME + ".service.wms.impl";
//
//	private static final String CONTROLLER_PACKAGE = MODULENAME + ".controller";
//
//	// 配置数据库连接信息
//	private static final String DRIVERNAME = "com.mysql.jdbc.Driver";
//
//	private static final String USER = "root";
//
//	private static final String PASSWORD = "123456";
//
//	private static final String URL = "jdbc:mysql://localhost:3306/jia_erp?serverTimezone=UTC&useUnicode=true&characterEncoding=utf-8&useSSL=false";
//
//	// 方法统一命名
//	private static final String save = "insert";
//
//	private static final String saveSelective = "insertSelective";
//
//	private static final String update = "updateByPrimaryKey";
//
//	private static final String updateSelective = "updateByPrimaryKeySelective";
//
//	private static final String countTotalNum = "count";
//
//	private static final String queryPage = "list";
//
//	private static final String queryById = " selectByPrimaryKey";
//
//	private static final String delete = "deleteByPrimaryKey";
//
//	// sql语句
//	private static final String showTablesName = "show tables"; // 获取数据库的所有表名
//
//	private static final String showTablesInfo = "show table status"; // 获取数据库的所有表详情信息(包括注释)
//
//	private static final String showFields = "show full fields from "; // 获取指定表的所有字段详情
//
//	// 定义系统中使用到的全局变量
//	private String tableName;
//
//	private String beanName;
//
//	private String dtoName;
//
//	private String serviceName;
//
//	private String serviceImplName;
//
//	private String controllerName;
//
//	private String lowerBeanName;
//
//	private String mapperName;
//
//	private List<String> columns = new ArrayList<>();
//
//	private List<String> types = new ArrayList<>();
//
//	private List<String> comments = new ArrayList<>();
//
//	private Connection conn;
//
//	// 常用的配置项
//	/** 用于指定生成类文件的表, 当值为空时会将数据库的所有表都生成类文件 */
//	private static final String TABLE_NAME = "";
//
//	/** 表名中的这些值将不会转换为类名的一部分 */
//	private static final String[] TABLE_PREFIXS = {"app","bi","jia","lms","p","web","wms","zwms", "v2"};
//
//	/** 指定是否生成分页查询方法, false为不生成, true为生成 */
//	private Boolean useListMeathod = false;
//
//	/**
//	 * 删除指定目录下所有文件,若目录不存在则创建该目录
//	 */
//	private static void mkdirs(Runtime runtime) throws IOException {
//		File file = new File(PACKAGEPATH);
//		if (file.exists()) {
//			runtime.exec("cmd /c del /q/a/f/s "+ file.getAbsolutePath());
//		}
//		file.mkdirs();
//	}
//
//	/**
//	 * 获取连接
//	 */
//	private void initConnection() throws ClassNotFoundException, SQLException {
//		Class.forName(DRIVERNAME);
//		conn = DriverManager.getConnection(URL, USER, PASSWORD);
//	}
//
//	/**
//	 * 获取所有的表名
//	 */
//	private List<String> getTables() throws SQLException {
//		List<String> tables = new ArrayList<>();
//		PreparedStatement pstate = conn.prepareStatement(showTablesName);
//		ResultSet results = pstate.executeQuery();
//		while (results.next()) {
//			String tableName = results.getString(1);
//			// if ( tableName.toLowerCase().startsWith("yy_") ) {
//			tables.add(tableName);
//			// }
//		}
//		return tables;
//	}
//
//	/**
//	 * 根据表名生成实体类名称及所有相关的文件名
//	 */
//	private void initNameByTable(String table) {
//		tableName = table;
//		beanName = getBeanName(table);
//		lowerBeanName = lowerCaseFirstLitter(beanName);
//		dtoName = beanName + "Vo";
//		mapperName = beanName + "Mapper";
//		serviceName = beanName + "Service";
//		serviceImplName = serviceName + "Impl";
//		controllerName = beanName + "Controller";
//	}
//
//	/**
//	 * 根据表名获取实体类名
//	 */
//	private String getBeanName(String table) {
//		StringBuilder entityName = new StringBuilder(table.length());
//		String tableLower = table.toLowerCase();
//		String[] tables = tableLower.split("_");
//		String temp = null;
//		for (int i = 0; i < tables.length; i++) {
//			temp = tables[i].trim();
//			if(canUseTemp(temp)){
//				entityName.append(upperCaseFirstLitter(temp));
//			}
//		}
//		return entityName.toString();
//	}
//
//	/**
//	 * 判断表名前缀是否要加到实体类名上
//	 */
//	private Boolean canUseTemp(String temp) {
//		if(isEmpty(temp)){
//			return false;
//		}
//		for(String tablePrefix: TABLE_PREFIXS){
//			if (tablePrefix.equalsIgnoreCase(temp)) {
//				return false;
//			}
//		}
//		return true;
//	}
//
//	/**
//	 * 获取实体类属性的数据类型
//	 */
//	private String processType(String type) {
//		if (type.indexOf(TYPE_CHAR) > -1) {
//			return "String";
//		} else if (type.indexOf(TYPE_BIGINT) > -1) {
//			return "Long";
//		} else if (type.indexOf(TYPE_INT) > -1) {
//			return "Integer";
//		} else if (type.indexOf(TYPE_DATE) > -1) {
//			return "Date";
//		} else if (type.indexOf(TYPE_TEXT) > -1) {
//			return "String";
//		} else if (type.indexOf(TYPE_TIMESTAMP) > -1) {
//			return "Date";
//		} else if (type.indexOf(TYPE_BIT) > -1) {
//			return "Boolean";
//		} else if (type.indexOf(TYPE_DECIMAL) > -1) {
//			return "BigDecimal";
//		} else if (type.indexOf(TYPE_BLOB) > -1) {
//			return "byte[]";
//		}
//		return null;
//	}
//
//	/**
//	 * 将字段名转换为实体类的属性名
//	 */
//	private String processField(String field) {
//		StringBuilder sb = new StringBuilder(field.length());
//		String[] fields = field.split("_");
//		sb.append(fields[0]);
//		for (int i = 1; i < fields.length; i++) {
//			sb.append(upperCaseFirstLitter(fields[i].trim()));
//		}
//		return sb.toString();
//	}
//
//	/**
//	 * 构建类上面的注释
//	 */
//	private void buildClassComment(BufferedWriter bw, String text) throws IOException {
//		bw.newLine();
//		bw.newLine();
//		bw.write("/**");
//		bw.newLine();
//		bw.write(" * " + text);
//		bw.newLine();
//		bw.write(" */");
//	}
//
//	/**
//	 * 构建方法上面的注释
//	 */
//	private void buildMethodComment(BufferedWriter bw, String text) throws IOException {
//		bw.newLine();
//		bw.write("\t/**" + text + "*/");
//	}
//
//	/**
//	 * 生成实体类
//	 */
//	private void buildEntityBean(List<String> columns, List<String> types, List<String> comments, String tableComment)
//			throws IOException {
//		instanceFolder(BEAN_PATH);
//		BufferedWriter bw = instanceBufferedWriter(BEAN_PATH, beanName + ".java");
//		writeBeanHead(tableComment, bw);
//		writeBeanColumns(columns, types, comments, bw);
////		writeGetSetMethod(columns, types, bw);
////		writeGetByDto(columns, bw);
//		writeEnd(bw);
//	}
//
//	/**
//	 * 写类结尾处代码
//	 */
//	private void writeEnd(BufferedWriter bw) throws IOException {
//		bw.newLine();
//		bw.write("}");
//		bw.newLine();
//		bw.flush();
//		bw.close();
//	}
//
//	/**
//	 * 写根据dto生成实体类方法
//	 */
//	private void writeGetByDto(List<String> columns, BufferedWriter bw) throws IOException {
//		bw.write("public static " + beanName + " get" + beanName + "(" + dtoName + " vo){");
//		bw.newLine();
//		bw.write("\t" + beanName + " " + lowerBeanName + " = new " + beanName + "();");
//		bw.newLine();
//		for (int i = 1; i < columns.size(); i++) {
//			String fieldName = upperCaseFirstLitter(processField(columns.get(i)));
//			bw.write("\t" + lowerBeanName + ".set" + fieldName + "(vo.get" + fieldName + "());");
//			bw.newLine();
//		}
//		bw.write("\treturn " + lowerBeanName);
//		bw.newLine();
//		bw.write("\t}");
//		bw.newLine();
//	}
//
//	/**
//	 * 写实体类的get,set方法
//	 */
//	private void writeGetSetMethod(List<String> columns, List<String> types, BufferedWriter bw) throws IOException {
//		String uppperField = null;
//		String lowerField = null;
//		String tempType = null;
//		for (int i = 0; i < columns.size(); i++) {
//			tempType = processType(types.get(i));
//			lowerField = processField(columns.get(i));
//			uppperField = upperCaseFirstLitter(lowerField);
//			bw.newLine();
//			bw.write("\tpublic void set" + uppperField + "(" + tempType + " " + lowerField + "){");
//			bw.newLine();
//			bw.write("\t\tthis." + lowerField + " = " + lowerField + ";");
//			bw.newLine();
//			bw.write("\t}");
//			bw.newLine();
//			bw.newLine();
//			bw.write("\tpublic " + tempType + " get" + uppperField + "(){");
//			bw.newLine();
//			bw.write("\t\treturn this." + lowerField + ";");
//			bw.newLine();
//			bw.write("\t}");
//			bw.newLine();
//		}
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 写实体类属性代码
//	 */
//	private void writeBeanColumns(List<String> columns, List<String> types, List<String> comments, BufferedWriter bw)
//			throws IOException {
//		for (int i = 0; i < columns.size(); i++) {
//			if (isNotEmpty(comments.get(i))) {
//				bw.write("\t/**" + comments.get(i) + "*/");
//				bw.newLine();
//			}
//			bw.write("\tprivate " + processType(types.get(i)) + " " + processField(columns.get(i)) + ";");
//			bw.newLine();
//			bw.newLine();
//		}
//		bw.newLine();
//	}
//
//	/**
//	 * 写实体类头部代码
//	 */
//	private void writeBeanHead(String tableComment, BufferedWriter bw) throws IOException {
//		bw.write("package " + BEAN_PACKAGE + ";");
//		bw.newLine();
//		bw.write("import java.io.Serializable;");
//		bw.newLine();
//		bw.write("import java.util.Date;");
//		bw.newLine();
//		buildClassComment(bw, tableComment + "实体类");
//		bw.newLine();
//		bw.write("public class " + beanName + " implements Serializable {");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 根据路径创建文件及输出流
//	 */
//	private BufferedWriter instanceBufferedWriter(String parent, String child) throws FileNotFoundException {
//		File beanFile = new File(parent, child);
//		return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(beanFile)));
//	}
//
//	/**
//	 * 根据路径创建目录
//	 */
//	private void instanceFolder(String folderPath) {
//		File folder = new File(folderPath);
//		if (!folder.exists()) {
//			folder.mkdir();
//		}
//	}
//
//	/**
//	 * 生成dto
//	 */
//	private void buildEntityDto(List<String> columns, List<String> types, List<String> comments, String tableComment)
//			throws IOException {
//		instanceFolder(DTO_PATH);
//		BufferedWriter bw = instanceBufferedWriter(DTO_PATH, dtoName + ".java");
//		writeDtoHead(tableComment, bw);
//		writeDtoClumns(columns, types, comments, bw);
//		writeEnd(bw);
//	}
//
//	/**
//	 * 写DTO类属性代码
//	 */
//	private void writeDtoClumns(List<String> columns, List<String> types, List<String> comments, BufferedWriter bw)
//			throws IOException {
//		String type = "";
//		for (int i = 0; i < columns.size(); i++) {
//			if (isNotEmpty(comments.get(i))) {
//				bw.write("\t/**" + comments.get(i) + "*/");
//				bw.newLine();
//			}
//			if (types.get(i).indexOf(TYPE_DATE) > -1 || types.get(i).indexOf(TYPE_TIMESTAMP) > -1 ) {
//				type = "char";
//			}else {
//				type = types.get(i);
//			}
//			bw.write("\tprivate " + processType(type) + " " + processField(columns.get(i)) + ";");
//			bw.newLine();
//			bw.newLine();
//		}
//		bw.newLine();
//	}
//
//	/**
//	 * 写DTO类头部代码
//	 */
//	private void writeDtoHead(String tableComment, BufferedWriter bw) throws IOException {
//		bw.write("package " + DTO_PACKAGE + ";");
//		bw.newLine();
//		buildClassComment(bw, tableComment+"页面显示对象");
//		bw.newLine();
//		bw.write("public class " + dtoName + " {");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 构建方法代码
//	 *
//	 * @param comment 方法注释
//	 * @param returnType 方法返回类型
//	 * @param name 方法名
//	 * @param param 方法参数
//	 */
//	private void buildMethod(BufferedWriter bw, String comment, String returnType, String name, String param) throws IOException{
//		buildMethodComment(bw, comment);
//		bw.newLine();
//		String result = MessageFormat.format("\t{0} {1}({2});", returnType, name, param);
//		bw.write(result);
//		bw.newLine();
//	}
//
//	/**
//	 * 构建Dao文件
//	 */
//	private void buildMapper() throws IOException {
//		instanceFolder(MAPPER_PATH);
//		BufferedWriter bw = instanceBufferedWriter(MAPPER_PATH, mapperName + ".java");
//		writeMapperHead(bw);
//		writeMethod(bw);
//		writeEnd(bw);
//	}
//
//	/**
//	 * 写Mapper及Service中方法代码
//	 */
//	private void writeMethod(BufferedWriter bw) throws IOException {
//		buildMethod(bw, "查询（根据主键ID查询", beanName, queryById, "String id");
//		buildMethod(bw, "删除（根据主键ID删除）", "int", delete, "String id");
//		buildMethod(bw, "添加", "int", save, beanName + " " + lowerBeanName);
//		buildMethod(bw, "添加 （匹配有值的字段）", "int", saveSelective, beanName + " " + lowerBeanName);
//		buildMethod(bw, "修改", "int", update, beanName + " " + lowerBeanName);
//		buildMethod(bw, "修改 （匹配有值的字段）", "int", updateSelective, beanName + " " + lowerBeanName);
//		if (useListMeathod) {
//			buildMethod(bw, "根据条件分页查询", "List<" + beanName + "Dto>", queryPage, beanName + "Param param");
//			buildMethod(bw, "查询总条数", "int", countTotalNum, beanName + "Param param");
//		}
//	}
//
//	/**
//	 * 写Mapper类头部代码
//	 */
//	private void writeMapperHead(BufferedWriter bw) throws IOException {
//		bw.write("package " + MAPPER_PACKAGE + ";");
//		bw.newLine();
//		bw.newLine();
//		bw.write("import " + BEAN_PACKAGE + "." + beanName + ";");
//		bw.newLine();
//		bw.write("import java.util.List;");
//		bw.newLine();
//		bw.write("import java.util.Map;");
//		bw.newLine();
//		bw.write("import org.apache.ibatis.annotations.Param;");
//		buildClassComment(bw, mapperName + "数据库操作接口类");
//		bw.newLine();
//		bw.write("public interface " + mapperName + "{");
//		bw.newLine();
//	}
//
//	/**
//	 * 构建Service文件
//	 */
//	private void buildServie() throws IOException {
//		instanceFolder(SERVICE_PATH);
//		BufferedWriter bw = instanceBufferedWriter(SERVICE_PATH, serviceName + ".java");
//		writeServiceHead(bw);
//		writeMethod(bw);
//		writeEnd(bw);
//	}
//
//	/**
//	 * 写service接口头部代码
//	 */
//	private void writeServiceHead(BufferedWriter bw) throws IOException {
//		bw.write("package " + SERVICE_PACKAGE + ";");
//		bw.newLine();
//		bw.newLine();
//		bw.write("import " + BEAN_PACKAGE + "." + beanName + ";");
//		bw.newLine();
//		bw.write("import java.util.List;");
//		bw.newLine();
//		bw.write("import java.util.Map;");
//		bw.newLine();
//		bw.write("import org.apache.ibatis.annotations.Param;");
//		buildClassComment(bw, serviceName + "数据库操作接口类");
//		bw.newLine();
//		bw.write("public interface " + serviceName + " {");
//		bw.newLine();
//	}
//
//	/**
//	 * 构建ServiceImpl文件
//	 */
//	private void buildServieImpl() throws IOException {
//		instanceFolder(SERVICE_PATH);
//		BufferedWriter bw = instanceBufferedWriter(SERVICE_PATH, serviceImplName + ".java");
//		writeServiceImplHead(bw);
//		writeServieImplMethod(bw);
//		writeEnd(bw);
//	}
//
//	/**
//	 * 写serveImpl中的方法
//	 */
//	private void writeServieImplMethod(BufferedWriter bw) throws IOException {
//		String lowerMapperName = lowerCaseFirstLitter(mapperName);
//		buildServiceImplMethod(bw, beanName, queryById, "String id", lowerMapperName);
//		buildServiceImplMethod(bw, "int", delete, "String id", lowerMapperName);
//		buildServiceImplMethod(bw, "int", save, beanName + " " + lowerBeanName, lowerMapperName);
//		buildServiceImplMethod(bw, "int", saveSelective, beanName + " " + lowerBeanName, lowerMapperName);
//		buildServiceImplMethod(bw, "int", update, beanName + " " + lowerBeanName, lowerMapperName);
//		buildServiceImplMethod(bw, "int", updateSelective, beanName + " " + lowerBeanName, lowerMapperName);
//		if(useListMeathod){
//			buildServiceImplMethod(bw, "List<" + beanName + "Dto>", queryPage, beanName + "Param param", lowerMapperName);
//			buildServiceImplMethod(bw, "int", countTotalNum, beanName + "Param param", lowerMapperName);
//		}
//	}
//
//	/**
//	 * 写serveImpl中的方法
//	 */
//	private void buildServiceImplMethod(BufferedWriter bw, String returnType, String name, String param, String lowerMapperName) throws IOException {
//		bw.write("\t@Override");
//		bw.newLine();
//		bw.write(MessageFormat.format("\tpublic {0} {1}({2})", returnType, name, param));
//		bw.write("{");
//		bw.newLine();
//		bw.write(MessageFormat.format("\t\treturn {0}.{1}({2});", lowerMapperName, name.trim(), param.split(" ")[1]));
//		bw.newLine();
//		bw.write("\t}");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 写serviceImpl头部代码
//	 */
//	private void writeServiceImplHead(BufferedWriter bw) throws IOException {
//		String lowerMapperName = lowerCaseFirstLitter(mapperName);
//		bw.write("package " + SERVICEIMPL_PACKAGE + ";");
//		bw.newLine();
//		bw.newLine();
//		bw.write("import java.util.List;");
//		bw.newLine();
//		bw.write("import org.springframework.beans.factory.annotation.Autowired;");
//		bw.newLine();
//		bw.write("import org.springframework.stereotype.Service;");
//		bw.newLine();
//		bw.write("import " + BEAN_PACKAGE + "." + beanName + ";");
//		bw.newLine();
//		buildClassComment(bw, serviceImplName + "数据库操作接口类");
//		bw.newLine();
//		bw.newLine();
//		bw.write("@Service");
//		bw.newLine();
//		bw.write("public class " + serviceImplName + " implements " + serviceName + " {");
//		bw.newLine();
//		bw.newLine();
//		bw.write("\t@Autowired");
//		bw.newLine();
//		bw.write("\tprivate " + mapperName + " " + lowerMapperName + ";");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 构建实体类映射XML文件
//	 */
//	private void buildMapperXml(List<String> columns, List<String> types, List<String> comments) throws IOException {
//		instanceFolder(XML_PATH);
//		BufferedWriter bw = instanceBufferedWriter(XML_PATH, mapperName + ".xml");
//		writeMapperXmlHead(bw);
//		writeMapperXmlResultMap(columns, comments, bw);
//		buildSQL(bw, columns, types);
//		bw.write("</mapper>");
//		bw.flush();
//		bw.close();
//	}
//
//	/**
//	 * 写Mappper.xml文件映射代码
//	 */
//	private void writeMapperXmlResultMap(List<String> columns, List<String> comments, BufferedWriter bw) throws IOException {
//		bw.write("\t<!--实体映射-->");
//		bw.newLine();
//		bw.write(MessageFormat.format("\t<resultMap id=\"{0}ResultMap\" type=\"{1}.{2}\">",
//				lowerCaseFirstLitter(beanName), BEAN_PACKAGE, beanName));
//		bw.newLine();
//		bw.write("\t\t<!--" + comments.get(0) + "-->");
//		bw.newLine();
//		bw.write("\t\t<id property=\"" + processField(columns.get(0)) + "\" column=\"" + columns.get(0) + "\" />");
//		bw.newLine();
//		int size = columns.size();
//		for (int i = 1; i < size; i++) {
//			bw.write("\t\t<!--" + comments.get(i) + "-->");
//			bw.newLine();
//			bw.write("\t\t<result property=\"" + processField(columns.get(i)) + "\" column=\"" + columns.get(i)
//					+ "\" />");
//			bw.newLine();
//		}
//		bw.write("\t</resultMap>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 写Mapper.xml文件头部代码
//	 */
//	private void writeMapperXmlHead(BufferedWriter bw) throws IOException {
//		bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
//		bw.newLine();
//		bw.write("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\"  \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
//		bw.newLine();
//		bw.write("<mapper namespace=\"" + MAPPER_PACKAGE + "." + mapperName + "\">");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 写Mapper.xml增删改查等语句
//	 */
//	private void buildSQL(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
//		writeClumnList(bw, columns);
//		selectByPrimaryKey(bw, columns, types);
//		insert(bw, columns);
//		insertSelective(bw, columns);
//		updateByPrimaryKey(bw, columns);
//		updateByPrimaryKeySelective(bw, columns);
//		deleteByPrimaryKey(bw, columns, types);
//	}
//
//	/**
//	 * 修改（匹配有值的字段）
//	 */
//	private void updateByPrimaryKey(BufferedWriter bw, List<String> columns) throws IOException {
//		int size = columns.size();
//		bw.write("\t<!-- 修 改-->");
//		bw.newLine();
//		bw.write("\t<update id=\"" + update + "\" parameterType=\"" + lowerCaseFirstLitter(beanName) + "\">");
//		bw.newLine();
//		bw.write("\t\t UPDATE " + tableName);
//		bw.newLine();
//		bw.write("\t\t SET ");
//
//		bw.newLine();
//		String tempField = null;
//		for (int i = 1; i < size; i++) {
//			tempField = processField(columns.get(i));
//			bw.write("\t\t\t " + columns.get(i) + " = #{" + tempField + "}");
//			if (i != size - 1) {
//				bw.write(",");
//			}
//			bw.newLine();
//		}
//
//		bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
//		bw.newLine();
//		bw.write("\t</update>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 修改（匹配有值的字段)
//	 */
//	private void updateByPrimaryKeySelective(BufferedWriter bw, List<String> columns) throws IOException {
//		int size = columns.size();
//		bw.write("\t<!-- 修 改（匹配有值的字段）-->");
//		bw.newLine();
//		bw.write("\t<update id=\"" + updateSelective.trim() + "\" parameterType=\"" + lowerCaseFirstLitter(beanName)
//				+ "\">");
//		bw.newLine();
//		bw.write("\t\t UPDATE " + tableName);
//		bw.newLine();
//		bw.write(" \t\t <set> ");
//		bw.newLine();
//
//		String tempField = null;
//		for (int i = 1; i < size; i++) {
//			tempField = processField(columns.get(i));
//			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
//			bw.newLine();
//			bw.write("\t\t\t\t " + columns.get(i) + " = #{" + tempField + "},");
//			bw.newLine();
//			bw.write("\t\t\t</if>");
//			bw.newLine();
//		}
//
//		bw.newLine();
//		bw.write(" \t\t </set>");
//		bw.newLine();
//		bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
//		bw.newLine();
//		bw.write("\t</update>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * insert方法（匹配有值的字段）
//	 */
//	private void insertSelective(BufferedWriter bw, List<String> columns) throws IOException {
//		int size = columns.size();
//		bw.write("\t<!-- 添加 （匹配有值的字段）-->");
//		bw.newLine();
//		bw.write("\t<insert id=\"" + saveSelective.trim() + "\" parameterType=\"" + lowerCaseFirstLitter(beanName)
//				+ "\">");
//		bw.newLine();
//		bw.write("\t\t INSERT INTO " + tableName);
//		bw.newLine();
//		bw.write("\t\t <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" >");
//		bw.newLine();
//
//		String tempField = null;
//		for (int i = 0; i < size; i++) {
//			tempField = processField(columns.get(i));
//			tempField = 0 == i ? "null" : tempField;
//			bw.write("\t\t\t<if test=\"" + tempField + " != null\">");
//			bw.newLine();
//			bw.write("\t\t\t\t " + columns.get(i) + ",");
//			bw.newLine();
//			bw.write("\t\t\t</if>");
//			bw.newLine();
//		}
//
//		bw.newLine();
//		bw.write("\t\t </trim>");
//		bw.newLine();
//
//		bw.write("\t\t <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" >");
//		bw.newLine();
//
//		tempField = null;
//		for (int i = 0; i < size; i++) {
//			tempField = processField(columns.get(i));
//			bw.write("\t\t\t<if test=\"" + tempField + "!=null\">");
//			bw.newLine();
//			bw.write("\t\t\t\t #{" + tempField + "},");
//			bw.newLine();
//			bw.write("\t\t\t</if>");
//			bw.newLine();
//		}
//
//		bw.write("\t\t </trim>");
//		bw.newLine();
//		bw.write("\t</insert>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 添加insert方法
//	 */
//	private void insert(BufferedWriter bw, List<String> columns) throws IOException {
//		int size = columns.size();
//		bw.write("\t<!-- 添加 -->");
//		bw.newLine();
//		bw.write("\t<insert id=\"" + save + "\" parameterType=\"" + lowerCaseFirstLitter(beanName) + "\">");
//		bw.newLine();
//		selectKey(bw, columns);
//		bw.write("\t\tINSERT INTO " + tableName + "(");
//		bw.newLine();
//		bw.write("\t\t\t<include refid=\"Base_Column_List\" />");
//		bw.newLine();
//		bw.write("\t\t)VALUES(");
//		bw.newLine();
//		for (int i = 0; i < size; i++) {
//			bw.write("\t\t\t");
//			bw.write("#{" + processField(columns.get(i)) + "}");
//			if (i != size - 1) {
//				bw.write(",");
//			}
//			bw.newLine();
//		}
//		bw.write("\t\t");
//		bw.write(") ");
//		bw.newLine();
//		bw.write("\t</insert>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 自动生成主键
//	 *
//	 * <selectKey keyProperty="hospitalId" resultType="int" order="AFTER">
//	 * 		SELECT LAST_INSERT_ID()
//	 * </selectKey>
//	 */
//	private void selectKey(BufferedWriter bw, List<String> columns) throws IOException {
//		bw.write("\t\t<selectKey keyProperty=\"" + processField(columns.get(0))
//				+ "\" resultType=\"int\" order=\"AFTER\">");
//		bw.newLine();
//		bw.write("\t\t\tSELECT LAST_INSERT_ID()");
//		bw.newLine();
//		bw.write("\t\t</selectKey>");
//		bw.newLine();
//	}
//
//	/**
//	 * 删除（根据主键ID删除）
//	 */
//	private void deleteByPrimaryKey(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
//		bw.write("\t<!--删除：根据主键ID删除-->");
//		bw.newLine();
//		bw.write("\t<delete id=\"" + delete + "\" parameterType=\"java.lang." + processType(types.get(0)) + "\">");
//		bw.newLine();
//		bw.write("\t\t DELETE FROM " + tableName);
//		bw.newLine();
//		bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
//		bw.newLine();
//		bw.write("\t</delete>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 查询（根据主键ID查询）
//	 */
//	private void selectByPrimaryKey(BufferedWriter bw, List<String> columns, List<String> types) throws IOException {
//		bw.write("\t<!-- 查询（根据主键ID查询） -->");
//		bw.newLine();
//		bw.write(MessageFormat.format("\t<select id=\"{0}\" resultMap=\"{1}ResultMap\" parameterType=\"java.lang.{2}\">",
//				queryById.trim(), lowerCaseFirstLitter(beanName), processType(types.get(0))));
//		bw.newLine();
//		bw.write("\t\t SELECT");
//		bw.newLine();
//		bw.write("\t\t <include refid=\"Base_Column_List\" />");
//		bw.newLine();
//		bw.write("\t\t FROM " + tableName);
//		bw.newLine();
//		bw.write("\t\t WHERE " + columns.get(0) + " = #{" + processField(columns.get(0)) + "}");
//		bw.newLine();
//		bw.write("\t</select>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 写mapper.xml中所有列名的<sql>标签
//	 */
//	private void writeClumnList(BufferedWriter bw, List<String> columns) throws IOException {
//		int size = columns.size();
//		bw.write("\t<!-- 通用查询结果列-->");
//		bw.newLine();
//		bw.write("\t<sql id=\"Base_Column_List\">");
//		bw.newLine();
//		for (int i = 0; i < size; i++) {
//			bw.write("\t\t" + columns.get(i));
//			if (i != size - 1) {
//				bw.write(",");
//				bw.newLine();
//			}
//		}
//		bw.newLine();
//		bw.write("\t</sql>");
//		bw.newLine();
//		bw.newLine();
//
//		bw.write("\t<sql id=\"Base_Column_List2\">");
//		bw.newLine();
//		for (int i = 0; i < size; i++) {
//			bw.write(MessageFormat.format("\t\t{0} AS {1}", columns.get(i), processField(columns.get(i))));
//			if (i != size - 1) {
//				bw.write(",");
//				bw.newLine();
//			}
//		}
//		bw.newLine();
//		bw.write("\t</sql>");
//		bw.newLine();
//		bw.newLine();
//	}
//
//	/**
//	 * 获取所有的数据库表名及注释
//	 */
//	private Map<String, String> getTableComment() throws SQLException {
//		Map<String, String> maps = new HashMap<>();
//		PreparedStatement pstate = conn.prepareStatement(showTablesInfo);
//		ResultSet results = pstate.executeQuery();
//		while (results.next()) {
//			String tableName = results.getString("NAME");
//			String comment = results.getString("COMMENT");
//			maps.put(tableName, comment);
//		}
//		return maps;
//	}
//
//	public static Boolean isEmpty(String str) {
//		return null == str || "".equals(str);
//	}
//
//	public static Boolean isNotEmpty(String str) {
//		return !isEmpty(str);
//	}
//
//	/**
//	 * 将字符串首字母小写
//	 */
//	public static String lowerCaseFirstLitter(String str) {
//		if(isEmpty(str)){
//			return "";
//		}else {
//			return str.substring(0, 1).toLowerCase() + str.substring(1);
//		}
//	}
//
//	/**
//	 * 将字符串首字母大写
//	 */
//	public static String upperCaseFirstLitter(String str) {
//		if(isEmpty(str)){
//			return "";
//		}else {
//			return str.substring(0, 1).toUpperCase() + str.substring(1);
//		}
//	}
//
//
//	/**
//	 * 根据某一个表生成实体类,dto,service,mapper,mapper.xml
//	 */
//	private void generateByTable(Map<String, String> tableComments, String table) throws SQLException, IOException {
//		columns.clear();
//		types.clear();
//		comments.clear();
//		PreparedStatement pstate = conn.prepareStatement(showFields + table);
//		ResultSet results = pstate.executeQuery();
//		while (results.next()) {
//			columns.add(results.getString("FIELD"));
//			types.add(results.getString("TYPE"));
//			comments.add(results.getString("COMMENT"));
//		}
//		initNameByTable(table);
//		String tableComment = tableComments.get(table);
//		buildEntityBean(columns, types, comments, tableComment);
//		buildEntityDto(columns, types, comments, tableComment);
//		buildMapper();
//		buildMapperXml(columns, types, comments);
//		buildServie();
//		buildServieImpl();
//	}
//
//	/**
//	 * 获取所有的表信息并循环生成相应文件
//	 */
//	public void generate() throws ClassNotFoundException, SQLException, IOException {
//		initConnection();
//		Map<String, String> tableComments = getTableComment();
//		if(isNotEmpty(TABLE_NAME)){
//			generateByTable(tableComments, TABLE_NAME);
//		}else {
//			List<String> tables = getTables();
//			for (String table : tables) {
//				generateByTable(tableComments, table);
//			}
//		}
//		conn.close();
//	}
//
//	public static void main(String[] args) {
//		try {
//			Runtime runtime = Runtime.getRuntime();
//			mkdirs(runtime);
//			new EntityUtil().generate();
//			// 自动打开生成文件的目录
//			runtime.exec("cmd /c start explorer " + PACKAGEPATH);
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//}
