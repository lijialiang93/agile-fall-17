package utils;

import org.json.JSONObject;

import java.sql.*;

public class DBUtils {

    private static final String DB_CONNECTION_STRING = "jdbc:mysql://%s/%s?user=%s&password=%s";

    public static Connection connect() throws Exception {
        String config = FileUtils.read(System.getProperty("user.dir") + "/config.json");
        JSONObject jsonObject = (JSONObject) JSONUtils.parse(config);

        String host = jsonObject.getString("db_host");
        String name = jsonObject.getString("db_name");
        String user = jsonObject.getString("db_user");
        String password = jsonObject.getString("db_password");
        String connectionString = String.format(DB_CONNECTION_STRING, host, name, user, password);

        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(connectionString);
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
