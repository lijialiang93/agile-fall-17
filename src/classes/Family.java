package classes;

import utils.DBUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Family {

    public String id;
    public Date married;
    public Date divorced;
    public List<IndividualRelFamily> members;

    public Family(String id, Date married, Date divorced) {
        this.id = id;
        this.married = married;
        this.divorced = divorced;
    }

    public static List<Family> all() throws Exception {
        String sql = "select * from families";
        ResultSet resultSet = DBUtils.query(sql);

        List<Family> res = new ArrayList<>();
        while (resultSet.next()) {
            Family family = new Family(
                    resultSet.getString(1),
                    resultSet.getDate(2),
                    resultSet.getDate(3)
            );

            family.members = IndividualRelFamily.findBy("family_id", family.id);
            res.add(family);
        }
        return res;
    }

    public static Family findById(String id) throws Exception {
        String sql = "select * from families where id = ?";
        ResultSet resultSet = DBUtils.query(sql, id);

        Family family = null;
        if (resultSet.next()) {
            family = new Family(
                    resultSet.getString(1),
                    resultSet.getDate(2),
                    resultSet.getDate(3)
            );

            family.members = IndividualRelFamily.findBy("family_id", family.id);
        }
        return family;
    }

    public void addMember(IndividualRelFamily member) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(member);
    }

    public void save() throws Exception {
        String sql = "insert into families(id, married, divorced) values(?, ?, ?)";
        DBUtils.update(sql, this.id, this.married, this.divorced);
    }

}
