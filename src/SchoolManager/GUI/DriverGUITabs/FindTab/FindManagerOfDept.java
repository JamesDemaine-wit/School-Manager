package SchoolManager.GUI.DriverGUITabs.FindTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.util.HashMap;

public class FindManagerOfDept
{
    private JPanel findManagerOfDeptPanel;
    private JComboBox<String> deptComboBox;
    private JButton mainMenuButton;
    private JLabel managerNameLabel;
    private final HashMap<String, Manager> departments;

    public FindManagerOfDept(JPanel parent, Driver driver)
    {
        departments = driver.getSchool().getDepartments();
        departments.forEach((deptName, manager) -> deptComboBox.addItem(deptName));
        refreshManager();
        deptComboBox.addActionListener(e -> refreshManager());
        mainMenuButton.addActionListener(e -> DriverGUI.changePanel(parent));
    }


    private void refreshManager()
    {
        int index = deptComboBox.getSelectedIndex();
        String deptName = deptComboBox.getItemAt(index);
        Manager m = departments.get(deptName);
        managerNameLabel.setText(m.getFirstName() + " " + m.getSecondName());
    }

    public JPanel getPanel()
    {
        return this.findManagerOfDeptPanel;
    }

}
