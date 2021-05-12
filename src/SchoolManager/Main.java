package SchoolManager;

import SchoolManager.GUI.DriverGUI;

import javax.swing.*;

import static SchoolManager.Driver.logger;

public class Main
{
    public static void main(String[] args)
    {
        try
        {
            DriverGUI.frame = new JFrame("School Manager");
            DriverGUI.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            DriverGUI.frame.setIconImage(DriverGUI.ICON.getImage());
            try
            {
                UIManager.setLookAndFeel("com.jtattoo.plaf.aluminium.AluminiumLookAndFeel");
                //UI L&F source: http://www.jtattoo.net/Download.html
            } catch (Exception e)
            {
                logger.log(e);
            }
            DriverGUI.changePanel(new DriverGUI().getMenuPanel());
        } catch (Exception e)
        {
            logger.log(e);
            e.printStackTrace();
            System.err.println("Fatal Error.");
            System.exit(1);
        }
    }
}
