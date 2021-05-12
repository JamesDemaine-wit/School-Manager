package SchoolManager.EmployeeManager.EmployeeTypes;

import SchoolManager.EmployeeManager.Employee;
import SchoolManager.Utilities;

public class AdminWorker extends Employee
{

    private int grade = 1;
    private static final float FIXED_BONUS = 200;

    public AdminWorker(String firstName, String secondName, String ppsNumber, int grade)
    {
        super(firstName, secondName, ppsNumber);
        setGrade(grade);
    }

    @Override
    public double calculateSalary()
    {
        return Utilities.getSalaryForAdminGrade(grade) + FIXED_BONUS;
    }

    @Override
    public String toString()
    {
        return "Manager: "
                + "\n\tFirst Name: " + getFirstName()
                + "\n\tSecond Name: " + getSecondName()
                + "\n\tPPS Number: " + getPpsNumber()
                + "\n\tSalary Grade: " + grade;
    }

    public boolean equals(AdminWorker adminWorker)
    {
        return getFirstName().equals(adminWorker.getFirstName())
                && getSecondName().equals(adminWorker.getSecondName())
                && getPpsNumber().equals(adminWorker.getPpsNumber())
                && getGrade() == adminWorker.getGrade();
    }

    //Getters and Setters

    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        if (Utilities.validAdminLevel(grade))
        {
            this.grade = grade;
        }
    }
}
