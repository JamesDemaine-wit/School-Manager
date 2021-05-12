package SchoolManager.GUI;

import SchoolManager.Driver;
import SchoolManager.Main;
import SchoolManager.School;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static SchoolManager.Driver.logger;
import static SchoolManager.GUI.DriverGUI.ICON;

public class ChangeSchool
{
    private JPanel panel;
    private JComboBox<String> schoolComboBox;
    private JButton changeSchoolButton;
    private JButton cancelButton;
    private final ArrayList<String> schoolNames;

    public ChangeSchool(JPanel parent, Driver driver)
    {
        schoolNames = new ArrayList<>();
        populateComboBox(driver);
        createListeners(driver, parent);
    }

    private void createListeners(Driver driver, JPanel parent)
    {
        cancelButton.addActionListener(e -> DriverGUI.changePanel(parent));
        changeSchoolButton.addActionListener(e ->
        {
            if (driver.changeWorkingSchool((String) schoolComboBox.getSelectedItem()))
            {
                if("DefinitelyNotNull☕".equals(schoolComboBox.getSelectedItem())){
                    JOptionPane.showMessageDialog(panel, "It appears you haven't named this school yet. ☕", "Hmmm...", JOptionPane.INFORMATION_MESSAGE, ICON);
                }
                DriverGUI.changePanel(parent);
            } else
            {
                JOptionPane.showMessageDialog(parent, "A severe error has occurred.\nSee the log for details.\nPlease save your work.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                logger.log(driver.toString());
                logger.log("Error when changing working school.\nAre there schools with duplicate names?\nIs the save file corrupted?");
                DriverGUI.save(driver);
                System.exit(1);
                //I am not going to do this for every potential error. I just included it to fix a bug. It shouldn't ever get here again. ☕
                //btw, it was a corrupt file. I added fields to the driver and used an old save. got a nullPointer. maybe I should include checks?
            }
        });
    }

    private void populateComboBox(Driver driver)
    {
        for (School s : driver.getSchools().keySet())
        {
            if (!s.getName().equals(driver.getSchool().getName()))
            {
                schoolNames.add(s.getName());
            }
        }
        schoolComboBox.setModel(new DefaultComboBoxModel<>(schoolNames.toArray(new String[0])));
    }

    public JPanel getPanel()
    {
        return this.panel;
    }


}
