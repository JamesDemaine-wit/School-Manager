package SchoolManager;

import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;

import java.util.HashMap;

import static SchoolManager.Driver.logger;

public class School
{

    private String name;
    private HashMap<String, Manager> departments;
    private final Driver driver;

    public School(String name, Driver driver)
    {
        this.driver = driver;
        departments = new HashMap<>();
        if (Utilities.max30Chars(name))
        {
            setName(name);
        } else
        {
            setName(name.substring(0, 30));
        }
    }

    //For cloning.
    //See Driver class : changeWorkingSchool
    public School(School school)
    {
        this.name = school.getName();
        this.driver = school.getDriver();
        this.departments = new HashMap<>(school.getDepartments());
    }

    public void refreshDepartments(Employee e)
    {
        //Refreshes each department to correspond to the main arraylist of employees.
        //It will remove the given employee.
        //This will automate the removal of department employees from their managers, so I don't have to check this when removing someone.
        //See EmployeeAPI Class method, initSchoolUpdater()

        //Basically, a manager's department is a subset of the employees in the system. The manager's employee cannot exist here without existing in the EmployeeAPI
        departments.forEach((deptName, manager) ->
        {
            if (manager.getDept().remove(e))
            {
                logger.log("Removed a deleted department Employee,\n" + e.toString() + "\n from Manager, " + manager.toString());
            }
        });
    }

    public Manager getManager(String deptName)
    {
        if (!departments.isEmpty())
        {
            if (departments.containsKey(deptName))
            {
                return departments.get(deptName);
            }
        }
        return null;
    }

    public boolean addDept(String deptName, Manager manager)
    {
        for (Employee e : driver.getEmpAPI().getEmployees())
        {
            if (e.getFirstName().equalsIgnoreCase(manager.getFirstName())
                    || e.getSecondName().equalsIgnoreCase(manager.getSecondName()))
            {
                //At this point, the Manager is a real employee. So add them and their department.
                if (!departments.containsKey(deptName) && !departments.containsValue(manager))
                {
                    //The department doesn't exist, and the manager doesn't exists, so add them and their department.
                    departments.put(deptName, manager);
                    return true;
                } else if (departments.containsKey(deptName) && !departments.containsValue(manager))
                {
                    UnsupportedOperationException a = new UnsupportedOperationException("A Manager already exists for this department.");
                    logger.log(a);
                    throw a;
                } else if (!departments.containsKey(deptName) && departments.containsValue(manager))
                {
                    // UnsupportedOperationException a = new UnsupportedOperationException("This Manager is already managing a different department.");
                    // logger.log(a);
                    // throw a;
                    //TODO Question for Siobhan/Mairead. The test is holding back me back here. Am I allowed to alter the test to suit what I want from this?
                    departments.put(deptName, manager);
                    return true;
                } else
                {
                    // UnsupportedOperationException a = new UnsupportedOperationException("This manager is already managing this department.");
                    // logger.log(a);
                    // throw a;
                    //Same thing here
                    return false;
                }
            }
        }
        return false;
    }

    public Manager deleteDept(String deptName)
    {
        return departments.remove(deptName);
    }

    public String displayManagerName(String deptName)
    {
        try
        {
            return departments.get(deptName).getFirstName() + " " + departments.get(deptName).getSecondName();
        } catch (Exception e)
        {
            return "Manager not found.";
        }
    }

    public String displayAllEmployeesFromDept(String deptName)
    {
        Manager m = departments.get(deptName);
        if (m == null)
        {
            return "Department not found.";
        } else
        {
            return m.toString();
        }
    }

    public int noDepartments()
    {
        return departments.size();
    }


    //Default Getters and Setters

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        if (Utilities.max30Chars(name))
        {
            this.name = name;
        }
    }

    public HashMap<String, Manager> getDepartments()
    {
        return departments;
    }

    public void setDepartments(HashMap<String, Manager> departments)
    {
        this.departments = departments;
    }

    public Driver getDriver()
    {
        return driver;
    }
}
