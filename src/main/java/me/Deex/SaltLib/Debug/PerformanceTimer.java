package me.Deex.SaltLib.Debug;

import me.Deex.SaltLib.SaltLibMod;

public class PerformanceTimer
{
    private String name;
    private long start;

    private PerformanceTimer(String name)
    {
        this.name = name;
        start = System.nanoTime();
    }

    private void StopAndWrite()
    {
        Instrumentor.WriteProfile(name, start / 1000, System.nanoTime() / 1000, Thread.currentThread().getId());
    }

    public static PerformanceTimer Start(String name)
    {
        if (SaltLibMod.enableProfiling)
        {
            return new PerformanceTimer(name);
        }

        return null;
    }

    public static void StopAndWrite(PerformanceTimer timer)
    {
        if (timer != null)
        {
            timer.StopAndWrite();
        }
    }
}