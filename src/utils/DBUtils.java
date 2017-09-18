package utils;

import org.json.JSONObject;

import java.sql.*;

public class DBUtils {

    private static final String DB_CONNECTION_STRING = "jdbc:mysql://%s/%s?user=%s&password=%s";
    private static Connection connection;

    public static Connection connect() throws Exception {
        if (connection == null) {
            String config = FileUtils.read(System.getProperty("user.dir") + "/config.json");
            JSONObject jsonObject = (JSONObject) JSONUtils.parse(config);

            String host = jsonObject.getString("db_host");
            String name = jsonObject.getString("db_name");
            String user = jsonObject.getString("db_user");
            String password = jsonObject.getString("db_password");
            String connectionString = String.format(DB_CONNECTION_STRING, host, name, user, password);

            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(connectionString);
        }

        return connection;
    }

    public static PreparedStatement prepare(Connection connection, String sql, Object... parameters) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

        for (int i = 0; i < parameters.length; i++) {
            statement.setObject(i + 1, parameters[i]);
        }

        return statement;
    }

    public static ResultSet query(String sql, Object... parameters) throws Exception {
        Connection connection = connect();
        PreparedStatement statement = prepare(connection, sql, parameters);
        return statement.executeQuery();
    }

    public static void update(String sql, Object... parameters) throws Exception {
        Connection connection = connect();
        PreparedStatement statement = prepare(connection, sql, parameters);
        statement.executeUpdate();
    }

}
