package SchoolManager.GUI.DriverGUITabs.SearchTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static SchoolManager.GUI.DriverGUI.frame;

public class SearchEmployeeByDept implements KeyListener
{
    private JPanel panel;
    private JComboBox<String> deptComboBox;
    private JButton doneButton;
    private JPanel empPanel;
    private JTextField searchBox;
    private HashMap<String, Manager> depts;
    private boolean searchBoxFocused;
    private ArrayList<String> empNames;

    public SearchEmployeeByDept(JPanel parent, Driver driver)
    {
        depts = driver.getSchool().getDepartments();
        //Source: https://stackoverflow.com/questions/9639017/intellij-gui-creator-jpanel-gives-runtime-null-pointer-exception-upon-adding-an
        //I would use a JTable, but I wouldn't get it done in time for this assignment.
        empPanel.setLayout(new BoxLayout(empPanel, BoxLayout.PAGE_AXIS));
        empNames = new ArrayList<>();
        doneButton.addActionListener(e -> DriverGUI.changePanel(parent));
        searchBoxFocused = true;
        searchBox.addKeyListener(this);
        searchBox.addFocusListener(new FocusListener()
        {
            @Override
            public void focusGained(FocusEvent e)
            {
                searchBoxFocused = true;
            }

            @Override
            public void focusLost(FocusEvent e)
            {
                searchBoxFocused = false;
            }
        });
        populateDeptComboBox();
        deptComboBox.addActionListener(e -> populateEmpPanel());
        populateEmpPanel();
    }

    private void populateEmpPanel()
    {
        String deptName = deptComboBox.getItemAt(deptComboBox.getSelectedIndex());
        Manager m = depts.get(deptName);
        ArrayList<Employee> emps = m.getDept();
        for(Employee e : emps){
            empNames.add(e.getFirstName() + " " + e.getSecondName());
        }
        for(String s : empNames){
            JLabel label = new JLabel(s);
            empPanel.add(label);
        }
    }

    private void populateDeptComboBox()
    {
        String[] deptNames = depts.keySet().toArray(new String[0]);
        deptComboBox.setModel(new DefaultComboBoxModel<>(deptNames));
        deptComboBox.setSelectedIndex(0);
    }

    private void refresh()
    {
        empPanel.removeAll();
        String searchTerm = searchBox.getText();
        ArrayList<String> results = new ArrayList<>();
        for (String s : empNames)
        {
            if (s.toLowerCase().contains(searchTerm.toLowerCase()))
            {
                results.add(s);
            }
        }
        Collections.sort(results);
        for (String s : results)
        {
            JLabel label = new JLabel(s);
            empPanel.add(label);
        }
        empPanel.revalidate();
        frame.repaint();
    }

    public JPanel getPanel()
    {
        return this.panel;
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if (searchBoxFocused)
        {
            refresh();
        }
    }
}
