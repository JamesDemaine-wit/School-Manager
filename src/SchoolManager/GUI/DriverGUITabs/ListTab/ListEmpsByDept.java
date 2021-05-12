package SchoolManager.GUI.DriverGUITabs.ListTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import javax.swing.table.TableColumnModel;
import java.util.ArrayList;
import java.util.HashMap;

public class ListEmpsByDept
{
    private JPanel panel;
    private JButton doneButton;
    private JComboBox<String> deptComboBox;
    private JLabel managerLabel;
    private JPanel empPanel;
    private final HashMap<String, Manager> depts;
    private final String[] deptNames;

    public ListEmpsByDept(JPanel parent, Driver driver)
    {
        depts = driver.getSchool().getDepartments();
        deptNames = depts.keySet().toArray(new String[0]);
        populateComboBox();
        populateList();
        doneButton.addActionListener(e -> DriverGUI.changePanel(parent));
        deptComboBox.addActionListener(e -> populateList());
    }

    private void populateList()
    {
        String deptName = deptComboBox.getItemAt(deptComboBox.getSelectedIndex());
        Manager m = depts.get(deptName);
        managerLabel.setText(m.getFirstName() + " " + m.getSecondName());
        //Source: https://stackoverflow.com/questions/9639017/intellij-gui-creator-jpanel-gives-runtime-null-pointer-exception-upon-adding-an
        //I would use a JTable, but I wouldn't get it done in time for this assignment.
        empPanel.setLayout(new BoxLayout(empPanel, BoxLayout.PAGE_AXIS));
        for (Employee e : m.getDept())
        {
            JLabel empLabel = new JLabel(e.getFirstName() + " " + e.getSecondName());
            empPanel.add(empLabel);
        }
    }

    private void populateComboBox()
    {
        deptComboBox.setModel(new DefaultComboBoxModel<>(deptNames));
        deptComboBox.setSelectedIndex(0);
    }

    public JPanel getPanel()
    {
        return this.panel;
    }
}
