package SchoolManager.EmployeeManager.EmployeeTypes;

import SchoolManager.EmployeeManager.Employee;
import SchoolManager.Utilities;

import java.util.ArrayList;

import static SchoolManager.Driver.logger;

public class Manager extends Employee
{

    private int grade;
    private ArrayList<Employee> dept;

    public Manager(String firstName, String secondName, String ppsNumber, int grade)
    {
        super(firstName, secondName, ppsNumber);
        if (Utilities.validManagerLevel(grade))
        {
            setGrade(grade);
        } else
        {
            setGrade(1);
        }
        dept = new ArrayList<>();
    }

    @Override
    public double calculateSalary()
    {
        double salary = Utilities.getSalaryForManagerGrade(grade);
        if (!dept.isEmpty())
        {
            for (Employee e : dept)
            {
                salary += e.calculateSalary() * 0.01D;
            }
        }
        return salary;
    }

    @Override
    public String toString()
    {
        String employees = "";
        if (!dept.isEmpty())
        {
            for (Employee e : dept)
            {
                employees = employees.concat("\n\t" + e.toString());
            }
        } else
        {
            employees = "There are no employees in this Manager's department.";
        }
        return "Manager: "
                + "\n\tFirst Name: " + getFirstName()
                + "\n\tSecond Name: " + getSecondName()
                + "\n\tPPS Number: " + getPpsNumber()
                + "\n\tSalary Grade: " + grade
                + "\n\tEmployees: " + employees;
        //TODO does this look ok when printing?
    }

    public boolean equals(Manager manager)
    {
        return getFirstName().equals(manager.getFirstName())
                && getSecondName().equals(manager.getSecondName())
                && getPpsNumber().equals(manager.getPpsNumber())
                && getGrade() == manager.getGrade();
    }

    public void addDeptEmployee(Employee employee)
    {
        if (!dept.isEmpty())
        {
            for (Employee e : dept)
            {
                if (e.equals(employee))
                {
                    logger.log("Warning: Adding "
                            + employee.toString()
                            + "\nto\n"
                            + toString()
                            + "\nThe employee is already in the Manager's Department, they are now duplicated.");
                    //.here TODO Question for Siobhan/Mairead
                    //I would return this method here with a different log message,
                    //but the test fails if I don't add the duplicate employee...
                    //Why does the test require this?
                }
            }
        }
        dept.add(employee);
    }

    public void removeEmployee(int index)
    {
        if (!dept.isEmpty())
        {
            if (Utilities.validIndex(index, dept))
            {
                dept.remove(index);
            } else
            {
                logger.log("Unable to remove employee at index "
                        + index
                        + "\nfrom\n"
                        + toString()
                        + "\nThe index is out of bounds for this Manager.");
            }
        } else
        {
            logger.log("Unable to remove employee at index "
                    + index
                    + "\nfrom\n"
                    + toString()
                    + "\nThis Manager has no Employees in their Department.");
        }
    }

    public int numberInDept()
    {
        return dept.size();
    }

    //Getters and Setters

    public int getGrade()
    {
        return grade;
    }

    public void setGrade(int grade)
    {
        if (Utilities.validManagerLevel(grade))
        {
            this.grade = grade;
        }
    }

    public ArrayList<Employee> getDept()
    {
        return dept;
    }

    public void setDept(ArrayList<Employee> dept)
    {
        this.dept = dept;
    }
}
