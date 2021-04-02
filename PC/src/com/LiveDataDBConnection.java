package com;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 采用阿里的德鲁伊Druid连接池连接mysql数据库
 */
public class LiveDataDBConnection {
    //连接池对象
    private static DataSource dataSource;

    static {
        if (dataSource == null) {
            try {
                //1.得到配置文件的输入流，注：/ 不能省略
                InputStream inputStream = LiveDataDBConnection.class.getResourceAsStream("/config/druid-livedata.properties");
                //2.创建Properties对象，读取上面的配置文件
                Properties properties = new Properties();
                properties.load(inputStream);
                //3.使用工厂类创建数据源
                dataSource = DruidDataSourceFactory.createDataSource(properties);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("连接池异常！");
            }
        }
    }

    /**
     * 获取连接池方法
     */
    private static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 创建连接
     *
     * @return
     * @throws SQLException
     */
    public static Connection getConnection() throws SQLException {
        return getDataSource().getConnection();
    }

    /**
     * 关闭连接
     *
     * @param conn
     */
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();//归还连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 测试
     */
    private static void test() {
        Connection conn = null;
        Statement statement = null;
        try {
            conn = LiveDataDBConnection.getConnection();

            statement = conn.createStatement();
            String sql = "SELECT FID,FWELDER_NO,FNAME FROM TB_WELDER";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                String fname = resultSet.getString("FNAME");
                System.out.println("FNAME:" + fname);
            }
            System.out.println("OK！");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            LiveDataDBConnection.close(conn, statement, null);
        }
    }

}
