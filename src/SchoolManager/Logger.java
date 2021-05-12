package SchoolManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.StandardCharsets;

public class Logger
{

    /*
     Source for a lot of this class:
     https://attacomsian.com/blog/java-save-string-to-text-file
    */

    private final String fileName;
    private boolean isWorking;

    public Logger()
    {
        isWorking = true;
        //get the time and format it
        long time = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("dd_MMM_yyyy HH_mm_ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd_MMM_yyyy HH:mm:ss");
        Date resultDate = new Date(time);
        //set the file name and directory
        fileName = System.getProperty("user.dir") + "/logs/" + sdf.format(resultDate) + ".log";
        //create the file.
        write("Application started: " + sdf2.format(resultDate));
    }

    private void write(String whatToWrite)
    {
        //create the file if it doesn't already exist and then write to it.
        try
        {
            Files.writeString(Paths.get(fileName), whatToWrite, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            isWorking = true;
        } catch (Exception e)
        {
            try
            {
                File logsFolder = new File(System.getProperty("user.dir") + "/logs/");
                if (!logsFolder.exists())
                {
                    if (logsFolder.mkdir())
                    {
                        Files.writeString(Paths.get(fileName), whatToWrite, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                        isWorking = true;
                    }
                } else
                {
                    e.printStackTrace();
                    isWorking = false;
                }
            } catch (Exception f)
            {
                System.err.println("Unable to write to log file, was the logs folder deleted?");
                System.err.println("Log will not be recorded for the remainder of this session.");
                isWorking = false;
                f.printStackTrace();
            }
        }
    }

    public void log(Exception e)
    {
        if (isWorking)
        {
            long time = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date resultDate = new Date(time);
            write("\n" + sdf.format(resultDate) + ": " + Arrays.toString(e.getStackTrace()));
        }
    }

    public void log(String string)
    {
        if (isWorking)
        {
            long time = System.currentTimeMillis();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            Date resultDate = new Date(time);
            write("\n" + sdf.format(resultDate) + ": " + string);
        }
    }

}