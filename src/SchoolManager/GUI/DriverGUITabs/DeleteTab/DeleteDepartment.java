package SchoolManager.GUI.DriverGUITabs.DeleteTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

import static SchoolManager.GUI.DriverGUI.ICON;

public class DeleteDepartment
{
    private JPanel deleteDepartmentPanel;
    private JButton deleteButton;
    private JButton cancelButton;
    private JComboBox<String> departmentComboBox;
    private ArrayList<String> departmentNames;

    public DeleteDepartment(JPanel parent, Driver driver)
    {
        populateComboBox(driver);
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        deleteButton.addActionListener(e ->
        {
            if (deleteDepartment(driver))
            {
                DriverGUI.changePanel(parent);
            }
        });
    }

    private boolean deleteDepartment(Driver driver)
    {
        //Maybe this is too sketchy...
        //Manager m = driver.getSchool().getDepartments().get((String)departmentComboBox.getSelectedItem());
        int index = departmentComboBox.getSelectedIndex();
        String key = departmentNames.get(index);
        Manager m = driver.getSchool().getDepartments().get(key);
        if (m != null)
        {
            if (!m.getDept().isEmpty())
            {
                String[] options = new String[]{"Yes", "No"};
                int option = JOptionPane.showOptionDialog(deleteDepartmentPanel,
                        "This Department is not empty.\nWould you like to delete it anyway?\n(This will delete the Department, but not its Employees.)",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        ICON,
                        options,
                        options[0]);
                if (option == 0)
                {
                    driver.getSchool().deleteDept(key);
                    m.getDept().clear();
                    return true;
                    //The employees will still exist, but not under that manager.
                } else
                {
                    return false;
                }
            } else
            {
                driver.getSchool().deleteDept(key);
                return true;
            }
        } else
        {
            return false;
        }
    }

    private void populateComboBox(Driver driver)
    {
        departmentNames = new ArrayList<>();
        if (!driver.getSchool().getDepartments().isEmpty())
        {
            departmentNames.addAll(Arrays.asList(driver.getSchool().getDepartments().keySet().toArray(new String[0])));
            //never seen Arrays.asList before, thanks IntelliJ
        } else
        {
            departmentNames.add("There are no Departments.");
            //I know I could have directly added the array directly, but I wouldn't have control over the logic if its empty.
            //Just a sanity check.
        }
        departmentComboBox.setModel(new DefaultComboBoxModel<>(departmentNames.toArray(new String[0])));
    }

    public JPanel getPanel()
    {
        return this.deleteDepartmentPanel;
    }
}
