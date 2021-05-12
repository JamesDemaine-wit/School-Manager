package SchoolManager.GUI.DriverGUITabs.UpdateTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;

import java.util.ArrayList;

import static SchoolManager.Driver.logger;
import static SchoolManager.GUI.DriverGUI.ICON;

public class UpdateEmployee
{

    private JPanel updateEmployeePanel;
    private JComboBox<String> employeeComboBox;
    private JTextField firstNameTextField;
    private JTextField secondNameTextField;
    private JRadioButton managerRadioButton;
    private JRadioButton lecturerRadioButton;
    private JRadioButton adminWorkerRadioButton;
    private JTextField ppsTextField;
    private JComboBox<String> payGradeBox;
    private JComboBox<String> payLevelBox;
    private JButton updateEmployeeButton;
    private JButton cancelButton;
    private JLabel payLevelLabel;
    private JLabel payGradeLabel;
    private final Driver driver;
    private final JPanel parent;
    private Employee oldEmployee;

    public UpdateEmployee(JPanel parent, Driver driver)
    {
        this.driver = driver;
        this.parent = parent;
        populateComboBoxes(driver);
        populateFields();
        createListeners();
    }

    private void createListeners()
    {
        managerRadioButton.addActionListener(e ->
        {
            //I made the action listener set the radio button back to true if its reselected. I don't want nothing to be selected at all.
            managerRadioButton.setSelected(true);
            lecturerRadioButton.setSelected(false);
            adminWorkerRadioButton.setSelected(false);
            payGradeBox.setVisible(true);
            payGradeLabel.setVisible(true);
            payLevelBox.setVisible(false);
            payLevelLabel.setVisible(false);
        });
        lecturerRadioButton.addActionListener(e ->
        {
            lecturerRadioButton.setSelected(true);
            managerRadioButton.setSelected(false);
            adminWorkerRadioButton.setSelected(false);
            payGradeBox.setVisible(false);
            payGradeLabel.setVisible(false);
            payLevelBox.setVisible(true);
            payLevelLabel.setVisible(true);
        });
        adminWorkerRadioButton.addActionListener(e ->
        {
            adminWorkerRadioButton.setSelected(true);
            managerRadioButton.setSelected(false);
            lecturerRadioButton.setSelected(false);
            payGradeBox.setVisible(true);
            payGradeLabel.setVisible(true);
            payLevelBox.setVisible(false);
            payLevelLabel.setVisible(false);
        });
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        updateEmployeeButton.addActionListener(e ->
        {
            if (updateEmployee())
            {
                DriverGUI.changePanel(parent);
            }
        });
        employeeComboBox.addActionListener(e ->
        {
            oldEmployee = findSelectedEmployee();
            populateFields();
            boolean isManager = oldEmployee instanceof Manager;
            boolean isLecturer = oldEmployee instanceof Lecturer;
            boolean isAdminWorker = oldEmployee instanceof AdminWorker;
            managerRadioButton.setSelected(isManager);
            lecturerRadioButton.setSelected(isLecturer);
            adminWorkerRadioButton.setSelected(isAdminWorker);
            payGradeBox.setVisible(isManager || isAdminWorker);
            payGradeLabel.setVisible(isManager || isAdminWorker);
            payLevelBox.setVisible(isLecturer);
            payLevelLabel.setVisible(isLecturer);
        });
    }

    private void populateFields()
    {
        boolean isManager = oldEmployee instanceof Manager;
        boolean isLecturer = oldEmployee instanceof Lecturer;
        boolean isAdminWorker = oldEmployee instanceof AdminWorker;
        firstNameTextField.setText(oldEmployee.getFirstName());
        secondNameTextField.setText(oldEmployee.getSecondName());
        ppsTextField.setText(oldEmployee.getPpsNumber());
        if (isManager)
        {
            Manager manager = (Manager) oldEmployee;
            payGradeBox.setSelectedIndex(manager.getGrade() - 1);
        } else if (isAdminWorker)
        {
            AdminWorker adminWorker = (AdminWorker) oldEmployee;
            payGradeBox.setSelectedIndex(adminWorker.getGrade() - 1);
        } else if (isLecturer)
        {
            Lecturer lecturer = (Lecturer) oldEmployee;
            payLevelBox.setSelectedIndex(lecturer.getLevel() - 1);
        } else
        {
            payGradeBox.setSelectedIndex(0);
            payLevelBox.setSelectedIndex(0);
        }
    }

