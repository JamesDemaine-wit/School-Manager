package SchoolManager.EmployeeManager;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;
import SchoolManager.School;
import SchoolManager.Utilities;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;

import static SchoolManager.Driver.logger;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class EmployeeAPI
{

    private ArrayList<Employee> employees;
    private ArrayList<Employee> previousEmployees;
    private Timer schoolUpdater;
    private Driver driver;

    public EmployeeAPI(Driver driver)
    {
        this.driver = driver;
        employees = new ArrayList<>();
        previousEmployees = new ArrayList<>(); //Will only be used by the schoolUpdate Timer thread.
        initSchoolUpdater(driver.getSchool());
        startSchoolUpdater();
    }

    //For cloning.
    //See Driver class : changeWorkingSchool
    //The timer is instantiated by the initSchoolUpdater method.
    public EmployeeAPI(EmployeeAPI e, School previousSchool)
    {//for cloning.
        this.driver = e.getDriver();
        this.employees = new ArrayList<>(e.getEmployees());
        this.previousEmployees = new ArrayList<>(e.getPreviousEmployees());
        initSchoolUpdater(previousSchool);
    }

    private void initSchoolUpdater(School school)
    {
        //This will automate the removal of department employees from their managers, so I don't have to check this when removing someone.
        //See School Class method, refreshDepartment(Employee)

        //Basically, a manager's department is a subset of the employees in the system. The manager's employee cannot exist in their department without existing in the EmployeeAPI
        ActionListener a = e ->
        {
            if (employees.size() != previousEmployees.size())
            {
                // a change was detected.
                ArrayList<Employee> removedEmployees = new ArrayList<>();
                for (Employee f : previousEmployees)
                {
                    if (!employees.contains(f))
                    {
                        //the change includes a removed employee. So remove that employee from their department, if they are in one.
                        school.refreshDepartments(f);
                        removedEmployees.add(f);
                        logger.log("Removed a Manager's employee, because they no longer exist in the EmployeeAPI");
                    }
                }
                if (!removedEmployees.isEmpty())
                {
                    previousEmployees.removeAll(removedEmployees);
                    //The previousEmployees ArrayList is now synced with the employees arraylist.
                    //Prevents ConcurrentModificationException
                }
            }
        };
        schoolUpdater = new Timer(5, a);
    }

    public void stopSchoolUpdater()
    {
        schoolUpdater.stop();
    }

    public void startSchoolUpdater()
    {
        schoolUpdater.start();
    }

    public void addEmployee(Employee employee)
    {
        employees.add(employee);
        previousEmployees.add(employee);
    }

    public Employee getEmployee(int index)
    {
        if (Utilities.validIndex(index, employees))
        {
            return employees.get(index);
        }
        return null;
    }

    public boolean doesEmployeeExist(Employee e)
    {
        for (Employee f : employees)
        {
            if (f.equals(e))
            {
                return true;
            }
        }
        return false;
    }

    public Employee removeEmployee(int index)
    {
        if (Utilities.validIndex(index, employees))
        {
            return employees.remove(index);
        } else
        {
            return null;
        }
    }

    public Employee removeEmployee(String secondName)
    {
        if (Utilities.max10Chars(secondName))
        {
            for (Employee e : employees)
            {
                if (e.getSecondName().equals(secondName))
                {
                    return employees.remove(employees.indexOf(e));
                }
            }
        }
        return null;
    }

    public int numberOfEmployees()
    {
        return employees.size();
    }

    public String listEmployees()
    {
        if (!employees.isEmpty())
        {
            String listOfEmployees = "";
            for (Employee e : employees)
            {
                listOfEmployees = listOfEmployees.concat("\n" + e.toString());
            }
            return listOfEmployees;
        }
        return "There are no employees in the school.";
    }

    public int searchEmployees(String secondName)
    {
        if (!employees.isEmpty())
        {
            for (Employee e : employees)
            {
                if (e.getSecondName().equalsIgnoreCase(secondName))
                {
                    return employees.indexOf(e);
                }
            }
        }
        return -1;
    }

    public String listManagerEmployees(Manager manager)
    {
        String returnString = "";
        if (employees.contains(manager))
        {
            if (!manager.getDept().isEmpty())
            {
                for (Employee e : manager.getDept())
                {
                    returnString = returnString.concat("\nIndex: " + manager.getDept().indexOf(e) + "\n" + e.toString());
                }
            } else
            {
                returnString = "This Manager has no Employees in their department.";
            }
        } else
        {
            returnString = "This Manager does not exist.";
        }
        return returnString;
    }

    public String listManagerEmployees()
    {
        String returnString = "";
        if (!employees.isEmpty())
        {
            for (Employee e : employees)
            {
                try
                {
                    Manager m = (Manager) e;
                    returnString = returnString.concat("\nIndex: "
                            + employees.indexOf(e)
                            + "\n"
                            + m.toString());
                } catch (ClassCastException ignored)
                {
                    logger.log("While cycling through Employees, "
                            + e.toString() + "\nWas not of type Manager.");
                }
            }
            if (returnString.equals(""))
            {
                returnString = "There are no Managers in the system.";
            }
        } else
        {
            returnString = "There are no Employees in the system.";
        }
        return returnString;
    }

    public double totalSalariesOwed()
    {
        double total = 0;
        if (!employees.isEmpty())
        {
            for (Employee e : employees)
            {
                total += e.calculateSalary();
            }
        }
        return total;
    }

    public double avgSalaryOwed()
    {
        double avg = 0;
        if (!employees.isEmpty())
        {
            double total = 0;
            for (Employee e : employees)
            {
                total += e.calculateSalary();
            }
            avg = total / employees.size();
        }
        return avg;
    }

    public Employee employeeWithHighestPay()
    {
        Employee returnEmployee = null;
        if (!employees.isEmpty())
        {
            double highestPay = 0;
            for (Employee e : employees)
            {
                if (e.calculateSalary() > highestPay)
                {
                    returnEmployee = e;
                    highestPay = e.calculateSalary();
                }
            }
        }
        return returnEmployee;
    }

    public void sortEmployeesByFirstName()
    {
        if (!employees.isEmpty())
        {
            for (Employee a : employees)
            {
                for (Employee b : employees)
                {
                    if (!a.equals(b) && employees.indexOf(a) < employees.indexOf(b))
                    {
                        if (a.getFirstName().compareTo(b.getFirstName()) > 0)
                        {
                            swapEmployees(employees, employees.indexOf(a), employees.indexOf(b));
                        }
                    }
                }
            }
        }
    }

    public void sortEmployeesBySecondName()
    {
        if (!employees.isEmpty())
        {
            for (Employee a : employees)
            {
                for (Employee b : employees)
                {
                    if (!a.equals(b) && employees.indexOf(a) < employees.indexOf(b))
                    {
                        if (a.getSecondName().compareTo(b.getSecondName()) > 0)
                        {
                            swapEmployees(employees, employees.indexOf(a), employees.indexOf(b));
                        }
                    }
                }
            }
        }
    }

    public void save(String fileName, boolean overWrite) throws IOException
    {
        if (!overWrite)
        {
            try
            {
                Utilities.readObjectSave(fileName);
                FileAlreadyExistsException e = new FileAlreadyExistsException("The file you are trying to save to already exists.");
                logger.log(e);
                throw e;
            } catch (Exception ignored)
            {
                //Write save, because the file is non-existent anyway.
                try
                {
                    if (!Utilities.writeSave(fileName, employees))
                    {
                        logger.log("Unable to save the file." + "\nfileName = " + fileName + "\nEmployees = " + employees.toString());
                        JOptionPane.showMessageDialog(DriverGUI.frame, "Unable to save.", "Error", JOptionPane.ERROR_MESSAGE, DriverGUI.ICON);
                    }
                } catch (Exception e)
                {
                    logger.log(e);
                    throw e;
                }
            }
        } else
        {
            try
            {
                Utilities.writeSave(fileName, employees);
            } catch (Exception e)
            {
                logger.log(e);
                throw e;
            }
        }
    }

    public void load(String fileName, boolean overWrite) throws IOException, ClassNotFoundException
    {
        if (employees.isEmpty() || overWrite)
        {
            try
            {
                ArrayList<?> temp = Utilities.readSave(fileName);
                ArrayList<Employee> loadedEmployees = new ArrayList<>();
                for (Object object : temp)
                {
                    loadedEmployees.add((Employee) object);
                }
                employees = loadedEmployees;
            } catch (Exception e)
            {
                logger.log(e);
                throw e;
            }
        } else
        {
            IllegalStateException e = new IllegalStateException("There is already data in ArrayList<Employee>. Loading a file will replace it.");
            logger.log(e);
            throw e;
        }
    }

    private void swapEmployees(ArrayList<Employee> employees, int a, int b)
    {
        if (Utilities.validIndex(a, employees) && Utilities.validIndex(b, employees))
        {
            Employee x = employees.get(a);
            Employee y = employees.get(b);
            employees.set(a, y);
            employees.set(b, x);
        } else
        {
            ArrayIndexOutOfBoundsException e = new ArrayIndexOutOfBoundsException("Indices must be within the bounds of the ArrayList<Employee>");
            logger.log(e);
            throw e;
        }
    }

    //Getters and Setters

    public ArrayList<Employee> getEmployees()
    {
        return employees;
    }

    public void setEmployees(ArrayList<Employee> employees)
    {
        this.employees = employees;
    }

    public Employee createEmployee(JPanel panel, Driver driver, String firstName, String secondName, String ppsNumber, int payGradeIndex, int payLevelIndex, boolean isManager, boolean isLecturer, boolean isAdminWorker)
    {
        if (Utilities.validEmployee(panel, driver, firstName, secondName, ppsNumber, payGradeIndex, payLevelIndex, isManager, isLecturer, isAdminWorker))
        {
            if (isManager)
            {
                return new Manager(firstName, secondName, ppsNumber, payGradeIndex + 1);
            } else if (isLecturer)
            {
                return new Lecturer(firstName, secondName, ppsNumber, payLevelIndex + 1);
            } else if (isAdminWorker)
            {
                return new AdminWorker(firstName, secondName, ppsNumber, payGradeIndex + 1);
            } else
            {
                return null;
            }
        } else
        {
            return null;
        }
    }

    public boolean replaceEmployee(Employee oldEmployee, Employee newEmployee)
    {
        if (oldEmployee == null || newEmployee == null)
        {
            logger.log("An Employee was null while replacing in EmployeeAPI");
            return false;
        } else if (employees.contains(oldEmployee))
        {
            employees.add(newEmployee);
            employees.remove(oldEmployee);
            return true;
        } else
        {
            logger.log("Employee was not found, so it was not replaced.");
            return false;
        }
    }

    public boolean containsManagers()
    {
        for (Employee e : employees)
        {
            if (e instanceof Manager)
            {
                return true;
            }
        }
        return false;
    }

    public Manager findManager(String fullName)
    {
        for (Employee e : employees)
        {
            try
            {
                Manager m = (Manager) e;
                if ((e.getFirstName() + " " + e.getSecondName()).equals(fullName))
                {
                    return m;
                }
            } catch (Exception ignored)
            {
            }
        }
        return null;
    }

    public ArrayList<Employee> getPreviousEmployees()
    {
        return previousEmployees;
    }

    public Timer getSchoolUpdater()
    {
        return schoolUpdater;
    }

    public Driver getDriver()
    {
        return driver;
    }
}
