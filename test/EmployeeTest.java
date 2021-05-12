import static org.junit.Assert.*;

import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Test Class for the Employee class
 * Only methods from the abstract class are tested.
 * Because Employee is an abstract class, we cannot directly test it.
 * So we test it via a concrete version (AdminWorker)
 * @author Siobhan Drohan & Mairead Meagher
 * @version 11/20
 */

public class EmployeeTest {
	
	private AdminWorker empNormal1, empNormal2;
	private AdminWorker empTestValidation;

	/**
	 * Method to set up data for testing. 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		empNormal1 = new AdminWorker("Donald", "Duck", "1234567xx", 2);
		empNormal2 = new AdminWorker("David", "Brent", "1234567xx", 4);
		empTestValidation = new AdminWorker("1234567890123", "1234567890123", "xx34567xx", -1);
	}

	/**
	 * Test method for Employee constructor
	 */
	@Test
	public void testConstructor() {
		//test on valid data
		assertEquals("Donald", empNormal1.getFirstName());
		assertEquals("Duck", empNormal1.getSecondName());
		assertEquals(2, empNormal1.getGrade(), 0.01);
		assertEquals("1234567xx",  empNormal1.getPpsNumber ());


		//test on invalid data
		assertEquals("1234567890", empTestValidation.getFirstName());
		assertEquals("1234567890", empTestValidation.getSecondName());
		assertEquals(1, empTestValidation.getGrade());
		assertEquals("0000000XX",  empTestValidation.getPpsNumber ());
	}
	

	
	/**
	 * Test method for getters and setters. 
	 */
	@Test	
	public void testSettersGetters() {
		assertEquals("Donald", empNormal1.getFirstName());
		empNormal1.setFirstName("John");
		assertEquals("John", empNormal1.getFirstName());
		empNormal1.setFirstName(("1234567890123"));
		assertEquals("John", empNormal1.getFirstName());


		assertEquals("1234567xx", empNormal1.getPpsNumber());
		empNormal1.setPpsNumber("2222222aa");
		assertEquals("2222222aa", empNormal1.getPpsNumber());
		empNormal1.setPpsNumber("123xxxxaa");
		assertEquals("2222222aa", empNormal1.getPpsNumber());

		assertEquals("Duck", empNormal1.getSecondName());
		empNormal1.setSecondName("Mouse");
		assertEquals("Mouse", empNormal1.getSecondName());
		empNormal1.setSecondName("Mickey Mouse");
		assertEquals("Mouse", empNormal1.getSecondName());
	}
//empNormal1 = new AdminWorker("Donald", "Duck", "1234567xx", 2);
	@Test
	public void testEquals() {
		AdminWorker  likeEmpNormal = new AdminWorker("Donald", "Duck", "1234567xx", 2);
		assertTrue(empNormal1.equals(likeEmpNormal));
		AdminWorker  notLikeEmpNormal = new AdminWorker("Donald", "Trump", "1234567xx", 2);
		assertFalse(empNormal1.equals(notLikeEmpNormal));
	}

	/**
	 * Test method for toString - checks that all data fields are present.
	 */
	@Test
	public void testToString() {
		assertTrue(empNormal1.toString().contains("Donald"));
		assertTrue(empNormal1.toString().contains("Duck"));
		assertTrue(empNormal1.toString().contains("2"));

	}
}
