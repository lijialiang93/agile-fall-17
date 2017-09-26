package tests;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import classes.Family;
import classes.Individual;

public class JialiangTests {
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	//birthday after current date
	@Test
	public void birthAfterCurrent() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("Birthday should not be after the current date");
		Date bDate = sdf.parse("01 AUG 2099");
		Date dDate = null;
		new Individual(99, "Jialiang", "M", bDate, dDate);
	}
	
	//death after current date
	@Test
	public void deathAfterCurrent() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("Death should not be after the current date");
		Date bDate = sdf.parse("01 AUG 1993");
		Date dDate = sdf.parse("01 AUG 2093");
		new Individual(99, "Jialiang", "M", bDate, dDate);
	}
	
	//married date after current date
	@Test
	public void marrAfterCurrent() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("Married should not be after the current date");
		Date mDate = sdf.parse("01 AUG 2099");
		Date diDate = null;
		new Family(99,mDate,diDate);
	}
	
	//divorce date after current date
	@Test
	public void divorceAfterCurrent() throws Exception {
		thrown.expect(Exception.class);
		thrown.expectMessage("Divorced should not be after the current date");
		Date mDate = sdf.parse("01 AUG 2017");
		Date diDate = sdf.parse("01 AUG 2099");
		new Family(99,mDate,diDate);
	}
	
	//birthday equals current date
	@Test
	public void birthEqualsCurrent() throws Exception {
		Date bDate = new Date();
		Date dDate = sdf.parse("01 AUG 2017");
		new Individual(99, "Jialiang", "M", bDate, dDate);
	}
	
	//death date equals current date
	@Test
	public void deathEqualsCurrent() throws Exception {
		Date bDate = sdf.parse("01 AUG 1993");
		Date dDate = new Date();
		new Individual(99, "Jialiang", "M", bDate, dDate);
	}
	
	//married date equals current date
	@Test
	public void marrEqualsCurrent() throws Exception {
		Date mDate = new Date();
		Date diDate = sdf.parse("01 AUG 2017");
		new Family(99,mDate,diDate);
	}
	
	//divorce date equals current date
	@Test
	public void divorceEqualsCurrent() throws Exception {
		Date mDate = sdf.parse("01 AUG 2017");
		Date diDate = new Date();
		new Family(99,mDate,diDate);
	}
	
	//birthday and death date before current date
	@Test
	public void birthAndDeathBeforeCurrent() throws Exception {
		Date bDate = sdf.parse("01 AUG 1993");
		Date dDate = sdf.parse("01 AUG 2017");
		new Individual(99, "Jialiang", "M", bDate, dDate);
	}
	
	//married date and divorce date before current date
	@Test
	public void marrAndDivorceBeforeCurrent() throws Exception {
		Date mDate = sdf.parse("01 JAN 2017");
		Date diDate = sdf.parse("01 FEB 2017");
		new Family(99,mDate,diDate);
	}

}
