package SchoolManager.GUI.DriverGUITabs.DeleteTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.util.ArrayList;

import static SchoolManager.Driver.logger;
import static SchoolManager.GUI.DriverGUI.ICON;

public class DeleteEmployee
{
    private JButton deleteButton;
    private JButton cancelButton;
    private JComboBox<String> empComboBox;
    private JPanel deleteEmployeePanel;

    public DeleteEmployee(JPanel parent, Driver driver)
    {
        populateComboBoxes(driver);
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        deleteButton.addActionListener(e ->
        {
            if (deleteEmployee(driver))
            {
                DriverGUI.changePanel(parent);
            }
        });
    }

    private boolean deleteEmployee(Driver driver)
    {
        Employee e = findEmployee(driver);
        if (e != null)
        {
            try
            {
                Manager m = (Manager) e;
                if (driver.getSchool().getDepartments().containsValue(m))
                {
                    if (!m.getDept().isEmpty())
                    {
                        logger.log("The User tried to delete an employee who is still managing a department.");
                        String[] options = new String[]{"Yes", "No"};
                        int option = JOptionPane.showOptionDialog(deleteEmployeePanel,
                                "This Employee Manages other Employees.\nWould you like to delete them anyway?\n(This will delete the Department, but not the other Employees.)",
                                "Warning",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                ICON,
                                options,
                                options[0]);
                        if (option == 0)
                        {
                            driver.getEmpAPI().getEmployees().remove(e);
                            m.getDept().clear();
                            return true;
                        } else
                        {
                            return false;
                        }
                    } else
                    {
                        logger.log("Manager has no dept employees");
                        driver.getEmpAPI().getEmployees().remove(e);
                        /*
                        I am not confident that this will do what I intended. So I made a timer do it for me in a way I know will definitely work, as I am not relying on a user interaction.
                        An employee that doesn't exist in the EmployeeAPI should 100% no longer exist in a department.
                        Manager l = findEmployeeManager(driver, e);
                        if (l != null) {
                            l.getDept().remove(e);
                        }
                        */
                        return true;
                    }
                } else
                {
                    logger.log("Manager has no dept, unsure if they are really a manager");
                    driver.getEmpAPI().getEmployees().remove(e);
                    /*
                        I am not confident that this will do what I intended. So I made a timer do it for me in a way I know will definitely work, as I am not relying on a user interaction.
                        An employee that doesn't exist in the EmployeeAPI should 100% no longer exist in a department.
                        Manager l = findEmployeeManager(driver, e);
                        if (l != null) {
                            l.getDept().remove(e);
                        }
                        */
                    return true;
                }
            } catch (ClassCastException f)
            {
                logger.log(f);
                driver.getEmpAPI().getEmployees().remove(e);
                /*
                        I am not confident that this will do what I intended. So I made a timer do it for me in a way I know will definitely work, as I am not relying on a user interaction.
                        An employee that doesn't exist in the EmployeeAPI should 100% no longer exist in a department.
                        Manager l = findEmployeeManager(driver, e);
                        if (l != null) {
                            l.getDept().remove(e);
                        }
                        */
                return true;
            }
        }
        return false;
    }
/*
I am no longer using this, it seemed inconsistent. see method in EmployeeAPI, initSchoolUpdateTimer()
    private Manager findEmployeeManager(Driver driver, Employee e) {
        for(Employee f : driver.getEmpAPI().getEmployees()){
            try{
                Manager m = (Manager) f;
                for(Employee h : m.getDept()){
                    if(h.equals(e)){
                        return m;
                    }
                }
            } catch(ClassCastException g){
                logger.log(g);
            }
        }
        return null;
    }

 */

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
                employeeNames.add(e.getFirstName() + " " + e.getSecondName());
            }
        }
        if (employeeNames.isEmpty())
        {
            employeeNames.add("There are no Employees.");
        }
        empComboBox.setModel(new DefaultComboBoxModel<>(employeeNames.toArray(new String[0])));
    }

    public JPanel getPanel()
    {
        return this.deleteEmployeePanel;
    }
}
