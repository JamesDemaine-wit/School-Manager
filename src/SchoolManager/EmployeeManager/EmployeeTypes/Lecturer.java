package SchoolManager.EmployeeManager.EmployeeTypes;

import SchoolManager.EmployeeManager.Employee;
import SchoolManager.Utilities;

public class Lecturer extends Employee
{

    private int level = 1;

    public Lecturer(String firstName, String secondName, String ppsNumber, int level)
    {
        super(firstName, secondName, ppsNumber);
        setLevel(level);
    }


    @Override
    public double calculateSalary()
    {
        return Utilities.getSalaryForLecturerLevel(level);
    }

    @Override
    public String toString()
    {
        return "Lecturer: "
                + "\n\tFirst Name: " + getFirstName()
                + "\n\tSecond Name: " + getSecondName()
                + "\n\tPPS Number: " + getPpsNumber()
                + "\n\tSalary Level: " + level;
    }

    public boolean equals(Lecturer lecturer)
    {
        return getFirstName().equals(lecturer.getFirstName())
                && getSecondName().equals(lecturer.getSecondName())
                && getPpsNumber().equals(lecturer.getPpsNumber())
                && getLevel() == lecturer.getLevel();
    }

    //Getters and Setters

    public int getLevel()
    {
        return level;
    }

    public void setLevel(int level)
    {
        if (Utilities.validLecturerLevel(level))
        {
            this.level = level;
        }
    }
}
