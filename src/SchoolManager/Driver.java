package SchoolManager;

import SchoolManager.EmployeeManager.EmployeeAPI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

@SuppressWarnings("FieldMayBeFinal")
public class Driver
{

    public static final Logger logger = new Logger();
    private EmployeeAPI empAPI;
    private School school;
    private HashMap<School, EmployeeAPI> schools;
    private static ActionListener listener;

    public Driver()
    {
        schools = new HashMap<>();
        logger.log("Hello world!");
        school = new School("School", this);
        empAPI = new EmployeeAPI(this);
        schools.put(school, empAPI);// I am hoping this will prevent data loss when swapping schools. TODO TEST THIS
        listener = null;
    }

    public Driver(ActionListener listener)
    {
        Driver.listener = listener;
        schools = new HashMap<>();
        logger.log("Hello world!");
        school = new School("School", this);
        empAPI = new EmployeeAPI(this);
        schools.put(school, empAPI);// I am hoping this will prevent data loss when swapping schools. TODO TEST THIS
        if (listener != null)
        {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, school.getName()));
        }
    }


    public void createSchool(String schoolName)
    {
        school = new School(schoolName, this);
        empAPI = new EmployeeAPI(this);
        schools.putIfAbsent(school, empAPI);// I am hoping this will prevent data loss when swapping schools.
        logger.log("Created a new school: " + schoolName);
        if (listener != null)
        {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, school.getName()));
        }
    }

    public boolean deleteSchool(String schoolName)
    {
        /*
        TODO why couldn't foreach allow remove, like it allows when iterating over the keySet()?
         I tried to use the size to test for removal. big flop
        schools.forEach((s, e) ->
        {
            if (s.getName().equals(schoolName))
            {
                if (!s.equals(school))
                {
                    school = new School("No School Loaded", this);
                    empAPI = new EmployeeAPI(this);
                } else
                {
                    schools.remove(s, e);
                }
            }
        });
         */
        for (School s : schools.keySet())
        {
            if (s.getName().equals(schoolName))
            {
                if (!s.equals(school))
                {
                    school = new School("School", this);
                    empAPI = new EmployeeAPI(this);
                } else
                {
                    schools.remove(s, schools.get(s));
                }
                if (listener != null)
                {
                    listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, school.getName()));
                }
                return true;
            }
        }
        return false;
    }

    public boolean changeWorkingSchool(String schoolName)
    {
        //TODO possibly rewrite this to use the keySet? the way it is now should work fine, because there isn't any concurrent modification.
        empAPI.stopSchoolUpdater();
        //Make a backup, because this can go seriously wrong it's like transplanting every organ in your body at once.
        School previousSchool = new School(school);
        EmployeeAPI previousEmpAPI = new EmployeeAPI(empAPI, previousSchool);
        //I couldn't be arsed to implement cloneable, so I made extra constructors and did it myself.
        schools.forEach((s, e) ->
        {
            if (s.getName().equalsIgnoreCase(schoolName))
            {
                school = s;
                empAPI = e;
            }
        });
        //I opted against Object.equals() because it compares their referenced instances. these are entirely new instances, so they will never be the same.
        boolean success = !school.getName().equals(previousSchool.getName());
        if (!success)
        {
            //restore original state.
            //This should not have happened.
            school = previousSchool;
            empAPI = previousEmpAPI;
        }
        empAPI.startSchoolUpdater();
        if (listener != null)
        {
            listener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, school.getName()));
        }
        return (success);
        //This method would be a lot simpler if the hashmap foreach interface method wasn't as much of a prick as my cat. ☕
    }

    public EmployeeAPI getEmpAPI()
    {
        return this.empAPI;
    }

    public School getSchool()
    {
        return this.school;
    }

    public HashMap<School, EmployeeAPI> getSchools()
    {
        return this.schools;
    }
}
