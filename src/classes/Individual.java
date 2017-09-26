package classes;

import utils.DBUtils;
import utils.DateTimeUtils;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Individual {

	public int id;
	public String name;
	public String gender;
	public Date birthday;
	public Date death;
	public int age;
	public boolean isAlive;

	public Individual(int id, String name, String gender, Date birthday, Date death) throws Exception {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.birthday = dateNotAfterCurrentDate(birthday, "Birthday", id);
		this.death = dateNotAfterCurrentDate(death, "Death", id);

		this.isAlive = this.death == null;
		this.age = DateTimeUtils.calculateAge(this.birthday, this.death);
	}

	public static List<Individual> all() throws Exception {
		String sql = "select * from individuals order by id";
		ResultSet resultSet = DBUtils.query(sql);

		List<Individual> res = new ArrayList<>();
		while (resultSet.next()) {
			res.add(new Individual(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
					resultSet.getDate(4), resultSet.getDate(5)));
		}
		return res;
	}

	public static Individual findById(int id) throws Exception {
		String sql = "select * from individuals where id = ?";
		ResultSet resultSet = DBUtils.query(sql, id);

		Individual individual = null;
		if (resultSet.next()) {
			individual = new Individual(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
					resultSet.getDate(4), resultSet.getDate(5));
		}

		return individual;
	}

	// Abdul start
	public static List<Individual> borninLast30Days(List<Individual> ind) {

		List<Individual> indiBornIn30Days = new ArrayList<>();
		for (int x = 0; x < ind.size(); x++) {
			if (DateTimeUtils.calculateAgeInDays(ind.get(x).birthday) <= 30) {
				indiBornIn30Days.add(ind.get(x));
			}
		}
		return indiBornIn30Days;
	}

	@Override
	public boolean equals(Object obj) {
		Individual compareAgainstIndi = (Individual) obj;

		if (this.id == compareAgainstIndi.id) {
			return true;
		}
		return false;
	}

	// Abdul end

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
		throw new Exception(type + " should not be after the current date " + "Individual ID: " + id);
	}

	public void save() throws Exception {
		String sql = "insert into individuals(id, name, gender, birthday, death) values(?, ?, ?, ?, ?)";
		DBUtils.update(sql, this.id, this.name, this.gender, this.birthday, this.death);
	}

}
