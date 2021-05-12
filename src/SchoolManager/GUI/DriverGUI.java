package SchoolManager.GUI;

import SchoolManager.Driver;
import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeAPI;
import SchoolManager.GUI.DriverGUITabs.AddTab.AddDept;
import SchoolManager.GUI.DriverGUITabs.AddTab.AddEmpToDept;
import SchoolManager.GUI.DriverGUITabs.AddTab.AddEmployee;
import SchoolManager.GUI.DriverGUITabs.DeleteTab.DeleteDepartment;
import SchoolManager.GUI.DriverGUITabs.DeleteTab.DeleteEmployee;
import SchoolManager.GUI.DriverGUITabs.FindTab.*;
import SchoolManager.GUI.DriverGUITabs.ListTab.ListEmployeesByFirstName;
import SchoolManager.GUI.DriverGUITabs.ListTab.ListEmployeesBySecondName;
import SchoolManager.GUI.DriverGUITabs.ListTab.ListEmpsByDept;
import SchoolManager.GUI.DriverGUITabs.SearchTab.SearchEmployeeByDept;
import SchoolManager.GUI.DriverGUITabs.SearchTab.SearchEmployeeSecondName;
import SchoolManager.GUI.DriverGUITabs.UpdateTab.SwapDeptManager;
import SchoolManager.GUI.DriverGUITabs.UpdateTab.UpdateDept;
import SchoolManager.GUI.DriverGUITabs.UpdateTab.UpdateEmployee;
import SchoolManager.School;
import SchoolManager.Utilities;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import static SchoolManager.Driver.logger;
import static javax.swing.JFileChooser.FILES_ONLY;

@SuppressWarnings("unused")
public class DriverGUI implements ActionListener
{
    private Driver app;
    public JPanel menuPanel;
    private JButton addEmpButton;
    private JButton addDeptButton;
    private JButton deleteEmpButton;
    private JButton addEmpToDeptButton;
    private JButton deleteDeptButton;
    private JButton findManagerOfDeptButton;
    private JButton findSalaryOfEmpButton;
    private JButton listEmpsByDeptButton;
    private JButton listEmpsByFirstNameButton;
    private JButton findTotalSalariesDueButton;
    private JButton averageSalaryDueButton;
    private JButton listEmpsBySecondNameButton;
    private JButton findEmpBySecondNameButton;
    private JButton findEmpByDeptButton;
    private JButton empWithHighestPayButton;
    private JButton updateEmpButton;
    private JButton updateDeptButton;
    private JButton swapDeptButton;
    private JTabbedPane tabbedPane1;
    private JLabel imageLabel;
    private JButton changeSchoolNameButton;
    private JLabel schoolNameLabel;
    private JButton changeSchoolButton;
    private JButton addSchoolButton;
    private JButton removeSchoolButton;
    private JTable table1;
    private JMenuItem loadButton;
    private JMenuItem saveButton;
    private JMenuItem aboutDev;
    public static ImageIcon ICON = new ImageIcon(Objects.requireNonNull(DriverGUI.class.getResource("/images/icon.png")));
    public static JFrame frame;
    private static Runtime instance;

    //I decided to preserve the original driver class and use it as a field here.
    public DriverGUI()
    {
        app = new Driver(this);
        instance = Runtime.getRuntime();
        createUIComponents();
        firstRunSoLoadSave();
        createListeners();
    }

    public DriverGUI(boolean test)
    {
        app = new Driver(this);
        instance = Runtime.getRuntime();
        createUIComponents();
        createListeners();
    }

    private void firstRunSoLoadSave()
    {
        //First run, There obviously isn't anything loaded, so prompt to load a file.
        //Source:
        // https://mkyong.com/swing/java-swing-joptionpane-showoptiondialog-example/
        String[] options = new String[]{"Yes", "No"};
        int option = JOptionPane.showOptionDialog(frame,
                "Would you like to load a file?",
                "School Manager",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                ICON,
                options,
                options[0]);
        if (option == 0)
        {
            load();
        } else
        {
            updateSchoolName();
        }
    }

