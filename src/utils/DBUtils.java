package utils;

import java.sql.*;

public class DBUtils {

    private static final String DB_HOST = "gedcom.cvsrpee89tba.us-east-1.rds.amazonaws.com";
    private static final String DB_NAME = "gedcom";
    private static final String DB_USER = "agile";
    private static final String DB_PASSWORD = "agilefall17";
    private static final String DB_CONNECTION_STRING = String.format("jdbc:mysql://%s/%s?user=%s&password=%s", DB_HOST, DB_NAME, DB_USER, DB_PASSWORD);

    public static Connection connect() throws Exception {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(DB_CONNECTION_STRING);
    }

    public static PreparedStatement prepare(Connection connection, String sql) throws SQLException {
        return connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    public static ResultSet query(String sql, Object... parameters) throws Exception {
        Connection connection = connect();

        PreparedStatement statement = prepare(connection, sql);
        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }

        ResultSet result = statement.executeQuery();
//        statement.close();
//        connection.close();

        return result;
    }

}
