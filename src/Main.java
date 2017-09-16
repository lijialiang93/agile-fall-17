import utils.DBUtils;

import java.sql.ResultSet;

public class Main {

    public static void main(String[] args) {
        try {
            ResultSet result = DBUtils.query("show tables");

            while (result.next()) {
                System.out.println(result.getString(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