    private boolean updateEmployee()
    {
        oldEmployee = findSelectedEmployee();
        Employee newEmployee = driver.getEmpAPI().createEmployee(
                updateEmployeePanel,
                driver,
                firstNameTextField.getText(),
                secondNameTextField.getText(),
                ppsTextField.getText(),
                payGradeBox.getSelectedIndex(),
                payLevelBox.getSelectedIndex(),
                managerRadioButton.isSelected(),
                lecturerRadioButton.isSelected(),
                adminWorkerRadioButton.isSelected());
        if (newEmployee == null)
        {
            return false;
        }
        if (oldEmployee instanceof Manager)
        {
            Manager oldEmployeeManager = (Manager) oldEmployee;
            if (oldEmployeeManager.getDept().isEmpty() || (oldEmployee.equals(newEmployee) && newEmployee instanceof Manager))
            {
                if (driver.getEmpAPI().replaceEmployee(oldEmployee, newEmployee))
                {
                    return true;
                } else
                {
                    JOptionPane.showMessageDialog(updateEmployeePanel, "Employee already exists.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                    return false;
                }
            } else
            {
                String[] options = new String[]{"Yes", "No"};
                int option = JOptionPane.showOptionDialog(updateEmployeePanel,
                        "This Employee Manages other Employees.\nWould you like to change their type anyway?\n(This will delete the Department, but not the other Employees.)",
                        "Warning",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        ICON,
                        options,
                        options[0]);
                if (option == 0)
                {
                    if (driver.getEmpAPI().replaceEmployee(oldEmployee, newEmployee))
                    {
                        return true;
                    } else
                    {
                        JOptionPane.showMessageDialog(updateEmployeePanel, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                        return false;
                    }
                } else
                {
                    return false;
                }
            }
        } else
        {
            if (driver.getEmpAPI().replaceEmployee(oldEmployee, newEmployee))
            {
                return true;
            } else
            {
                JOptionPane.showMessageDialog(updateEmployeePanel, "Employee not found.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                return false;
            }
        }
    }


    private Employee findSelectedEmployee()
    {
        int index = employeeComboBox.getSelectedIndex();
        String fullName = employeeComboBox.getItemAt(index);
        for (Employee f : driver.getEmpAPI().getEmployees())
        {
            if (fullName.equals(f.getFirstName() + " " + f.getSecondName()))
            {
                return f;
            }
        }
        logger.log("A non-existent employee was selected in the combo box.");
        return null;
    }

    private void populateComboBoxes(Driver driver)
    {
        ArrayList<String> employeeNames = new ArrayList<>();
        if (!driver.getEmpAPI().getEmployees().isEmpty())
        {
            for (Employee e : driver.getEmpAPI().getEmployees())
            {
                if (employeeNames.isEmpty())
                {
                    oldEmployee = e;
                    boolean isManager = oldEmployee instanceof Manager;
                    boolean isLecturer = oldEmployee instanceof Lecturer;
                    boolean isAdminWorker = oldEmployee instanceof AdminWorker;
                    managerRadioButton.setSelected(isManager);
                    lecturerRadioButton.setSelected(isLecturer);
                    adminWorkerRadioButton.setSelected(isAdminWorker);
                    payGradeBox.setVisible(isManager || isAdminWorker);
                    payGradeLabel.setVisible(isManager || isAdminWorker);
                    payLevelBox.setVisible(isLecturer);
                    payLevelLabel.setVisible(isLecturer);
                }
                employeeNames.add(e.getFirstName() + " " + e.getSecondName());
            }
        }
        if (employeeNames.isEmpty())
        {
            employeeNames.add("There are no Employees.");
        }
        employeeComboBox.setModel(new DefaultComboBoxModel<>(employeeNames.toArray(new String[0])));
    }

    public JPanel getPanel()
    {
        return this.updateEmployeePanel;
    }
}
