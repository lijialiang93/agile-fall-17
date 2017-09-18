package classes;

import utils.DBUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class IndividualRelFamily {

    public int individualId;
    public int familyId;
    public String role;

    public IndividualRelFamily(int iid, int fid, String role) {
        this.individualId = iid;
        this.familyId = fid;
        this.role = role;
    }

    public static List<IndividualRelFamily> findBy(String key, String value) throws Exception {
        String sql = "select distinct * from individual_rel_family where " + key + " = ?";
        ResultSet resultSet = DBUtils.query(sql, value);

        List<IndividualRelFamily> res = new ArrayList<>();
        while (resultSet.next()) {
            res.add(new IndividualRelFamily(
                    resultSet.getInt(1),
                    resultSet.getInt(2),
                    resultSet.getString(3)
            ));
        }
        return res;
    }

    public void save() throws Exception {
        String sql = "insert into individual_rel_family(individual_id, family_id, role) values(?, ?, ?)";
        DBUtils.update(sql, individualId, familyId, role);
    }

}
