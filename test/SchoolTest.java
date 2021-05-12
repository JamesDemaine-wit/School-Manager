import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.School;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;
/**
 * Test Class for the School class
 * @author Siobhan Drohan & Mairead Meagher
 * @version 11/20
 */
public class SchoolTest {
    School sciAndComp;
    Manager alan, orla;
    AdminWorker fiona, mary;
    Lecturer mairead, siobhan, claire;

    //Custom changes.
    //According to the instructions, the add department method requires a check against the employees in the employee api.
    //So add the employees to the api as well.
    Driver app = new Driver();
    ArrayList<Employee> employees = app.getEmpAPI().getEmployees();

    @Before
    public void setUp() {
        sciAndComp = new School("Science and Computing", app);

        alan = new Manager("Alan", "Davy", "1234567aa", 3);
        employees.add(alan);
        orla = new Manager("Orla", "Donovan", "2222222aa", 4);
        employees.add(orla);
        fiona = new AdminWorker("Fiona", "Power", "3333333aa", 6);
        employees.add(fiona);
        mary = new AdminWorker("Mary", "Ryan", "4444444aa", 5);
        employees.add(mary);
        mairead = new Lecturer("Mairead", "Meagher", "5555555ss", 2);
        employees.add(mairead);
        siobhan = new Lecturer("Siobhan", "Drohan", "1111111xx", 3);
        employees.add(siobhan);
        claire = new Lecturer("Claire", "Keary", "7777777ss", 2);
        employees.add(claire);

        alan.addDeptEmployee(fiona);
        alan.addDeptEmployee(mary);
        alan.addDeptEmployee(mairead);
        alan.addDeptEmployee(siobhan);

        orla.addDeptEmployee(fiona);
        orla.addDeptEmployee(claire);

        //Now that the employees are created, place them in the EmployeeAPI
        app.getEmpAPI().setEmployees(employees);

        sciAndComp.addDept("Computing and Mathematics", alan);
        sciAndComp.addDept("Science", orla);

    }
    @Test
    public void testConstructor() {
        assertEquals("Science and Computing", sciAndComp.getName());
        School invalid = new School("123456789012345678901234567890-123", app);
        assertEquals("123456789012345678901234567890", invalid.getName());
        assertEquals(0, invalid.noDepartments());
    }
    @Test
    public void testSetName() {
        assertEquals("Science and Computing", sciAndComp.getName());
        sciAndComp.setName("123456789012345678901234567890-");
        assertEquals("Science and Computing", sciAndComp.getName());
        sciAndComp.setName("123456789012345678901234567890");
        assertEquals("123456789012345678901234567890", sciAndComp.getName());
    }
    @Test
    public void testSetDept() {
        HashMap<String, Manager> newDept = new HashMap<>();
        newDept.put("test dept 1", orla);
        newDept.put("test dept 2", alan);
        newDept.put("test dept 3", new Manager("test", "testy", "1234567ab", 3));

        assertEquals(2, sciAndComp.noDepartments());            //     from constructor
        sciAndComp.setDepartments(newDept);
        assertEquals(3, sciAndComp.noDepartments());
        sciAndComp.setDepartments(new HashMap<>());      //     empty hashmap
        assertEquals(0, sciAndComp.noDepartments());
    }


    @Test
    public void getManager() {
        assertTrue(sciAndComp.getManager("Computing and Mathematics").equals(alan));
        assertTrue(sciAndComp.getManager("Science").equals(orla));
        assertNull(sciAndComp.getManager("Chemistry"));
    }

    @Test
    public void addDept() {
        assertEquals(2, sciAndComp.noDepartments());            //     from constructor
        sciAndComp.addDept("Alan's second department", alan);
        assertEquals(3, sciAndComp.noDepartments());
        assertEquals(alan, sciAndComp.getDepartments().get("Alan's second department"));
        assertFalse(sciAndComp.addDept("Alan's second department", alan));       // this is already there so this should return false
    }

    @Test
    public void deleteDept() {
        assertEquals(2, sciAndComp.noDepartments());            //     from constructor
        assertEquals(orla,sciAndComp.deleteDept("Science"));                // this should return the deleted manager object if successful
        assertEquals(1, sciAndComp.noDepartments());            // should be reduce to 1
        assertNull(sciAndComp.getManager("Science"));           // should not be there so should return null

    }

    @Test
    public void testDisplayManagerName() {
        String str = sciAndComp.displayManagerName("Science");
        assertTrue(str.contains("Orla"));
        assertTrue(str.contains("Donovan"));
    }

    @Test
    public void testDisplayAllEmployeesFromDept() {
        String str = sciAndComp.displayAllEmployeesFromDept("Science");
        assertTrue(str.contains("Orla"));       //Manager's name also
        assertTrue(str.contains("Fiona"));      //Fiona's info
        assertTrue(str.contains("Power"));
        assertTrue(str.contains("3333333aa"));
        assertTrue(str.contains("6"));
        assertTrue(str.contains("Claire"));     //Claire's info
        assertTrue(str.contains("Keary"));
        assertTrue(str.contains("7777777ss"));
        assertTrue(str.contains("2"));
    }
}