package me.Deex.SaltLib.Debug;

import java.io.FileOutputStream;

import javax.swing.filechooser.FileSystemView;

public class Instrumentor 
{
    private static FileOutputStream out;
    private static int profileCount;

    private static final String desktopPath = 
        FileSystemView.getFileSystemView().getHomeDirectory().getPath().concat("/Desktop/");

    public static void BeginSession(String name)
    {
        try
        {
            out = new FileOutputStream(desktopPath.concat(name).concat(".json"));
            WriteHeader();
        }
        catch (Exception e)
        {

        }
    }

    public static void EndSession(String name)
    {
        try
        {
            WriteFooter();
            out.close();
        }
        catch (Exception e)
        {

        }

        profileCount = 0;
    }

    private static void WriteHeader()
    {
        try
        {
            out.write("{\"otherData\": {},\"traceEvents\":[".getBytes());
            out.flush();
        }
        catch (Exception e)
        {

        }
    }

    private static void WriteFooter()
    {
        try
        {
            out.write("]}".getBytes());
            out.flush();
        }
        catch (Exception e)
        {

        }
    }

    public static void WriteProfile(String name, long start, long end, long threadID)
    {
        if (profileCount > 0)
        {
            try
            {
                out.write(",".getBytes());
            }
            catch (Exception e)
            {

            }
        }
        
        profileCount++;

        try
        {
            out.write("{".getBytes());
            out.write("\"cat\":\"function\",".getBytes());
            out.write("\"dur\":".concat(String.valueOf(end - start)).concat(",").getBytes());
            out.write("\"name\":\"".concat(name).concat("\",").getBytes());
            out.write("\"ph\":\"X\",".getBytes());
            out.write("\"pid\":0,".getBytes());
            out.write("\"tid\":".concat(String.valueOf(threadID)).concat(",").getBytes());
            out.write("\"ts\":".concat(String.valueOf(start)).getBytes());
            out.write("}".getBytes());
    
            out.flush();
        }
        catch (Exception e)
        {

        }

    }
}
