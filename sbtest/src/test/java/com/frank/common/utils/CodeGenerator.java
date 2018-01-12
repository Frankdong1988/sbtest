package com.frank.common.utils;

import static com.frank.common.utils.StringUtils.javaBoolean;
import static com.frank.common.utils.StringUtils.toCamelCase;
import static com.frank.common.utils.StringUtils.toUpperCaseFirst;
import static com.frank.common.utils.StringUtils.toLowCaseFirst;
import static com.frank.common.utils.StringUtils.formatColumName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.sf.json.JSONObject;
/**
 * 简易代码生成器
 * @author tongyi
 * @date Sep 28, 2013
 */
public class CodeGenerator {
	private final static String	TABLE = "jh_credit_record";//表名
	private final static String	DB = "jhw3.0"; //db
	private final static String	DRIVER = "com.mysql.jdbc.Driver";//驱动
	private final static String	URL = "jdbc:mysql://58.58.180.48:33060/information_schema";//rm-bp1460o9ak1f18vrzo.mysql.rds.aliyuncs.com rm-bp1vxqlotd8bqf601o.mysql.rds.aliyuncs.com
	private final static String	USERNAME = "root";//数据库账号
	private final static String	PASSWORD = "jhw001";//数据库密码
	private final static String	QUERY_SQL = "SELECT `COLUMN_NAME`, `DATA_TYPE`, `COLUMN_TYPE`, `EXTRA`,`COLUMN_COMMENT` FROM `COLUMNS` WHERE `TABLE_SCHEMA` = ? AND `TABLE_NAME` = ? ORDER BY `ORDINAL_POSITION` ASC";
	private final static Map<String, String> JAVA_TYPE = new HashMap<String, String>();

