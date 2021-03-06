package classes;

import utils.DBUtils;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Family {

	public int id;
	public Date married;
	public Date divorced;
	public List<IndividualRelFamily> members;

	public Family(int id, Date married, Date divorced) throws Exception {
		this.id = id;
		this.married = dateNotAfterCurrentDate(married, "Married", id);
		this.divorced = dateNotAfterCurrentDate(divorced, "Divorced", id);
	}

	public static List<Family> all() throws Exception {
		String sql = "select * from families";
		ResultSet resultSet = DBUtils.query(sql);

		List<Family> res = new ArrayList<>();
		while (resultSet.next()) {
			Family family = new Family(resultSet.getInt(1), resultSet.getDate(2), resultSet.getDate(3));

			family.members = IndividualRelFamily.findBy("family_id", String.valueOf(family.id));
			res.add(family);
		}
		return res;
	}

	public static Family findById(int id) throws Exception {
		String sql = "select * from families where id = ?";
		ResultSet resultSet = DBUtils.query(sql, id);

		Family family = null;
		if (resultSet.next()) {
			family = new Family(resultSet.getInt(1), resultSet.getDate(2), resultSet.getDate(3));

			family.members = IndividualRelFamily.findBy("family_id", String.valueOf(family.id));
		}
		return family;
	}

	public void addMember(IndividualRelFamily member) {
		if (members == null) {
			members = new ArrayList<>();
		}
		members.add(member);
	}

	// check date validity
	public Date dateNotAfterCurrentDate(Date date, String type, int id) throws Exception {
		if (date == null) {
			return date;
		} else {
			Date currentDate = new Date();
			int check = currentDate.compareTo(date);
			if (check == 0 || check >= 0) {
				return date;
			}
		}
		throw new Exception(type + " should not be after the current date " + "Family ID: " + id);
	}

	public void save() throws Exception {
		String sql = "insert into families(id, married, divorced) values(?, ?, ?)";
		DBUtils.update(sql, this.id, this.married, this.divorced);
	}

}
