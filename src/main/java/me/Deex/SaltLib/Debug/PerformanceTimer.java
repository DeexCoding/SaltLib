package me.Deex.SaltLib.Debug;

public class PerformanceTimer
{
    private String name;
    private long start;

    public PerformanceTimer(String name)
    {
        this.name = name;
        start = System.nanoTime();
    }

    public void Stop()
    {
        Instrumentor.WriteProfile(name, start / 1000, System.nanoTime() / 1000, Thread.currentThread().getId());
    }
}