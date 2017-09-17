package classes;

import utils.DBUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Individual {

    public String id;
    public String name;
    public String gender;
    public Date birthday;
    public Date death;

    public Individual(String id, String name, String gender, Date birthday, Date death) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.death = death;
    }

    public static List<Individual> list() throws Exception {
        String sql = "select * from individuals";
        ResultSet resultSet = DBUtils.query(sql);

        List<Individual> res = new ArrayList<>();
        while (resultSet.next()) {
            res.add(new Individual(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4),
                    resultSet.getDate(5)
            ));
        }
        return res;
    }

    public static Individual findById(String id) throws Exception {
        String sql = "select * from individuals where id = ?";
        ResultSet resultSet = DBUtils.query(sql, id);

        Individual individual = null;
        if (resultSet.next()) {
            individual = new Individual(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getDate(4),
                    resultSet.getDate(5)
            );
        }

        return individual;
    }

    public void save() throws Exception {
        String sql = "insert into individuals(id, name, gender, birthday, death) values(?, ?, ?, ?, ?)";
        DBUtils.update(sql, this.id, this.name, this.gender, this.birthday, this.death);
    }

}
