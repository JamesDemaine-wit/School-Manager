package SchoolManager.GUI.DriverGUITabs.UpdateTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;
import SchoolManager.Utilities;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

import static SchoolManager.GUI.DriverGUI.ICON;

public class UpdateDept
{
    private JComboBox<String> deptComboBox;
    private JTextField deptNameTextField;
    private JComboBox<String> managerComboBox;
    private JButton updateButton;
    private JButton cancelButton;
    private JPanel updateDepartmentPanel;
    private JLabel numberOfEmployeesLabel;
    private JCheckBox deleteEmployeesCheckBox;
    private final Driver driver;
    private final JPanel parent;
    private Manager selectedManager;
    private String selectedDepartment;
    private ArrayList<String> departmentNames;
    private ArrayList<String> managerNames;

    public UpdateDept(JPanel parent, Driver driver)
    {
        this.parent = parent;
        this.driver = driver;
        createListeners();
        populateManagerComboBox();
        populateDeptComboBox();
        updateEmployeesCheckBox();
        //TODO populate the text box with the selected dept. Also, refresh manager when department is changed.
    }

    private void updateEmployeesCheckBox()
    {
        if (selectedManager != null)
        {
            if (!selectedManager.getDept().isEmpty())
            {
                deleteEmployeesCheckBox.setEnabled(true);
                deleteEmployeesCheckBox.setSelected(false);
                numberOfEmployeesLabel.setText(String.valueOf(selectedManager.getDept().size()));
            } else
            {
                deleteEmployeesCheckBox.setEnabled(false);
                deleteEmployeesCheckBox.setSelected(false);
                numberOfEmployeesLabel.setText("0");
            }
        } else
        {
            deleteEmployeesCheckBox.setEnabled(false);
            deleteEmployeesCheckBox.setSelected(false);
            numberOfEmployeesLabel.setText("0");
        }
    }

    private boolean dataChanged()
    {
        String deptNameInput = deptNameTextField.getText();
        HashMap<String, Manager> departments = driver.getSchool().getDepartments();
        Manager oldManager = departments.get(departmentNames.get(deptComboBox.getSelectedIndex()));
        selectedManager = driver.getEmpAPI().findManager(managerNames.get(managerComboBox.getSelectedIndex()));
        selectedDepartment = departmentNames.get(deptComboBox.getSelectedIndex());
        boolean deleteEmployees = deleteEmployeesCheckBox.isSelected() && deleteEmployeesCheckBox.isEnabled();
        boolean isManagerTheSame = oldManager == selectedManager;
        boolean isDeptNameTheSame = deptNameInput.equals(selectedDepartment);
        boolean max30Chars = Utilities.max30Chars(deptNameInput);
        boolean emptyInput = deptNameInput.equals("");
        //I made local booleans to simplify the if branches.
        if (max30Chars && !emptyInput)
        {
            if (isDeptNameTheSame && isManagerTheSame)
            {
                if (deleteEmployees)
                {
                    selectedManager.getDept().clear();
                } else
                {
                    JOptionPane.showMessageDialog(updateDepartmentPanel, "Nothing was changed.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                }
                return true;
            } else if (isManagerTheSame)
            {
                if (deleteEmployees)
                {
                    selectedManager.getDept().clear();
                }
                //manager corresponds to the selected department. they are updating the dept name.
                Manager m = departments.get(selectedDepartment);
                departments.remove(selectedDepartment);
                departments.put(deptNameInput, m);
                return true;
            } else if (!isDeptNameTheSame)
            {
                if (deleteEmployees)
                {
                    selectedManager.getDept().clear();
                }
                //Manager is different and dept name is different. change both.
                departments.remove(selectedDepartment);
                departments.put(deptNameInput, selectedManager);
                return true;
            } else
            {
                if (deleteEmployees)
                {
                    selectedManager.getDept().clear();
                }
                //Manager is different, Department is the same.
                departments.put(deptNameInput, selectedManager);
                return true;
            }
        } else if (emptyInput)
        {
            JOptionPane.showMessageDialog(updateDepartmentPanel, "Please enter a Department name.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            return false;
        } else
        {
            JOptionPane.showMessageDialog(updateDepartmentPanel, "Department name must be 30 characters.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            return false;
        }
    }

    private void populateManagerComboBox()
    {
        managerNames = new ArrayList<>();
        for (Employee e : driver.getEmpAPI().getEmployees())
        {
            if (e instanceof Manager)
            {
                managerNames.add(e.getFirstName() + " " + e.getSecondName());
            }
        }
        if (managerNames.isEmpty())
        {
            managerNames.add("There are no Managers.");
            numberOfEmployeesLabel.setText("0");
        }
        managerComboBox.setModel(new DefaultComboBoxModel<>(managerNames.toArray(new String[0])));
    }

    private void populateDeptComboBox()
    {
        departmentNames = new ArrayList<>();
        driver.getSchool().getDepartments().forEach((deptName, manager) -> departmentNames.add(deptName));
        if (departmentNames.isEmpty())
        {
            departmentNames.add("There are no Departments.");
            selectedDepartment = departmentNames.get(0);
            selectedManager = null;
        } else
        {
            selectedDepartment = departmentNames.get(0);
            selectedManager = driver.getSchool().getDepartments().get(departmentNames.get(departmentNames.indexOf(selectedDepartment)));
        }
        deptComboBox.setModel(new DefaultComboBoxModel<>(departmentNames.toArray(new String[0])));
    }

    private void createListeners()
    {
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        updateButton.addActionListener(e ->
        {
            if (dataChanged())
            {
                DriverGUI.changePanel(parent);
            }
        });
    }

    public JPanel getPanel()
    {
        return this.updateDepartmentPanel;
    }
}
