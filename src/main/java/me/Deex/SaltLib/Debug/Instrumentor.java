package me.Deex.SaltLib.Debug;

import java.io.FileOutputStream;
import java.util.concurrent.Semaphore;

public class Instrumentor 
{
    private static FileOutputStream out;
    private static Semaphore profileWriteMutex = new Semaphore(1);
    private static boolean startWithComma = false;

    public static void BeginSession(String name)
    {
        new Thread(() -> 
        {
            try
            {
                out = new FileOutputStream(name.concat(".json"));
                WriteHeader();
                startWithComma = false;
            }
            catch (Exception e)
            {
                System.out.println("[SaltLib] Could not initalize a profiling session!");
                e.printStackTrace();
            }

        }).start();
    }

    public static void EndSession()
    {
        try
        {
            WriteFooter();
            out.close();
        }
        catch (Exception e)
        {

        }
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

    public synchronized static void WriteProfile(String name, long start, long end, long threadID)
    {
        try
        {
            StringBuilder stringBuild = new StringBuilder();
            
            if (startWithComma)
            {
                stringBuild.append(",");
            }

            stringBuild.append("{");
            stringBuild.append("\"cat\":\"function\",");
            stringBuild.append("\"dur\":".concat(String.valueOf(end - start)).concat(","));
            stringBuild.append("\"name\":\"".concat(name).concat("\","));
            stringBuild.append("\"ph\":\"X\",");
            stringBuild.append("\"pid\":0,");
            stringBuild.append("\"tid\":".concat(String.valueOf(threadID)).concat(","));
            stringBuild.append("\"ts\":".concat(String.valueOf(start)));
            stringBuild.append("}");
            
            profileWriteMutex.acquire();

            try
            {
                out.write(stringBuild.toString().getBytes());
                out.flush();
                startWithComma = true;
            }
            catch (Exception e)
            {
                System.out.println("[SaltLib] Failed to write to the profiling output stream!");
                e.printStackTrace();
            }
        }
        catch (InterruptedException e)
        {
            System.out.println("[SaltLib] Failed to mutex!");
            e.printStackTrace();
        }
        finally
        {
            profileWriteMutex.release();
        }
    }
}