    private void createListeners()
    {
        aboutDev.addActionListener(e ->
        {
            try
            {
                Desktop.getDesktop().browse(new URI("https://github.com/JamesDemaine-wit/"));
                //Make the aboutDev clickable : source: https://www.codejava.net/java-se/swing/how-to-create-hyperlink-with-jlabel-in-java-swing
            } catch (IOException | URISyntaxException f)
            {
                logger.log(f);
            }
            //see custom create UI component method below
        });
        loadButton.addActionListener(e ->
        {
            savePrompt();//always save your work, don't be a pleb.
            load();
        });
        saveButton.addActionListener(e -> save(app));
        addEmpButton.addActionListener(e -> changePanel(new AddEmployee(menuPanel, app).getPanel()));
        addDeptButton.addActionListener(e -> addDept());
        addEmpToDeptButton.addActionListener(e -> addEmpToDept());
        changeSchoolNameButton.addActionListener(e -> updateSchoolName());
        changeSchoolButton.addActionListener(e -> changeSchool());
        addSchoolButton.addActionListener(e -> addSchool());
        removeSchoolButton.addActionListener(e -> removeSchool());
        deleteEmpButton.addActionListener(e -> deleteEmp());
        deleteDeptButton.addActionListener(e -> deleteDept());
        updateEmpButton.addActionListener(e -> updateEmp());
        updateDeptButton.addActionListener(e -> updateDept());
        swapDeptButton.addActionListener(e -> swapDept());
        findManagerOfDeptButton.addActionListener(e -> findManagerOfDept());
        findSalaryOfEmpButton.addActionListener(e -> findSalaryOfEmployee());
        findTotalSalariesDueButton.addActionListener(e -> findTotalSalariesDue());
        averageSalaryDueButton.addActionListener(e -> findAverageSalaryDue());
        empWithHighestPayButton.addActionListener(e -> findEmployeeWithHighestPay());
        listEmpsByDeptButton.addActionListener(e -> listEmpsByDept());
        listEmpsByFirstNameButton.addActionListener(e -> listEmpsByFirstName());
        listEmpsBySecondNameButton.addActionListener(e -> listEmpsBySecondName());
        findEmpBySecondNameButton.addActionListener(e -> searchEmployeeBySecondName());
        findEmpByDeptButton.addActionListener(e -> SearchEmployeeByDept());
        frame.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                super.windowClosing(e);
                savePrompt();
            }
        });
    }

    private void removeSchool()
    {
        if (app.getSchools().size() > 1)
        {
            changePanel(new RemoveSchool(menuPanel, app).getPanel());
        } else{
            JOptionPane.showMessageDialog(menuPanel, "There are no other Schools", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void SearchEmployeeByDept()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            if (!app.getSchool().getDepartments().isEmpty())
            {
                changePanel(new SearchEmployeeByDept(menuPanel, app).getPanel());
            } else
            {
                JOptionPane.showMessageDialog(menuPanel, "There are no Departments.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void searchEmployeeBySecondName()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new SearchEmployeeSecondName(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void listEmpsBySecondName()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new ListEmployeesBySecondName(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void listEmpsByFirstName()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new ListEmployeesByFirstName(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void listEmpsByDept()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            if (!app.getSchool().getDepartments().isEmpty())
            {
                changePanel(new ListEmpsByDept(menuPanel, app).getPanel());
            } else
            {
                JOptionPane.showMessageDialog(menuPanel, "There are no Departments.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void findEmployeeWithHighestPay()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new FindEmployeeWithHighestPay(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void findAverageSalaryDue()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new FindAverageSalaryDue(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void findTotalSalariesDue()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new FindTotalSalariesDue(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void addSchool()
    {
        String input = JOptionPane.showInputDialog(menuPanel, "Enter the name for the New School.", "School Manager", JOptionPane.QUESTION_MESSAGE);
        if (input == null)
        {
            return;
        }
        if (input.contains(";") || input.contains("\\"))
        {
            logger.log("Someone is trying to break something, Input was:\n" + input);
            //I accidentally pasted some code in the input dialog and it threw a load of exceptions. Does JOptionPane not check for malicious intent?
        }
        if (Utilities.max30Chars(input))
        {
            if (input.length() != 0)
            {
                app.createSchool(input);
            } else
            {
                addSchool();
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "Please enter a name with a maximum of 30 characters.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            addSchool();
        }
        this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, app.getSchool().getName()));
    }

    private void changeSchool()
    {
        if (app.getSchools().size() > 1)
        {
            changePanel(new ChangeSchool(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no other Schools.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void findSalaryOfEmployee()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new FindSalaryOfEmployee(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void findManagerOfDept()
    {
        if (!app.getSchool().getDepartments().isEmpty())
        {
            if (app.getEmpAPI().containsManagers())
            {
                changePanel(new FindManagerOfDept(menuPanel, app).getPanel());
            } else
            {
                JOptionPane.showMessageDialog(menuPanel, "There are no Managers.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Departments.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void addDept()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            if (app.getEmpAPI().containsManagers())
            {
                changePanel(new AddDept(menuPanel, app).getPanel());
            } else
            {
                JOptionPane.showMessageDialog(menuPanel, "There are no Managers.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void addEmpToDept()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            if (!app.getSchool().getDepartments().isEmpty())
            {
                changePanel(new AddEmpToDept(menuPanel, app).getPanel());
            } else
            {
                JOptionPane.showMessageDialog(menuPanel, "There are no Departments.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void deleteEmp()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new DeleteEmployee(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void deleteDept()
    {
        if (!app.getSchool().getDepartments().isEmpty())
        {
            changePanel(new DeleteDepartment(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Departments.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void swapDept()
    {
        if (app.getSchool().getDepartments().size() > 2)
        {
            changePanel(new SwapDeptManager(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There aren't enough Departments to swap.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void updateEmp()
    {
        if (!app.getEmpAPI().getEmployees().isEmpty())
        {
            changePanel(new UpdateEmployee(menuPanel, app).getPanel());
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Employees.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void updateDept()
    {
        if (!app.getSchool().getDepartments().isEmpty())
        {
            if (!app.getEmpAPI().containsManagers())
            {
                changePanel(new UpdateDept(menuPanel, app).getPanel());
            } else
            {
                JOptionPane.showMessageDialog(menuPanel, "There are no Managers.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "There are no Departments.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
        }
    }

    private void savePrompt()
    {
        String[] options = new String[]{"Yes", "No"};
        int option = JOptionPane.showOptionDialog(frame,
                "Would you like to save?",
                "School Manager",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                ICON,
                options,
                options[0]);
        if (option == 0)
        {
            save(app);
        }
    }

    private void updateSchoolName()
    {
        String input = JOptionPane.showInputDialog(menuPanel, "Enter the name for the School.", "School Manager", JOptionPane.QUESTION_MESSAGE);
        if (input == null)
        {
            return;
        }
        if (input.contains(";") || input.contains("\\"))
        {
            logger.log("Someone is trying to break something, Input was:\n" + input);
            //I accidentally pasted some code in the input dialog and it threw a load of exceptions. Does JOptionPane not check for malicious intent?
        }
        if (Utilities.max30Chars(input))
        {
            if (input.length() != 0)
            {
                app.getSchool().setName(input);
                schoolNameLabel.setText(input);
                frame.repaint();
            } else
            {
                updateSchoolName();
            }
        } else
        {
            JOptionPane.showMessageDialog(menuPanel, "Please enter a name with a maximum of 30 characters.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            updateSchoolName();
        }
    }

    public static void changePanel(JPanel panel)
    {
        frame.setContentPane(panel);
        frame.setMinimumSize(new Dimension(500, 500));
        frame.pack();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.repaint();
        if (!frame.isVisible())
        {
            frame.setVisible(true);
        }
        instance.gc();
    }

    public JPanel getMenuPanel()
    {
        return menuPanel;
    }

    public static void save(Driver app)
    {
        //set up the window
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setPreferredSize(new Dimension(700, 525));

        //Filter the file type to XML only
        fileChooser.setFileSelectionMode(FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML File (.xml)", "xml"));
        fileChooser.setDialogTitle("Specify a file to save");

        //Start in details view instead of list view. Source: https://stackoverflow.com/questions/16292502/how-can-i-start-the-jfilechooser-in-the-details-view
        fileChooser.getActionMap().get("viewTypeDetails").actionPerformed(null);

        //get the selection
        int userSelection = fileChooser.showSaveDialog(frame);
        //save the file
        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().contains(".xml"))
            {
                fileToSave = new File(fileToSave.getPath() + ".xml");
            }
            try
            {
                if (Utilities.writeObjectSave(fileToSave.getAbsolutePath(), app))
                {
                    logger.log("Saved as file: " + fileToSave.getAbsolutePath());
                } else
                {
                    logger.log("Unable to save file." + "\nfileName = " + fileToSave.getAbsolutePath());
                    JOptionPane.showMessageDialog(frame, "Unable to save.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                }
            } catch (IOException e)
            {
                logger.log(e);
                JOptionPane.showMessageDialog(frame, "File not found.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                save(app);
            }
        }
    }

    private void load()
    {
        //set up the window
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        fileChooser.setPreferredSize(new Dimension(700, 500));

        //There is no way to change the location of the window without overriding the create dialog method. I don't have enough coffee to do this.

        //Filter the file type to XML only
        fileChooser.setFileSelectionMode(FILES_ONLY);
        fileChooser.setFileFilter(new FileNameExtensionFilter("XML File (.xml)", "xml"));
        fileChooser.setDialogTitle("Specify a file to open");

        //Start in details view instead of list view. Source: https://stackoverflow.com/questions/16292502/how-can-i-start-the-jfilechooser-in-the-details-view
        fileChooser.getActionMap().get("viewTypeDetails").actionPerformed(null);

        //get the selection
        int userSelection = fileChooser.showOpenDialog(menuPanel);
        //save the file
        if (userSelection == JFileChooser.APPROVE_OPTION)
        {
            File fileToOpen = fileChooser.getSelectedFile();
            if (fileToOpen.getName().contains(".xml"))
            {
                try
                {
                    Object o = Utilities.readObjectSave(fileToOpen.getAbsolutePath());
                    if (o != null)
                    {
                        try
                        {
                            Driver a = (Driver) o;
                            ArrayList<Employee> k = a.getEmpAPI().getEmployees();
                            HashMap<School, EmployeeAPI> l = a.getSchools();
                            School m = a.getSchool();
                            EmployeeAPI n = a.getEmpAPI();
                            assert (k != null && l != null && m != null && n != null);
                            app = (Driver) o;
                            schoolNameLabel.setText(app.getSchool().getName());
                            logger.log("Loaded file: " + fileToOpen.getAbsolutePath());
                        } catch (Exception e)
                        {
                            logger.log(e);
                            JOptionPane.showMessageDialog(menuPanel, "Incompatible file. Is it for an older version?", "Info", JOptionPane.INFORMATION_MESSAGE, ICON);
                            load();
                        } catch (AssertionError f)
                        {
                            logger.log(Arrays.toString(f.getStackTrace()));
                            JOptionPane.showMessageDialog(menuPanel, "Incompatible file. Is it for an older version?", "Info", JOptionPane.INFORMATION_MESSAGE, ICON);
                            load();
                        }
                    } else
                    {
                        JOptionPane.showMessageDialog(menuPanel, "That file is empty.", "Info", JOptionPane.INFORMATION_MESSAGE, ICON);
                        load();
                    }
                } catch (IOException e)
                {
                    logger.log(e);
                    JOptionPane.showMessageDialog(menuPanel, "File not found.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                    load();
                } catch (ClassNotFoundException | ClassCastException e)
                {
                    logger.log(e);
                    JOptionPane.showMessageDialog(menuPanel, "Incompatible file.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                    load();
                }
            } else
            {
                logger.log("Incompatible file." + fileToOpen.getAbsolutePath());
                JOptionPane.showMessageDialog(menuPanel, "Incompatible file.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                load();
            }
        }
    }

    private void createUIComponents()
    {
        // TODO: place custom component creation code here
        imageLabel = new JLabel(ICON);

        //Create custom MenuBar
        JMenuBar menuBar = new JMenuBar();
        //Create MenuBar Menus and add them to the menu bar
        JMenu fileMenu = new JMenu("File");
        JMenu aboutMenu = new JMenu("About");
        //Add buttons to the File Menu
        loadButton = new JMenuItem("Load");
        saveButton = new JMenuItem("Save as");
        fileMenu.add(loadButton);
        fileMenu.add(saveButton);
        //Add buttons to the About Menu
        aboutDev = new JMenuItem("About the Developer");
        //Make the aboutDev clickable : source: https://www.codejava.net/java-se/swing/how-to-create-hyperlink-with-jlabel-in-java-swing
        aboutDev.setForeground(Color.BLUE.darker());
        aboutDev.setCursor(new Cursor(Cursor.HAND_CURSOR));
        aboutDev.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseExited(MouseEvent e)
            {
                aboutDev.setText("About the Developer");
            }

            @Override
            public void mouseEntered(MouseEvent e)
            {
                aboutDev.setText("<html><a href='https://github.com/JamesDemaine-wit/'>" + "About the Developer" + "</a></html>");
            }
        });
        aboutMenu.add(aboutDev);
        menuBar.add(fileMenu);
        menuBar.add(aboutMenu);
        //finally, add the custom menu bar. Why couldn't intellij include it in the form builder?
        frame.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        logger.log(e.toString() + "\n\t" + e.getActionCommand());
        schoolNameLabel.setText(e.getActionCommand());
        frame.repaint();
    }
}
