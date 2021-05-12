package SchoolManager.GUI.DriverGUITabs.FindTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.util.ArrayList;

public class FindSalaryOfEmployee
{
    private JButton mainMenuButton;
    private JComboBox<String> employeeComboBox;
    private JLabel salaryLabel;
    private JPanel panel;
    private final ArrayList<String> employeeNames;

    public FindSalaryOfEmployee(JPanel parent, Driver driver)
    {
        employeeNames = new ArrayList<>();
        populateComboBox(driver);
        createListeners(parent, driver);
        updateSalary(driver);
    }

    private void updateSalary(Driver driver)
    {
        String employeeName = (String) employeeComboBox.getSelectedItem();
        for(Employee e : driver.getEmpAPI().getEmployees()){
            if((e.getFirstName() + " " + e.getSecondName()).equals(employeeName)){
                salaryLabel.setText("€" + String.format("%.2f", e.calculateSalary()));
            }
        }
    }

    private void populateComboBox(Driver driver)
    {
        for (Employee e : driver.getEmpAPI().getEmployees())
        {
            employeeNames.add(e.getFirstName() + " " + e.getSecondName());
        }
        employeeComboBox.setModel(new DefaultComboBoxModel<>(employeeNames.toArray(new String[0])));
        employeeComboBox.setSelectedIndex(0);
    }

    private void createListeners(JPanel parent, Driver driver)
    {
        mainMenuButton.addActionListener(e -> DriverGUI.changePanel(parent));
        employeeComboBox.addActionListener(e -> updateSalary(driver));
    }

    public JPanel getPanel()
    {
        return this.panel;
    }

}
