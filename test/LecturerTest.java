import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Test Class for the Lecturer class
 * @author Siobhan Drohan & Mairead Meagher
 * @version 11/20
 */
public class LecturerTest {
    private Lecturer lecturerValid, lecturerInvalid;

    @Before
    public void setUp() throws Exception {
        lecturerValid = new Lecturer("Selly", "Selly", "1234567xx", 2);
        lecturerInvalid = new Lecturer("Mad", "Men", "xx34567qq", 4);
    }

    @Test
    public void testConstructor() {
        assertEquals(2, lecturerValid.getLevel());
        assertEquals(1, lecturerInvalid.getLevel());
    }

    @Test
    public void setLevel() {
        assertEquals(2, lecturerValid.getLevel());
        lecturerValid.setLevel(1);
        assertEquals(1, lecturerValid.getLevel());
        lecturerValid.setLevel(0);
        assertEquals(1, lecturerValid.getLevel());
        lecturerValid.setLevel(3);
        assertEquals(3, lecturerValid.getLevel());
        lecturerValid.setLevel(4);
        assertEquals(3, lecturerValid.getLevel(), 0.01);
    }

    @Test
    public void calculateSalary() {
        assertEquals(2000.0, lecturerValid.calculateSalary(), 0.01);
        assertEquals(1000, lecturerInvalid.calculateSalary(), 0.01);
    }

   @Test
    public void testEquals() {
        assertTrue(lecturerValid.equals(lecturerValid));
        Lecturer otherLecturer = new Lecturer("Selly", "Selly", "1234567xx", 2);
        assertTrue(lecturerValid.equals(otherLecturer));
        otherLecturer.setLevel(1);
        assertFalse(lecturerValid.equals(otherLecturer));
    }

   @Test
    public void testToString() {
        assertTrue(lecturerValid.toString().contains("Selly"));
        assertTrue(lecturerValid.toString().contains("1234567xx"));
        assertTrue(lecturerValid.toString().contains("2"));

    }
}