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

public class FindTotalSalariesDue
{
    private JPanel panel;
    private JButton doneButton;
    private JLabel allEmployeesLabel;
    private JLabel managersLabel;
    private JLabel lecturersLabel;
    private JLabel adminWorkersLabel;

    public FindTotalSalariesDue(JPanel parent, Driver driver)
    {
        calculateAndDisplaySalaries(driver);
        doneButton.addActionListener(e -> DriverGUI.changePanel(parent));
    }

    private void calculateAndDisplaySalaries(Driver driver)
    {
        ArrayList<Employee> employees = driver.getEmpAPI().getEmployees();
        double allEmployees = 0;
        double managers = 0;
        double lecturers = 0;
        double adminWorkers = 0;
        for(Employee e : employees){
            double employeeSalary = e.calculateSalary(); //call the method once, why waste precious CPU?
            allEmployees += employeeSalary;
            if(e instanceof Manager){
                managers += employeeSalary;
            } else if(e instanceof Lecturer){
                lecturers += employeeSalary;
            } else if(e instanceof AdminWorker){
                adminWorkers += employeeSalary;
            }
        }
        logger.log("All Employees Salaries: " + allEmployees
                + "\nManagers Salaries: " + managers
                + "\nLecturers Salaries: " + lecturers
                + "\nAdmin Workers Salaries: " + adminWorkers);
        allEmployeesLabel.setText("€" + String.format("%.2f", allEmployees));
        managersLabel.setText("€" + String.format("%.2f", managers));
        lecturersLabel.setText("€" + String.format("%.2f", lecturers));
        adminWorkersLabel.setText("€" + String.format("%.2f", adminWorkers));
    }

    public JPanel getPanel()
    {
        return this.panel;
    }
}
