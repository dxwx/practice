package com.up.util.code;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by jinhaishan on 17/9/25.
 */
public class ConnectionUtil {

    private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";

    /**
     * 获取数据库连接
     * @param host
     * @param port
     * @param dataBase
     * @param user
     * @param password
     * @return
     */
    public static Connection createMysqlConnection(String host, int port, String dataBase, String user, String password) {
        try {
            Class.forName(DRIVER_CLASS);
            return DriverManager.getConnection(getUrl(host, port, dataBase), user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getUrl(String host, int port, String dabaBase) {
        return "jdbc:mysql://" + host + ":" + port + "/" + dabaBase;
    }
}
