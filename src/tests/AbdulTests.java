package tests;

import org.junit.Test;

import classes.Individual;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List; 

public class AbdulTests{

	@Test 
	public void testBornIn30Days() throws Exception{
		List<Individual> list = Individual.borninLast30Days(Individual.all()); 
		ArrayList<Individual> fake = new ArrayList<Individual>();
		Date date = null;
		Individual ind = new Individual(21, "Abdul /Alhomai/", "M", new Date (2017,9,13), date);
		fake.add(ind);
		assertEquals(list,fake);
	}
	
	@Test
	public void testArrListSize()throws Exception{
		
		List<Individual> list = Individual.borninLast30Days(Individual.all()); 
		ArrayList<Individual> fake = new ArrayList<Individual>();
		Date date = null;
		Individual ind = new Individual(21, "Abdul /Alhomai/", "M", new Date (2017,9,13), date);
		fake.add(ind);
		assertTrue(list.size() == fake.size());
	}
	
	@Test
	public void testNotNull()throws Exception{
		
		List<Individual> list = Individual.borninLast30Days(Individual.all()); 
		assertNotNull(list);
	}
	
	@Test 
	public void testNotEquals() throws Exception{
		List<Individual> list = Individual.borninLast30Days(Individual.all()); 
		ArrayList<Individual> fake = new ArrayList<Individual>();
		Date date = null;
		Individual ind = new Individual(22, "Abdul /Alhomai/", "M", new Date (2017,9,13), date);
		fake.add(ind);
		assertNotEquals(list,fake);
	}
	
	@Test
	public void testAssertFalls()throws Exception{
		
		List<Individual> list = Individual.borninLast30Days(Individual.all()); 
		ArrayList<Individual> fake = new ArrayList<Individual>();
		Date date = null;
		Individual ind = new Individual(21, "Abdul /Alhomai/", "M", new Date (2017,9,13), date);
		fake.add(ind);
		assertFalse(list.size() != fake.size());
	}
}
