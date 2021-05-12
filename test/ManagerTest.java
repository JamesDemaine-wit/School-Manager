import static org.junit.Assert.*;

import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Test Classe for the Manager class
 */

/**
 * @author Siobhan Drohan & Mairead Meagher
 * @version 12/20
 */

public class ManagerTest {
    private Manager alan, manNormal2, invalidGrade1, invalidGrade2;
    private AdminWorker fiona;
    private Lecturer mairead, siobhan, roseanne;

    /**
     * Method to set up data for testing.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        alan = new Manager("Alan", "Davy", "1234567aa", 2);
        fiona = new AdminWorker("Fiona", "Power", "1234567aa", 2);
        siobhan = new Lecturer("Siobhan", "Drohan", "1234567aa", 3);
        mairead = new Lecturer("Mairead", "Meagher", "1234567bb", 2);
        roseanne = new Lecturer("Roseanne", "Birney", "1234567cc", 2);
        invalidGrade1 = new Manager("Bad", "Grade", "ss", 0);
        invalidGrade2 = new Manager("Big ", "Grade", "ss", 8);
        manNormal2 = new Manager("Big", "Boss", "1234567aa", 2);
        manNormal2.addDeptEmployee(fiona);
        manNormal2.addDeptEmployee(siobhan);
        manNormal2.addDeptEmployee(mairead);

    }

    /**
     * Test method for Manager constructor
     */
    @Test
    public void testManagerConstructor() {
        assertEquals("Alan", alan.getFirstName());
        assertEquals("Davy", alan.getSecondName());
        assertEquals(2, alan.getGrade());
        assertEquals(0, alan.numberInDept());
        assertEquals("Big", manNormal2.getFirstName());
        assertEquals("Boss", manNormal2.getSecondName());
        assertEquals(3, manNormal2.numberInDept());
        assertEquals(1, invalidGrade1.getGrade());
        assertEquals(1, invalidGrade2.getGrade());
    }


    @Test
    public void testCalculateSalary() {
        // manager with no employees
        assertEquals(1400, alan.calculateSalary(), 0.01);
        assertEquals(1600, fiona.calculateSalary(), 0.01);
        assertEquals(3000, siobhan.calculateSalary(), 0.01);
        assertEquals(1466, manNormal2.calculateSalary(), 0.01);
        manNormal2.addDeptEmployee(roseanne);
        assertEquals(1486, manNormal2.calculateSalary(), 0.01);
    }

    @Test
    public void testSetGrade() {
        assertEquals(2, alan.getGrade());
        alan.setGrade(1);
        assertEquals(1, alan.getGrade());
        alan.setGrade(0);
        assertEquals(1, alan.getGrade());   //no change
        alan.setGrade(7);  //top bound
        assertEquals(7, alan.getGrade());
        alan.setGrade(8);  //top bound
        assertEquals(7, alan.getGrade());   //no change
    }

    /**
     * Test method for addDeptEmployee(Employee).
     */
    @Test
    public void testAddDeptEmployee() {
        assertEquals(0, alan.numberInDept());
        alan.addDeptEmployee(roseanne);
        assertEquals(1, alan.numberInDept());
        assertEquals("Roseanne", alan.getDept().get(0).getFirstName());

        manNormal2.addDeptEmployee(roseanne);
        assertEquals(4, manNormal2.numberInDept());
        assertEquals("Fiona", manNormal2.getDept().get(0).getFirstName());
        assertEquals("Power", manNormal2.getDept().get(0).getSecondName());
        assertEquals("1234567aa", manNormal2.getDept().get(0).getPpsNumber());
        AdminWorker temp = (AdminWorker) manNormal2.getDept().get(0);
        assertEquals(2, temp.getGrade());


    }

    @Test
    public void removeEmployee() {
        assertEquals(3, manNormal2.numberInDept());
        manNormal2.removeEmployee(0);
        assertEquals(2, manNormal2.numberInDept());
        assertEquals("Siobhan", manNormal2.getDept().get(0).getFirstName());

    }

    @Test
    public void setDept() {
        ArrayList<Employee> newDept = new ArrayList<Employee>();
        newDept.add(new AdminWorker("Mairead", "Meagher", "1234567aa", 2));
        newDept.add(new Lecturer("Siobhan", "Drohan", "1234567aa", 2));
        newDept.add(new Lecturer("Sinead", "O Leary", "1234567aa", 2));
        manNormal2.setDept(newDept);
        assertEquals(3, manNormal2.numberInDept());
        assertEquals("Mairead", manNormal2.getDept().get(0).getFirstName());


    }

    @Test
    public void testEquals() {
        Manager sameAsManNormal1 = new Manager("Alan", "Davy", "1234567aa", 2);
        assertTrue(alan.equals(sameAsManNormal1));
        Manager notsameAsManNormal1 = new Manager("Alan", "Davy", "1234567aa", 4);
        assertFalse(alan.equals(notsameAsManNormal1));
    }

    @Test
    public void testToString() {
        assertTrue(alan.toString().contains("Alan"));
        assertTrue(alan.toString().contains("Davy"));
        assertTrue(alan.toString().toLowerCase().contains("no employees"));
        assertTrue(manNormal2.toString().contains("Big"));
        assertTrue(manNormal2.toString().contains("Boss"));
        assertTrue(manNormal2.toString().contains("Fiona"));
        assertTrue(manNormal2.toString().contains("Siobhan"));
        assertTrue(manNormal2.toString().contains("Mairead"));

    }

    @Test
    public void testNumberInDept() {
        assertEquals(3, manNormal2.numberInDept());
        manNormal2.addDeptEmployee(fiona);
        assertEquals(4, manNormal2.numberInDept());
    }
}

