package rs.pedjaapps.tvshowtracker.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Executor
{
    ExecutorService executorService;
	private static Executor executor = null;
    private static List<Future<Runnable>> tasks;

    public Executor()
    {
        executorService = Executors.newCachedThreadPool();
        tasks = new ArrayList<Future<Runnable>>();
    }

	public static Executor getInstance()
	{
		if(executor == null)
		{
			executor = new Executor();
		}
        tasks.clear();//Im not sure about this
		return executor;
	}

    /**
     * Adds task to the que
     * @param runnable - runnable to be executed*/
    public void addTask(Runnable runnable)
    {
        Future f = executorService.submit(runnable);
        tasks.add(f);
    }

    /**
     * Executes all tasks currently in que and waits for all of them to finish before returning
     * */
    public void executeAll()
    {
        for (Future<Runnable> f : tasks)
        {
            try
            {
                f.get();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
    }

    
    public void shutdown()
    {
        if (executorService != null)
        {
            executorService.shutdownNow();
        }
    }
}
