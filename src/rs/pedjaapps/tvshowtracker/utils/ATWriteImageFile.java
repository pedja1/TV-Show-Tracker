package rs.pedjaapps.tvshowtracker.utils;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import rs.pedjaapps.tvshowtracker.utils.Constants;

public class ATWriteImageFile extends AsyncTask<String, Void, Boolean>
{
    private Bitmap bmp;
    private File file;

    public ATWriteImageFile(Bitmap bmp, File file)
    {
        this.bmp = bmp;
        this.file = file;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params)
    {
        return writeFile(bmp, file);
    }

    @Override
    protected void onPostExecute(Boolean result)
    {
        super.onPostExecute(result);
    }

    public static boolean writeFile(Bitmap bmp, File file)
    {
        FileOutputStream out = null;
        boolean result = true;

        try
        {
            if (bmp != null)
            {
                out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            }
        }
        catch (Exception e)
        {
            Log.e(Constants.LOG_TAG, "ATWriteImageFile :: error: " + e.getMessage());
            result = false;
        }
        finally
        {
            try
            {
                if (out != null)
                {
                    out.close();
                }
            }
            catch (Exception e)
            {
                Log.e(Constants.LOG_TAG, "ATWriteImageFile :: error closing: " + e.getMessage());
                result = false;
            }
        }

        return result;
    }
}
