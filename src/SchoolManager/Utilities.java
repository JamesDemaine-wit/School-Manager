package SchoolManager;

import SchoolManager.EmployeeManager.Employee;
import SchoolManager.EmployeeManager.EmployeeAPI;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.security.NoTypePermission;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;

import static SchoolManager.Driver.logger;
import static SchoolManager.GUI.DriverGUI.ICON;

@SuppressWarnings("unused")
public class Utilities
{

    public static boolean max10Chars(String string)
    {
        return string.length() <= 10;
    }

    public static boolean max30Chars(String string)
    {
        return string.length() <= 30;
    }

    public static boolean validIntRange(int start, int end, int value)
    {
        return value >= start && value <= end;
    }

    public static boolean validIntNonNegative(int number)
    {
        return (number >= 0);
    }

    public static boolean validDoubleNonNegative(double number)
    {
        return (number >= 0);
    }

    public static boolean validIndex(int number, int indexSize)
    {
        return (number >= 0 && number < indexSize);
    }

    public static boolean validIndex(int number, ArrayList<?> arrayList)
    {
        return (number >= 0 && number < arrayList.size());
    }

    //Saving and Loading arrayLists. I tried to keep these generic, can be useful elsewhere.

    public static ArrayList<?> readSave(String fileName) throws IOException, ClassNotFoundException
    {
        XStream xstream = new XStream(new DomDriver());
        ArrayList<?> arrayList;
        xstream.addPermission(NoTypePermission.NONE);//Suppress warning, found on stackoverflow
        xstream.allowTypesByRegExp(new String[]{".*"});//Suppress warning
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(fileName));
        arrayList = (ArrayList<?>) in.readObject();
        in.close();
        return arrayList;
    }

    public static Object readObjectSave(String fileName) throws IOException, ClassNotFoundException
    {
        Object object;
        XStream xstream = new XStream(new DomDriver());
        xstream.addPermission(NoTypePermission.NONE);//Suppress warning, found on stackoverflow
        xstream.allowTypesByRegExp(new String[]{".*"});//Suppress warning
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(fileName));
        object = in.readObject();
        //I had a nullpointer exception here, fixed it by making the Driver's listener static, so it is excluded from the save.
        //TODO update the loaded Driver's listener with the current DriverGUI
        in.close();
        return object;
    }

    public static boolean writeSave(String fileName, ArrayList<?> arrayList) throws IOException
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.addPermission(NoTypePermission.NONE);//Suppress warning
        xstream.allowTypesByRegExp(new String[]{".*"});//Suppress warning
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(fileName));
        out.writeObject(arrayList);
        out.flush();
        out.close();
        return true;//Not really necessary? but I use it to check anyway.
    }

    public static boolean writeObjectSave(String fileName, Object o) throws IOException
    {
        XStream xstream = new XStream(new DomDriver());
        xstream.addPermission(NoTypePermission.NONE);//Suppress warning
        xstream.allowTypesByRegExp(new String[]{".*"});//Suppress warning
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(fileName));
        out.writeObject(o);
        out.flush();
        out.close();
        return true;//Not really necessary? but I use it to check anyway.
    }

    //application specific methods

    public static boolean validPPS(String pps)
    {
        return pps.matches("[0-9]{7}[A-z]{2}");
    }

    public static boolean validLecturerLevel(int level)
    {
        return validIntRange(1, 3, level);
    }

    public static boolean validManagerLevel(int level)
    {     //level or grade?
        //Why do the instructions say a top bound of 4, but the test calls for 7?
        //.here TODO Question for Siobhan/Mairead
        return validIntRange(1, 7, level);
    }

    public static boolean validAdminLevel(int level)
    {
        return validManagerLevel(level);
    }

    public static double getSalaryForLecturerLevel(int level)
    {
        if (validIntRange(1, 3, level))
        {
            return level * 1000;
        } else
        {
            return -1;
        }
    }

    public static double getSalaryForAdminGrade(int grade)
    {
        if (validIntRange(1, 4, grade))
        {
            return grade * 700;
        } else
        {
            return -1;
        }
    }

    public static double getSalaryForManagerGrade(int grade)
    {
        return getSalaryForAdminGrade(grade);
    }

    public static ArrayList<String> filterEmployeesByName(String string, ArrayList<String> arrayList)
    {
        ArrayList<String> employees = new ArrayList<>();
        for (String e : arrayList)
        {
            if (e.toLowerCase().contains(string.toLowerCase()))
            {
                employees.add(e);
            }
        }
        return employees;
    }

    public static boolean validEmployee(JPanel panel, Driver driver, String firstName, String secondName, String ppsNumber, int payGradeIndex, int payLevelIndex, boolean isManager, boolean isLecturer, boolean isAdminWorker)
    {
        int payGrade = 0;
        int payLevel = 0;
        if (isManager || isAdminWorker)
        {
            payGrade = payGradeIndex + 1;
        } else if (isLecturer)
        {
            payLevel = payLevelIndex + 1;
        } else
        {
            JOptionPane.showMessageDialog(panel, "Invalid data was entered.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            return false;
        }
        if (Utilities.max10Chars(firstName))
        {
            if (Utilities.max10Chars(secondName))
            {
                if (Utilities.validPPS(ppsNumber))
                {
                    if (validManagerLevel(payGrade) && (isManager || isAdminWorker))
                    {
                        return true;
                    } else if (validLecturerLevel(payLevel) && isLecturer)
                    {
                        return true;
                    } else
                    {
                        JOptionPane.showMessageDialog(panel, "Something went wrong.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                        logger.log("No radio button was selected.");
                        return false;
                    }
                } else
                {
                    JOptionPane.showMessageDialog(panel, "Invalid PPS was entered.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                    return false;
                }
            } else
            {
                JOptionPane.showMessageDialog(panel, "Invalid Second Name was entered.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
                return false;
            }
        } else
        {
            JOptionPane.showMessageDialog(panel, "Invalid First Name was entered.", "Error", JOptionPane.ERROR_MESSAGE, ICON);
            return false;
        }
    }

}