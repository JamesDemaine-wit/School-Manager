package SchoolManager.GUI.DriverGUITabs.ListTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;

public class ListEmployeesByFirstName
{
    private JPanel panel;
    private JButton doneButton;
    private JPanel empPanel;
    private ArrayList<String> empNames;

    public ListEmployeesByFirstName(JPanel parent, Driver driver)
    {
        doneButton.addActionListener(e -> DriverGUI.changePanel(parent));
        //Source: https://stackoverflow.com/questions/9639017/intellij-gui-creator-jpanel-gives-runtime-null-pointer-exception-upon-adding-an
        //I would use a JTable, but I wouldn't get it done in time for this assignment.
        empPanel.setLayout(new BoxLayout(empPanel, BoxLayout.PAGE_AXIS));
        empNames = new ArrayList<>();
        populateEmpPanel(driver);
    }

    private void populateEmpPanel(Driver driver)
    {
        for(Employee e : driver.getEmpAPI().getEmployees()){
            empNames.add(e.getFirstName() + " " + e.getSecondName());
        }
        Collections.sort(empNames);
        for(String s : empNames){
            JLabel label = new JLabel(s);
            empPanel.add(label);
        }
    }

    public JPanel getPanel()
    {
        return this.panel;
    }
}
