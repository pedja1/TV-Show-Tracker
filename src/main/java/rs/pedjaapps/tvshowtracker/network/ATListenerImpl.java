package rs.pedjaapps.tvshowtracker.network;

/**
 * Created by pedja on 2/19/14.
 *
 * This class is wrapper for {@link ATListener} allowing you to implement only needed methods
 * @author Predrag Čokulov
 */
public class ATListenerImpl<Params, Progress, Result> implements ATListener<Params, Progress, Result>
{
    @Override
    public Result doInBackground(int requestCode, Params... params)
    {
        return null;
    }

    @Override
    public void onPostExecute(int requestCode, Result result)
    {

    }

    @Override
    public void onPreExecute(int requestCode)
    {

    }

    @Override
    public void onProgressUpdate(int requestCode, Progress... values)
    {

    }

    @Override
    public void onCancelled(int requestCode, Result result)
    {

    }
}