	static {
		JAVA_TYPE.put("int", "int");
		JAVA_TYPE.put("tinyint", "int");
		JAVA_TYPE.put("tinyint1", "boolean");
		JAVA_TYPE.put("smallint", "int");
		JAVA_TYPE.put("mediumint", "int");
		JAVA_TYPE.put("bigint", "long");
		JAVA_TYPE.put("varchar", "string");
		JAVA_TYPE.put("char", "string");
		JAVA_TYPE.put("decimal", "decimal");
		JAVA_TYPE.put("datetime", "date");
		JAVA_TYPE.put("timestamp", "long");
		JAVA_TYPE.put("float", "float");
		JAVA_TYPE.put("date", "date");
		JAVA_TYPE.put("year", "int");
		JAVA_TYPE.put("enum", "string");
		JAVA_TYPE.put("text", "string");
		JAVA_TYPE.put("mediumtext", "string");
	}
	public static void main(String[] args) {
		String tableSchema = DB;
		// String tableName = "tb_order";
		String tableName = TABLE;

		Connection conn = null;
		try {
			Class.forName(DRIVER);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			
			creatMybatisMapper(conn, tableSchema, tableName);
			
			// ibatis Sqlmap file
//			doGeneratorIbatisSqlmap(conn, tableSchema, tableName);

			// 实体字段
//			doGeneratorEntityField(conn, tableSchema, tableName);

			// Webx form
			// doGeneratorForm(conn, tableSchema, tableName);

			// Action field
			// doGeneratorActionField(conn, tableSchema, tableName);

			// service field
			// doGeneratorServiceField(conn, tableSchema, tableName);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void doGeneratorServiceField(Connection connection, String tableSchema, String tableName) throws SQLException {
		String ns = tableName.substring(3);

		PreparedStatement preparedStmt = connection.prepareStatement(QUERY_SQL);
		preparedStmt.setString(1, tableSchema);
		preparedStmt.setString(2, tableName);

		ResultSet resultSet = preparedStmt.executeQuery();
		while (resultSet.next()) {
			String column = resultSet.getString("COLUMN_NAME");
			String jdbcType = resultSet.getString("DATA_TYPE");
			String columnType = resultSet.getString("COLUMN_TYPE");

			String columnType1 = javaBoolean(columnType) ? "tinyint1" : jdbcType;
			String javaType = JAVA_TYPE.get(columnType1);
			if ("string".equalsIgnoreCase(javaType) || "date".equalsIgnoreCase(javaType)) {
				javaType = toUpperCaseFirst(javaType);
			}
			System.out.println(ns + ".set" + toUpperCaseFirst(toCamelCase(column)) + "(" + toCamelCase(column) + ");");
		}
	}

	private static void doGeneratorActionField(Connection connection, String tableSchema, String tableName) throws SQLException {
		PreparedStatement preparedStmt = connection.prepareStatement(QUERY_SQL);
		preparedStmt.setString(1, tableSchema);
		preparedStmt.setString(2, tableName);

		String functionParam = "";

		ResultSet resultSet = preparedStmt.executeQuery();
		while (resultSet.next()) {
			String column = resultSet.getString("COLUMN_NAME");
			String jdbcType = resultSet.getString("DATA_TYPE");
			String columnType = resultSet.getString("COLUMN_TYPE");

			String columnType1 = javaBoolean(columnType) ? "tinyint1" : jdbcType;
			String javaType = JAVA_TYPE.get(columnType1);
			if ("string".equalsIgnoreCase(javaType) || "date".equalsIgnoreCase(javaType)) {
				javaType = toUpperCaseFirst(javaType);
			}
			System.out.println(javaType + " " + toCamelCase(column) + " = formGroup.getField(\"" + toCamelCase(column) + "\").get" + toUpperCaseFirst(javaType)
					+ "Value();");
			functionParam += (StringUtils.isEmpty(functionParam)) ? (javaType + " " + toCamelCase(column)) : (", " + javaType + " " + toCamelCase(column));
		}
		System.out.println();
		System.out.println(functionParam);
	}

	private static void doGeneratorForm(Connection connection, String tableSchema, String tableName) throws SQLException {
		PreparedStatement preparedStmt = connection.prepareStatement(QUERY_SQL);
		preparedStmt.setString(1, tableSchema);
		preparedStmt.setString(2, tableName);

		ResultSet resultSet = preparedStmt.executeQuery();
		while (resultSet.next()) {
			String column = resultSet.getString("COLUMN_NAME");

			System.out.println("<field name=\"" + toCamelCase(column) + "\" />");

		}
	}

	/**
	 * 实体bean
	 * @param connection
	 * @param tableSchema
	 * @param tableName
	 * @throws SQLException
	 */
	private static void doGeneratorEntityField(Connection connection, String tableSchema, String tableName) throws SQLException {
		PreparedStatement preparedStmt = connection.prepareStatement(QUERY_SQL);
		preparedStmt.setString(1, tableSchema);
		preparedStmt.setString(2, tableName);

		ResultSet resultSet = preparedStmt.executeQuery();
		while (resultSet.next()) {
			String column = resultSet.getString("COLUMN_NAME");
			String jdbcType = resultSet.getString("DATA_TYPE");
			String columnType = resultSet.getString("COLUMN_TYPE");
			String columnComment = resultSet.getString("COLUMN_COMMENT"); 

			String columnType1 = javaBoolean(columnType) ? "tinyint1" : jdbcType;
			String javaType = JAVA_TYPE.get(columnType1);
			if ("string".equalsIgnoreCase(javaType) || "date".equalsIgnoreCase(javaType)) {
				javaType = toUpperCaseFirst(javaType);
			} else if ("decimal".equals(javaType)) {
				javaType = "BigDecimal";
			}
//			System.out.println("/** "+columnComment+" **/");
			System.out.println("@ApiModelProperty(value = \""+columnComment+"\")");
			System.out.println("private " + javaType + " " + toCamelCase(column) + ";");

		}
	}
	
	/**
	 * 创建mapper文件
	 * @param connection
	 * @param tableSchema
	 * @param tableName
	 * @throws SQLException
	 */
	private static void creatMybatisMapper(Connection connection, String tableSchema, String tableName) throws SQLException {
		String ns = tableName.substring(3);
		ns = formatColumName(ns);
		System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		System.out.println("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" " + "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		System.out.println("<mapper namespace=\"" + toCamelCase(ns) + "\">");
		System.out.println();
		System.out.println("\t<!-- bean别名 -->");
		System.out.println("\t<typeAlias alias=\"" + ns + "\" type=\"实例bean的路径\" />\n");
		PreparedStatement preparedStmt = connection.prepareStatement(QUERY_SQL);
		preparedStmt.setString(1, tableSchema);
		preparedStmt.setString(2, tableName);

		String resultMapIdName = "resultMap_"+ns;
		String columns = "";
		Map<String, JSONObject> columMap = new LinkedHashMap<String, JSONObject>();
		System.out.println("\t<resultMap type=\"" + ns + "\" id=\"" + resultMapIdName + "\">");
		ResultSet resultSet = preparedStmt.executeQuery();
		String primaryKey = "";
		int n = 0;
		while (resultSet.next()) {
			String column = resultSet.getString("COLUMN_NAME");
			String jdbcType = resultSet.getString("DATA_TYPE");
			String columnType = resultSet.getString("COLUMN_TYPE");
			String columnComment = resultSet.getString("COLUMN_COMMENT");
			if(0 == n) {
				System.out.println("\t\t<id property=\"" + toCamelCase(column) + "\" column=\"" + column + "\"  /> <!--" + columnType + "-->");
			} else {
				System.out.println("\t\t<result property=\"" + toCamelCase(column) + "\" column=\"" + column + "\"  /> <!--" + columnType + "-->");
			}
//			统计所有字段及信息
			columns += (StringUtils.isEmpty(columns)) ? column : "," + column;
			if(n%10 == 0 && n >= 10) {
				columns += "\n";
			}
			JSONObject json = new JSONObject();
			json.put("JDBCTYPE", jdbcType);
			json.put("COLUMNTYPE", columnType);
			json.put("COLUMNCOMMENT", columnComment);
			// 设置主键标识
			if(0 == n) {
				json.put("PRIMARYKEY", "y");
				primaryKey = column;
			} else {
				json.put("PRIMARYKEY", "n");
			}
			columMap.put(column, json);
			n++;
		}
		System.out.println("\t</resultMap>");
		

		String baseColumsIdName = "BaseColums_"+ns;
		String insertIdNameBefor = "insertItemBefor_"+ns;
		String insertIdNameAfter = "insertItemAfter_"+ns;
		String updateItemIdName = "updateItemSql_"+ns;
		String queryItemIdName = "queryItemSql_"+ns;

		// 输出表所有字段
		System.out.println("\t<!-- 表基本字段 -->");
		System.out.println("\t<sql id=\""+baseColumsIdName+"\">");
		System.out.println("\t\t"+columns);
		System.out.println("\t</sql>");
		
		//输出更新条件sql
		System.out.println("\n\t<!-- 更新sql -->");
		System.out.println("\t<sql id=\""+updateItemIdName+"\">");
		for(String key:columMap.keySet()) {
			System.out.println("\t\t<if test=\""+toLowCaseFirst(key)+" != null\">"
					+ "\n\t\t\t"+key+" = #{"+toLowCaseFirst(key)+",jdbcType="+columMap.get(key).getString("JDBCTYPE").toUpperCase()+"},"
			+ "\n\t\t</if>");
		}
		System.out.println("\t</sql>");
		
		//输出插入条件sql(前)
		System.out.println("\n\t<!-- 插入sql前半部分判断 -->");
		System.out.println("\t<sql id=\""+insertIdNameBefor+"\">");
		for(String key:columMap.keySet()) {
			System.out.println("\t\t<if test=\""+toLowCaseFirst(key)+" != null\">\n" + 
					"\t\t\t"+key+",\n" + 
					"\t\t</if>");
		}
		System.out.println("\t</sql>");
		
		//输出插入条件sql(后)
		System.out.println("\n\t<!-- 插入sql后半部分判断 -->");
		System.out.println("\t<sql id=\""+insertIdNameAfter+"\">");
		for(String key:columMap.keySet()) {
			System.out.println("\t\t<if test=\""+toLowCaseFirst(key)+" != null\">\n" + 
					"\t\t\t#{"+toLowCaseFirst(key)+",jdbcType="+columMap.get(key).getString("JDBCTYPE").toUpperCase()+"},\n" + 
					"\t\t</if>");
		}
		System.out.println("\t</sql>");
		
		//输出查询sql
		System.out.println("\n\t<!-- 查询sql枚举 -->");
		System.out.println("\t<sql id=\""+queryItemIdName+"\">");
		for(String key:columMap.keySet()) {
			StringBuilder build = new StringBuilder();
			build.append("\t\t<if test=\""+toLowCaseFirst(key)+" != null\">\n");
			if("DATETIME".equals(columMap.get(key).getString("JDBCTYPE").toUpperCase())) {
				build.append("\t\t\tAND "+key+" BETWEEN CONCAT(DATE_FORMAT("+toLowCaseFirst(key)+",'%y-%m-%d'),'00:00:00') AND CONCAT(DATE_FORMAT("+toLowCaseFirst(key)+",'%y-%m-%d'),'23:59:59')");
			} else {
				build.append("\t\t\tAND "+key+" = #{"+toLowCaseFirst(key)+",jdbcType="+columMap.get(key).getString("JDBCTYPE").toUpperCase()+"},\n\t\t</if>");
			}
			System.out.println(build.toString());
		}
		System.out.println("\t</sql>");
		
		System.out.println("\n\t<!-- 插入语句 -->");
		System.out.println("\t<insert id=\"add"+ns+"\" parameterType=\"" + ns + "\">");
		System.out.println("\t\tINSERT INTO " +tableName+"\n\t\t<trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\">");
		System.out.println("\t\t\t<include refid=\""+insertIdNameBefor+"\"/>");
		System.out.println("\t\t</trim>");
		System.out.println("\t\t<trim prefix=\"VALUES (\" suffix=\")\" suffixOverrides=\",\">");
		System.out.println("\t\t\t<include refid=\""+insertIdNameAfter+"\"/>");
		System.out.println("\t\t</trim>");
		System.out.println("\t</insert>");
		
		System.out.println("\n\t<!-- 删除语句 -->");
		System.out.println("\t<delete id=\"deleteByPrimaryKey_"+ns+"\" parameterType=\"" + ns + "\">");
		System.out.println("\t\tDELETE FROM "+tableName+" WHERE "+primaryKey+" = #{"+primaryKey+"}");
		System.out.println("\t\t\t<include refid=\""+queryItemIdName+"\"/>");
		System.out.println("\t</delete>");
		
		System.out.println("\n\t<!-- 更新语句 -->");
		System.out.println("\t<update id=\"update_"+ns+"\" parameterType=\"" + ns + "\">");
		System.out.println("\t\tUPDATE "+tableName+" \n\t\t<set> "+primaryKey+" = #{"+primaryKey+"}");
		System.out.println("\t\t\t<include refid=\""+updateItemIdName+"\"/> \n\t\t</set>");
		System.out.println("\t</update>");
		
		System.out.println("\n\t<!-- 查询语句 -->");
		System.out.println("\t<select id=\"query_"+ns+"\" parameterType=\"" + ns + "\" resultMap=\""+resultMapIdName+"\">");
		System.out.println("\t\tSELECT <include refid=\""+baseColumsIdName+"\"/> FROM "+tableName+" WHERE 1=1 ");
		System.out.println("\t\t\t<include refid=\""+queryItemIdName+"\"/>");
		System.out.println("\t\tLimit 0,10");
		System.out.println("\t</<select>");
		
		System.out.println("</mapper>");
	}
	
	
	
	/**
	 * 生成Mybati的mapper.xml文件
	 * @param connection
	 * @param tableSchema
	 * @param tableName
	 * @throws SQLException
	 */
	private static void doGeneratorIbatisSqlmap(Connection connection, String tableSchema, String tableName) throws SQLException {
		String ns = tableName.substring(3);

		System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		System.out.println("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" " + "\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
		System.out.println("<mapper namespace=\"" + toCamelCase(ns) + "\">");
		System.out.println();
		System.out.println("\t<typeAlias alias=\"" + ns + "\" type=\"vip.yile.dubaita.domain." + toUpperCaseFirst(toCamelCase(ns)) + "DO\" />");
		System.out.println("\t<typeAlias alias=\"" + ns + "_query\" type=\"vip.yile.dubaita.query." + toUpperCaseFirst(toCamelCase(ns)) + "Query\" />");

		System.out.println("\t<resultMap type=\"" + ns + "\" id=\"" + tableName + "_resultMap\">");

		PreparedStatement preparedStmt = connection.prepareStatement(QUERY_SQL);
		preparedStmt.setString(1, tableSchema);
		preparedStmt.setString(2, tableName);

		String columns = "";
		String insertColumns = "";
		String insertValues = "";
		String updateSets = "";

		ResultSet resultSet = preparedStmt.executeQuery();
		while (resultSet.next()) {
			String column = resultSet.getString("COLUMN_NAME");
			String jdbcType = resultSet.getString("DATA_TYPE");
			String columnType = resultSet.getString("COLUMN_TYPE");
			

			String columnType1 = javaBoolean(columnType) ? "tinyint1" : jdbcType;
			boolean autoIncrement = "auto_increment".equalsIgnoreCase(resultSet.getString("EXTRA"));

			System.out.println("\t\t<result property=\"" + toCamelCase(column) + "\" column=\"" + column + "\"  /> <!--" + columnType + "-->");
//			System.out.println("\t\t<result property=\"" + toCamelCase(column) + "\" column=\"" + column + "\" javaType=\"" + JAVA_TYPE.get(columnType1)
//			+ "\" jdbcType=\"" + ("int".equalsIgnoreCase(jdbcType) ? "INTEGER" : jdbcType.toUpperCase()) + "\" /> <!--" + columnType + "-->");

			columns += (StringUtils.isEmpty(columns)) ? "`" + column + "`" : ", `" + column + "`";
			if (!autoIncrement) {
				insertColumns += (StringUtils.isEmpty(insertColumns)) ? "`" + column + "`" : ", `" + column + "`";

				// 处理两个特殊的字段
				if ("gmt_create".equalsIgnoreCase(column) || "gmt_modified".equalsIgnoreCase(column)) {
					insertValues += (StringUtils.isEmpty(insertValues)) ? "now()" : ", now()";
				} else {
					insertValues += (StringUtils.isEmpty(insertValues)) ? "#" + toCamelCase(column) + "#" : ", #" + toCamelCase(column) + "#";
					updateSets += (StringUtils.isEmpty(updateSets)) ? "`" + column + "`=#" + toCamelCase(column) + "#"
							: ", `" + column + "`=#" + toCamelCase(column) + "#";
				}
			}
		}

		System.out.println("\t</resultMap>");

		System.out.println();
		System.out.println("\t<insert id=\"INSERT\" parameterClass=\"" + ns + "\">");
		System.out.println("\tINSERT INTO `" + tableName + "`(" + insertColumns + ") VALUES (" + insertValues + ");");
		System.out.println("\t\t<selectKey resultClass=\"int\" keyProperty=\"id\">" + "select last_insert_id() as id" + "</selectKey>");
		System.out.println("\t</insert>");

		System.out.println();
		System.out.println("\t<update id=\"UPDATE\" parameterClass=\"" + ns + "\">");
		System.out.println("\tUPDATE `" + tableName + "` SET " + updateSets + ", `gmt_modified`=now() WHERE `id`=#id#");
		System.out.println("\t</update>");

		System.out.println();
		System.out.println("\t<delete id=\"DELETE\" parameterClass=\"int\">");
		System.out.println("\tDELETE FROM `" + tableName + "` WHERE `id`=#id#");
		System.out.println("\t</delete>");

		System.out.println();
		System.out.println("\t<!-- ====== SELECT statement  ======= -->");
		System.out.println("\t<sql id=\"column_list\">");
		System.out.println("\t" + columns);
		System.out.println("\t</sql>");

		System.out.println();
		System.out.println("\t<sql id=\"limit_condition\">");
		System.out.println("\tORDER BY `gmt_create` DESC LIMIT #startPos#, #pageSize#");
		System.out.println("\t</sql>");

		System.out.println();
		System.out.println("\t<sql id=\"query_condition\">");
		System.out.println("\t\t<dynamic prepend=\"WHERE\">\r\n");
		System.out.println("\t\t</dynamic>");
		System.out.println("\t</sql>");

		System.out.println();
		System.out.println("\t<select id=\"FIND_BY_ID\" parameterClass=\"int\" resultMap=\"" + ns + "_result\">");
		System.out.println("\tSELECT");
		System.out.println("\t\t<include refid=\"column_list\" />");
		System.out.println("\t\tFROM `" + tableName + "` WHERE `id`= #id#");
		System.out.println("\t</select>");

		System.out.println();
		System.out.println("\t<select id=\"LIST_BY_QUERY\" parameterClass=\"" + ns + "_query\" resultMap=\"" + ns + "_result\">");
		System.out.println("\tSELECT");
		System.out.println("\t\t<include refid=\"column_list\" />");
		System.out.println("\t\tFROM `" + tableName + "`");
		System.out.println("\t\t\t<include refid=\"query_condition\" />");
		System.out.println("\t\t\t<include refid=\"limit_condition\" />");
		System.out.println("\t</select>");

		System.out.println();
		System.out.println("\t<select id=\"COUNT_BY_QUERY\" parameterClass=\"" + ns + "_query\" resultClass=\"int\">");
		System.out.println("\tSELECT COUNT(*) FROM `" + tableName + "`");
		System.out.println("\t\t<include refid=\"query_condition\" />");
		System.out.println("\t</select>");

		System.out.println();
		System.out.println("</mapper>");
	}

}
