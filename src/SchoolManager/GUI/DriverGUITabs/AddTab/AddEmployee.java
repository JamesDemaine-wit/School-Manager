package SchoolManager.GUI.DriverGUITabs.AddTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;

import static SchoolManager.GUI.DriverGUI.ICON;

public class AddEmployee
{
    private JButton addEmployeeButton;
    private JButton cancelButton;
    private JRadioButton managerRadioButton;
    private JRadioButton lecturerRadioButton;
    private JRadioButton adminWorkerRadioButton;
    private JTextField firstNameTextField;
    private JTextField secondNameTextField;
    private JTextField ppsTextField;
    private JComboBox<String> payGradeBox;
    private JComboBox<String> payLevelBox;
    private JPanel addEmployeePanel;
    private JLabel payGradeLabel;
    private JLabel payLevelLabel;
    private final Driver driver;

    public AddEmployee(JPanel parent, Driver driver)
    {
        this.driver = driver;
        //Initialise to Manager type
        managerRadioButton.setSelected(true);
        lecturerRadioButton.setSelected(false);
        adminWorkerRadioButton.setSelected(false);
        payGradeBox.setVisible(true);
        payGradeLabel.setVisible(true);
        payLevelBox.setVisible(false);
        payLevelLabel.setVisible(false);
        //Listeners
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
        addEmployeeButton.addActionListener(e ->
        {
            if (validData())
            {
                DriverGUI.changePanel(parent);
            }
        });
    }

    private boolean validData()
    {
        Employee newEmployee = driver.getEmpAPI().createEmployee(addEmployeePanel,
                driver,
                firstNameTextField.getText(),
                secondNameTextField.getText(),
                ppsTextField.getText(),
                payGradeBox.getSelectedIndex(),
                payLevelBox.getSelectedIndex(),
                managerRadioButton.isSelected(),
                lecturerRadioButton.isSelected(),
                adminWorkerRadioButton.isSelected());
        if (newEmployee != null)
        {
            if (!driver.getEmpAPI().doesEmployeeExist(newEmployee))
            {
                driver.getEmpAPI().addEmployee(newEmployee);
                return true;
            } else
            {
                JOptionPane.showMessageDialog(addEmployeePanel, "Employee already exists.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                return false;
            }
        } else
        {
            return false;
        }
    }

    public JPanel getPanel()
    {
        return this.addEmployeePanel;
    }

}
