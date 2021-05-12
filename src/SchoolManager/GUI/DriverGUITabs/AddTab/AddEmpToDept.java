package SchoolManager.GUI.DriverGUITabs.AddTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import static SchoolManager.Driver.logger;
import static SchoolManager.GUI.DriverGUI.ICON;

public class AddEmpToDept
{
    private JButton addEmployeeButton;
    private JButton cancelButton;
    private JComboBox<String> empComboBox;
    private JComboBox<String> deptComboBox;
    private JPanel appEmpToDeptPanel;
    private String[] deptNames;
    private HashMap<String, Manager> departments;

    public AddEmpToDept(JPanel parent, Driver driver)
    {
        departments = driver.getSchool().getDepartments();
        populateComboBoxes(driver);
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        addEmployeeButton.addActionListener(e ->
        {
            Employee f = findEmployee(driver);
            Manager m = findManager(driver);
            if (f != null)
            {
                if (m != null)
                {
                    if (!m.getDept().contains(f))
                    {
                        /*
                        TODO question for Siobhan/Mairead
                        I used a weird trick that intellij suggested to get a value out of the hashmap foreach loop. It
                        involved an atomic reference. Does hashmap foreach execute on separate threads??
                        I looked this up, and found that I could instead use a for loop on a collection of the
                        hashmap values only.

                        The main reason I needed this is that I cannot negate the check as it needs to be run on
                        all the managers and return the method if the case is true, preventing the method from
                        continuing and accidentally adding an employee already in a different department.
                         */
                        for(Manager manager : departments.values()){
                            if (manager != m && manager.getDept().contains(f))
                            {
                                JOptionPane.showMessageDialog(appEmpToDeptPanel, "This Employee is already in a different department.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                                return;
                            }
                        }
                        m.addDeptEmployee(f);
                        DriverGUI.changePanel(parent);
                    } else
                    {
                        JOptionPane.showMessageDialog(appEmpToDeptPanel, "This Employee is already in this Department.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                    }
                } else
                {
                    JOptionPane.showMessageDialog(appEmpToDeptPanel, "There are no Managers.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                }
            } else
            {
                JOptionPane.showMessageDialog(appEmpToDeptPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            }
        });
    }

    //These finders have to be unique to each panel, as they have different filters/etc. to check.
    private Manager findManager(Driver driver)
    {
        int index = deptComboBox.getSelectedIndex();
        String name = deptNames[index];
        return driver.getSchool().getDepartments().get(name); //Will return null if doesn't exist anyway, no need for checks.
    }

    private Employee findEmployee(Driver driver)
    {
        int index = empComboBox.getSelectedIndex();
        String fullName = empComboBox.getItemAt(index);
        for (Employee e : driver.getEmpAPI().getEmployees())
        {
            if (fullName.equals(e.getFirstName() + " " + e.getSecondName()))
            {
                return e;
            }
        }
        return null;
    }

    private void populateComboBoxes(Driver driver)
    {
        ArrayList<String> employeeNames = new ArrayList<>();
        if (!driver.getEmpAPI().getEmployees().isEmpty())
        {
            for (Employee e : driver.getEmpAPI().getEmployees())
            {
                try
                {
                    Manager m = (Manager) e;
                    logger.log("Employee: " + m.toString() + "\nIs a Manager, because casting succeeded. They will not be added to the combo box.");
                    //filter out anyone who is a manager. you can't be your own manager... can you? although, this also means that a manager cant manage another manager.
                } catch (Exception ignore)
                {
                    //add all non managers
                    employeeNames.add(e.getFirstName() + " " + e.getSecondName());
                }
            }
        }
        if (employeeNames.isEmpty())
        {
            employeeNames.add("There are no Employees.");
        }
        empComboBox.setModel(new DefaultComboBoxModel<>(employeeNames.toArray(new String[0])));
        if (!driver.getSchool().getDepartments().isEmpty())
        {
            deptNames = driver.getSchool().getDepartments().keySet().toArray(new String[0]);
        } else
        {
            //maybe this would have been easier with an arraylist instead of an array?
            //TODO Question for Mairead/Siobhan, is there a less clunky way of doing this?
            deptNames = new String[1];
            deptNames[0] = "There are no Departments.";
        }
        deptComboBox.setModel(new DefaultComboBoxModel<>(deptNames));
    }

    public JPanel getPanel()
    {
        return this.appEmpToDeptPanel;
    }
}
