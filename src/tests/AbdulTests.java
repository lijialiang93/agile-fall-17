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
}
