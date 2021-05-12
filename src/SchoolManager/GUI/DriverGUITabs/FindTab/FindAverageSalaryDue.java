package SchoolManager.GUI.DriverGUITabs.FindTab;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeTypes.AdminWorker;
import SchoolManager.EmployeeManager.EmployeeTypes.Lecturer;
import SchoolManager.EmployeeManager.EmployeeTypes.Manager;
import SchoolManager.GUI.DriverGUI;

import javax.swing.*;
import java.util.ArrayList;

import static SchoolManager.Driver.logger;

public class FindAverageSalaryDue
{
    private JPanel panel;
    private JButton doneButton;
    private JLabel allEmployeesLabel;
    private JLabel managersLabel;
    private JLabel lecturersLabel;
    private JLabel adminWorkersLabel;

    public FindAverageSalaryDue(JPanel parent, Driver driver)
    {
        calculateAndDisplaySalaries(driver);
        doneButton.addActionListener(e -> DriverGUI.changePanel(parent));
    }

    private void calculateAndDisplaySalaries(Driver driver)
    {
        ArrayList<Employee> employees = driver.getEmpAPI().getEmployees();
        double allEmployees = 0;
        int employeeCounter = 0;
        double managers = 0;
        int managerCounter = 0;
        double lecturers = 0;
        int lecturerCounter = 0;
        double adminWorkers = 0;
        int adminCounter = 0;
        for (Employee e : employees)
        {
            double employeeSalary = e.calculateSalary(); //call the method once, why waste precious CPU?
            allEmployees += employeeSalary;
            employeeCounter++;
            if (e instanceof Manager)
            {
                managers += employeeSalary;
                managerCounter++;
            } else if (e instanceof Lecturer)
            {
                lecturers += employeeSalary;
                lecturerCounter++;
            } else if (e instanceof AdminWorker)
            {
                adminWorkers += employeeSalary;
                adminCounter++;
            }
        }
        logger.log("All Employees Salaries Average: " + allEmployees/employeeCounter
                + "\nManagers Salaries Average: " + managers/managerCounter
                + "\nLecturers Salaries Average: " + lecturers/lecturerCounter
                + "\nAdmin Workers Salaries Average: " + adminWorkers/adminCounter);
        allEmployeesLabel.setText("€" + String.format("%.2f", allEmployees/employeeCounter));
        managersLabel.setText("€" + String.format("%.2f", managers/managerCounter));
        lecturersLabel.setText("€" + String.format("%.2f", lecturers/lecturerCounter));
        adminWorkersLabel.setText("€" + String.format("%.2f", adminWorkers/adminCounter));
    }

    public JPanel getPanel()
    {
        return this.panel;
    }
}
