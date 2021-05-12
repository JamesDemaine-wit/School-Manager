import SchoolManager.Driver;
import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import SchoolManager.EmployeeManager.EmployeeAPI;
import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.School;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmployeeAPITestPartial {
    private EmployeeAPI empAPI;
    private Manager alan, orla;
    private AdminWorker fiona, mary;
    private Lecturer mairead, siobhan, claire;
    private School school;
    private Driver driver;

    @Before
    public void setUp() throws Exception {

        alan = new Manager("Alan", "Davy", "1234567aa", 3);    // salary 2231.0
        orla = new Manager("Orla", "Donovan", "2222222aa", 4);  //salary  2844
        fiona = new AdminWorker("Fiona", "Power", "3333333aa", 6);  // salary 4400
        mary = new AdminWorker("Mary", "Ryan", "4444444aa", 5);   // salary 3700
        mairead = new Lecturer("Mairead", "Meagher", "5555555ss", 2);  //salary 2000
        siobhan = new Lecturer("Siobhan", "Drohan", "1111111xx", 3); // salary 3000
        claire = new Lecturer("Claire", "Keary", "7777777ss", 2);   // salary 2000

        driver = new Driver();
        empAPI = driver.getEmpAPI();
        school = driver.getSchool();

        empAPI.addEmployee(alan);
        empAPI.addEmployee(orla);
        empAPI.addEmployee(fiona);
        empAPI.addEmployee(mary);
        empAPI.addEmployee(mairead);


        alan.addDeptEmployee(fiona);
        alan.addDeptEmployee(mary);
        alan.addDeptEmployee(mairead);
        alan.addDeptEmployee(siobhan);

        orla.addDeptEmployee(fiona);
    }


    @After
    public void tearDown() throws Exception {
     //TODO
    }

    @Test
    public void testTimer(){
        //TODO make a test for the timer.
    }

    @Test
    public void testConstructors() {
        assertEquals(5, empAPI.getEmployees().size());
        assertEquals("Alan", empAPI.getEmployees().get(0).getFirstName());
        assertEquals(4, ((Manager) empAPI.getEmployees().get(0)).getDept().size());
        assertEquals("Orla", empAPI.getEmployees().get(1).getFirstName());
        assertEquals("Fiona", empAPI.getEmployees().get(2).getFirstName());
        assertEquals("Mary", empAPI.getEmployees().get(3).getFirstName());

        assertEquals("Donovan", empAPI.getEmployees().get(1).getSecondName());
        assertEquals("Power", empAPI.getEmployees().get(2).getSecondName());
        assertEquals("Ryan", empAPI.getEmployees().get(3).getSecondName());
    }


    @Test
    public void searchEmployees() {
      assertEquals(0, empAPI.searchEmployees("Davy"));
      assertEquals(1, empAPI.searchEmployees("Donovan"));
      assertEquals(2, empAPI.searchEmployees("Power"));
      assertEquals(3, empAPI.searchEmployees("Ryan"));
      assertEquals(4 ,empAPI.searchEmployees("Meagher"));
      assertEquals(-1 ,empAPI.searchEmployees("Trump"));

    }


    @Test
    public void listEmployees() {
        assertTrue(empAPI.listEmployees().contains("Alan"));
        assertTrue(empAPI.listEmployees().contains("Davy"));
        assertTrue(empAPI.listEmployees().contains("1234567aa"));
        assertTrue(empAPI.listEmployees().contains("Orla"));
        assertTrue(empAPI.listEmployees().contains("Donovan"));
        assertTrue(empAPI.listEmployees().contains("Fiona"));


        assertTrue(empAPI.listEmployees().contains("Mary"));
        assertTrue(empAPI.listEmployees().contains("Mairead"));

        EmployeeAPI emptyList = new EmployeeAPI(new Driver());
        assertTrue(emptyList.listEmployees().toLowerCase().contains("no employees"));

    }


}
