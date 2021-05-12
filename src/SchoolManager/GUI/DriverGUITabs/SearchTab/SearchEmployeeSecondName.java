package SchoolManager.GUI.DriverGUITabs.SearchTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

import static SchoolManager.GUI.DriverGUI.frame;
import static java.awt.event.KeyEvent.VK_BACK_SPACE;
import static java.awt.event.KeyEvent.VK_DELETE;

public class SearchEmployeeSecondName implements KeyListener
{
    private JPanel panel;
    private JTextField searchBox;
    private JButton doneButton;
    private JPanel empPanel;
    private ArrayList<String> empNames;
    private boolean searchBoxFocused;

    public SearchEmployeeSecondName(JPanel parent, Driver driver)
    {
        empNames = new ArrayList<>();
        //Source: https://stackoverflow.com/questions/9639017/intellij-gui-creator-jpanel-gives-runtime-null-pointer-exception-upon-adding-an
        //I would use a JTable, but I wouldn't get it done in time for this assignment.
        empPanel.setLayout(new BoxLayout(empPanel, BoxLayout.PAGE_AXIS));
        populateEmpPanel(driver);
        doneButton.addActionListener(e -> DriverGUI.changePanel(parent));
        searchBox.addKeyListener(this);
        searchBoxFocused = true;
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
    }

    private void populateEmpPanel(Driver driver)
    {
        for (Employee e : driver.getEmpAPI().getEmployees())
        {
            empNames.add(e.getSecondName() + ", " + e.getFirstName());
        }
        Collections.sort(empNames);
        for (String s : empNames)
        {
            JLabel label = new JLabel(s);
            empPanel.add(label);
        }
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
