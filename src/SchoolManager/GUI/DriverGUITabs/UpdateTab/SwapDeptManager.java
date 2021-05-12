package SchoolManager.GUI.DriverGUITabs.UpdateTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class SwapDeptManager
{
    private JPanel swapPanel;
    private JComboBox<String> dept1ComboBox;
    private JComboBox<String> dept2ComboBox;
    private JButton swapButton;
    private JButton cancelButton;
    private JLabel manager1Name;
    private JLabel manager2Name;
    private final JPanel parent;
    private final HashMap<String, Manager> departments;
    private int index1;
    private int index2;

    //Let's try something different for once. I wonder if it is easier.
    //Result... much much much better! ☕
    //This is the first place I am using the dept name string directly from the combo box. Why didn't i do this earlier?
    //The model and object system of JComboBox is clunky and stupid...

    public SwapDeptManager(JPanel parent, Driver driver)
    {
        this.parent = parent;
        departments = driver.getSchool().getDepartments();
        populateComboBoxes();
        refreshManagerNames();
        createListeners();
    }

    private void createListeners()
    {
        dept1ComboBox.addActionListener(e -> updateComboBoxOne());
        dept2ComboBox.addActionListener(e -> updateComboBoxTwo());
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        swapButton.addActionListener(e ->
        {
            if (swapManagers())
            {
                DriverGUI.changePanel(parent);
            }
        });
    }

    private boolean swapManagers()
    {
        index1 = dept1ComboBox.getSelectedIndex();
        index2 = dept2ComboBox.getSelectedIndex();
        String selectedDept1 = dept1ComboBox.getItemAt(index1);
        String selectedDept2 = dept2ComboBox.getItemAt(index2);
        Manager manager1 = departments.get(selectedDept1);
        Manager manager2 = departments.get(selectedDept2);
        return departments.replace(selectedDept1, manager1, manager2) && departments.replace(selectedDept2, manager2, manager1);
    }

    private void updateComboBoxOne()
    {
        int prevIndex1 = index1;
        index1 = dept1ComboBox.getSelectedIndex();
        index2 = dept2ComboBox.getSelectedIndex();
            if (index1 == index2)
            {
                dept2ComboBox.setSelectedIndex(prevIndex1); //The same department won't be in both boxes.
            }
            String selectedDept1 = dept1ComboBox.getItemAt(index1);
            Manager manager1 = departments.get(selectedDept1);
            manager1Name.setText(manager1.getFirstName() + " " + manager1.getSecondName());
    }

    private void updateComboBoxTwo()
    {
        int prevIndex2 = index2;
        index1 = dept1ComboBox.getSelectedIndex();
        index2 = dept2ComboBox.getSelectedIndex();
        if (index2 == index1)
        {
            dept1ComboBox.setSelectedIndex(prevIndex2); //The same department won't be in both boxes.
        }
        String selectedDept2 = dept2ComboBox.getItemAt(index2);
        Manager manager2 = departments.get(selectedDept2);
        manager2Name.setText(manager2.getFirstName() + " " + manager2.getSecondName());
    }

    private void refreshManagerNames()
    {
        int index1 = dept1ComboBox.getSelectedIndex();
        int index2 = dept2ComboBox.getSelectedIndex();
        String selectedDept1 = dept1ComboBox.getItemAt(index1);
        String selectedDept2 = dept2ComboBox.getItemAt(index2);
        Manager manager1 = departments.get(selectedDept1);
        Manager manager2 = departments.get(selectedDept2);
        manager1Name.setText(manager1.getFirstName() + " " + manager1.getSecondName());
        manager2Name.setText(manager2.getFirstName() + " " + manager2.getSecondName());
    }

    private void populateComboBoxes()
    {
        departments.forEach((deptName, manager) ->
        {
            dept1ComboBox.addItem(deptName);
            dept2ComboBox.addItem(deptName);
        });
        index2 = 1;
        index1 = 0;
        dept2ComboBox.setSelectedIndex(index2); //The same department won't be in both boxes.
    }

    public JPanel getPanel()
    {
        return this.swapPanel;
    }
}
