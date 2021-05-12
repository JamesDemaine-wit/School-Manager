package SchoolManager.GUI;

import SchoolManager.Driver;
import SchoolManager.School;

import javax.swing.*;
import java.util.ArrayList;

import static SchoolManager.GUI.DriverGUI.ICON;

public class RemoveSchool
{
    private JPanel panel;
    private JComboBox<String> schoolComboBox;
    private JButton removeSchoolButton;
    private JButton cancelButton;

    public RemoveSchool(JPanel parent, Driver driver)
    {
        removeSchoolButton.addActionListener(e -> removeSchool(parent, driver));
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        populateSchoolComboBox(driver);
    }

    private void populateSchoolComboBox(Driver driver)
    {
        School[] schools;
        ArrayList<String> schoolNames = new ArrayList<>();
        schools = driver.getSchools().keySet().toArray(new School[0]);
        for(School s : schools){
            if(!s.getName().equals(driver.getSchool().getName())){
                schoolNames.add(s.getName());
            }
        }
        schoolComboBox.setModel(new DefaultComboBoxModel<>(schoolNames.toArray(new String[0])));
        schoolComboBox.setSelectedIndex(0);
    }

    private void removeSchool(JPanel parent, Driver driver)
    {
        String schoolName = schoolComboBox.getItemAt(schoolComboBox.getSelectedIndex());
        School[] schools;
        schools = driver.getSchools().keySet().toArray(new School[0]);
        for(School s : schools){
            if(schoolName.equals(s.getName())){
                int result = JOptionPane.showConfirmDialog(panel, "Are you sure you want to remove this school?", "Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, ICON);
                if(result == JOptionPane.YES_OPTION){
                    driver.getSchools().remove(s);
                    DriverGUI.changePanel(parent);
                } else{
                    return;
                }
            }
        }
    }

    public JPanel getPanel()
    {
        return this.panel;
    }
}
