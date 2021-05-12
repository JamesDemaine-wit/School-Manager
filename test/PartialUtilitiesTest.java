

import SchoolManager.Utilities;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class PartialUtilitiesTest {


    @Test
    public void max10Chars() {
        assertTrue( Utilities.max10Chars("1234567890"));  //normal
        assertFalse(Utilities.max10Chars("1234567890-123"));  //abnormal
        assertTrue(Utilities.max10Chars(""));  //normal but unusual
    }
    @Test
    public void max30Chars() {
        assertTrue( Utilities.max30Chars("123456789012345678901234567890"));  //normal
        assertFalse(Utilities.max30Chars("123456789012345678901234567890-123"));  //abnormal
        assertTrue(Utilities.max30Chars(""));  //normal but unusual
    }


    @Test
    public void validIntRange() {
        assertTrue(Utilities.validIntRange(1, 10, 5));
        assertTrue(Utilities.validIntRange(1, 10, 1));
        assertTrue(Utilities.validIntRange(1, 10, 10));
        assertFalse(Utilities.validIntRange(1, 10, 0));
        assertFalse(Utilities.validIntRange(1, 10, 11));

    }

    @Test
    public void validIntNonNegative() {
        assertTrue(Utilities.validIntNonNegative(0));
        assertTrue(Utilities.validIntNonNegative(1));
        assertTrue(Utilities.validIntNonNegative(1000));
        assertFalse(Utilities.validIntNonNegative(-1));


    }
    @Test
    public void validPPS() {
        assertTrue(Utilities.validPPS(("1234567ab")));    // normal
        assertTrue(Utilities.validPPS(("1234567AB")));    // checking different incorrect positions
        assertFalse(Utilities.validPPS(("1234567ab1")));
        assertFalse(Utilities.validPPS(("a234567ab")));
        assertFalse(Utilities.validPPS(("1a34567ab")));
        assertFalse(Utilities.validPPS(("12a4567ab")));
        assertFalse(Utilities.validPPS(("123a567ab")));
        assertFalse(Utilities.validPPS(("1234a67ab")));
        assertFalse(Utilities.validPPS(("12345a7ab")));
        assertFalse(Utilities.validPPS(("123456aab")));
        assertFalse(Utilities.validPPS(("12345671b")));
        assertFalse(Utilities.validPPS(("1234567a1")));
    }
    public void getSalaryForLecturerLevel() {
        assertEquals(1000, Utilities.getSalaryForLecturerLevel(1), .01);
        assertEquals(2000, Utilities.getSalaryForLecturerLevel(2), .01);
        assertEquals(3000, Utilities.getSalaryForLecturerLevel(3), .01);
        assertEquals(-1, Utilities.getSalaryForLecturerLevel(0), .01);
        assertEquals(-1, Utilities.getSalaryForLecturerLevel(4), .01);

    }
}

