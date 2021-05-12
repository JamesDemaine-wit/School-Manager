package SchoolManager.GUI.DriverGUITabs.FindTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static SchoolManager.Driver.logger;
import static SchoolManager.GUI.DriverGUI.ICON;

public class FindEmployeeWithHighestPay
{
    private JPanel panel;
    private JButton doneButton;
    private JLabel salaryDueLabel;
    private JLabel payGradeLabel;
    private JLabel payLevelLabel;
    private JLabel ppsLabel;
    private JLabel secondNameLabel;
    private JLabel firstNameLabel;
    private JLabel payLevelLabelTitle;
    private JLabel payGradeLabelTitle;

    public FindEmployeeWithHighestPay(JPanel parent, Driver driver){
        findAndDisplayEmployee(driver, parent);
        doneButton.addActionListener(e -> DriverGUI.changePanel(parent));
    }

    private void findAndDisplayEmployee(Driver driver, JPanel parent)
    {
        ArrayList<Employee> employees = driver.getEmpAPI().getEmployees();
        double highestPay = 0;
        Employee emp = null;
        for(Employee e : employees){
            if(e.calculateSalary() > highestPay){
                highestPay = e.calculateSalary();
                emp = e;
            }
        }
        if(emp != null){
            salaryDueLabel.setText(String.format("%.2f", highestPay));
            ppsLabel.setText(emp.getPpsNumber());
            firstNameLabel.setText(emp.getFirstName());
            secondNameLabel.setText(emp.getSecondName());
            if(emp instanceof Manager || emp instanceof AdminWorker){
                payLevelLabelTitle.setVisible(false);
                payLevelLabel.setVisible(false);
                if(emp instanceof Manager){
                    Manager m = (Manager) emp;
                    payGradeLabel.setText(String.valueOf(m.getGrade()));
                } else{
                    AdminWorker a = (AdminWorker) emp;
                    payGradeLabel.setText(String.valueOf(a.getGrade()));
                }
            } else if(emp instanceof Lecturer){
                payGradeLabelTitle.setVisible(false);
                payGradeLabel.setVisible(false);
                payLevelLabel.setText(String.valueOf(((Lecturer) emp).getLevel()));
            }
        } else{
            logger.log("This person doesn't pay their staff, damn communists...");
            JOptionPane.showMessageDialog(panel, "You don't pay your staff", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            DriverGUI.changePanel(parent);
            //TODO its amazing how programming can stumble on outrageous possibilities such as this...
        }
    }

    public JPanel getPanel()
    {
        return this.panel;
    }
}
