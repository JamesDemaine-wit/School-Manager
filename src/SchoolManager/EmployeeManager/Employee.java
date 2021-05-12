package SchoolManager.EmployeeManager;

import SchoolManager.Utilities;

public abstract class Employee
{

    private String firstName;
    private String secondName;
    private String ppsNumber;

    public Employee(String firstName, String secondName, String ppsNumber)
    {
        if (Utilities.max10Chars(firstName))
        {
            setFirstName(firstName);
        } else
        {
            setFirstName(firstName.substring(0, 10));
        }
        if (Utilities.max10Chars(secondName))
        {
            setSecondName(secondName);
        } else
        {
            setSecondName(secondName.substring(0, 10));
        }
        if (Utilities.validPPS(ppsNumber))
        {
            setPpsNumber(ppsNumber);
        } else
        {
            setPpsNumber("0000000XX");
        }
    }

    public abstract double calculateSalary();

    public boolean equals(Employee employee)
    {
        return firstName.equals(employee.getFirstName())
                && secondName.equals(employee.getSecondName())
                && ppsNumber.equals(employee.getPpsNumber());
    }

    @Override
    public String toString()
    {
        return "Employee: "
                + "\n\tFirst Name: " + getFirstName()
                + "\n\tSecond Name: " + getSecondName()
                + "\n\tPPS Number: " + getPpsNumber();
    }


    //Getters and Setters

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        if (Utilities.max10Chars(firstName))
        {
            this.firstName = firstName;
        }
    }

    public String getSecondName()
    {
        return secondName;
    }

    public void setSecondName(String secondName)
    {
        if (Utilities.max10Chars(secondName))
        {
            this.secondName = secondName;
        }
    }

    public String getPpsNumber()
    {
        return ppsNumber;
    }

    public void setPpsNumber(String ppsNumber)
    {
        if (Utilities.validPPS(ppsNumber))
        {
            this.ppsNumber = ppsNumber;
        }
    }
}
