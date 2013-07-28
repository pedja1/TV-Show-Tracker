package rs.pedjaapps.tvshowtracker.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.util.Log;


import rs.pedjaapps.tvshowtracker.TVShowTracker;
import rs.pedjaapps.tvshowtracker.utils.Constants;

public class FileSystem
{
    private static final String LOG_TAG = Constants.LOG_TAG;

    public static boolean isExternalStorageWritable()
    {
        boolean mExternalStorageWriteable;

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state))
        {
            // We can read and write the media
            mExternalStorageWriteable = true;
        }
        else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state))
        {
            // We can only read the media
            mExternalStorageWriteable = false;
        }
        else
        {
            // Something else is wrong. It may be one of many other states, but all we need
            // to know is we can neither read nor write
            mExternalStorageWriteable = false;
        }

        return mExternalStorageWriteable;
    }

    public static long getMegabytesAvailable(File file)
    {
        StatFs stat = new StatFs(file.getPath());
        long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
        return (long) (bytesAvailable / (1024.f * 1024.f));
    }

    public static void deleteRecursive(File fileOrDirectory)
    {
        if (fileOrDirectory.isDirectory())
        {
            for (File child : fileOrDirectory.listFiles())
            {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
    }

    public static void deleteFile(String fileName)
    {
        Log.d(LOG_TAG, "deleting: " + fileName);
        File myFile = new File(fileName);
        if (myFile != null)
        {
            myFile.delete();
            Log.d(LOG_TAG, "deleted");
        }
    }

    public static void deleteExternalFile(String fileName)
    {
        File myFile = new File(Environment.getExternalStorageDirectory().getPath(), fileName);
        if (myFile != null)
        {
            myFile.delete();
        }
    }

    public static boolean hasFile(String fileName)
    {
        File myFile = new File(fileName);
        if (myFile != null)
        {
            return myFile.exists();
        }
        return false;
    }

    public static boolean hasExternalFile(String fileName)
    {
        File myFile = new File(Environment.getExternalStorageDirectory().getPath(), fileName);
        if (myFile != null)
        {
            return myFile.exists();
        }
        return false;
    }

    public static boolean createDir(File dir)
    {
        if (dir.exists())
        {
            return true;
        }

        Log.d(LOG_TAG, "Creating dir [" + dir.getName() + "]");

        if (!dir.mkdirs())
        {
            Log.e(Constants.LOG_TAG, "Failed to create dir " + dir.getName());
            return false;
        }

        return true;
    }

    public static boolean createRootDir(String dirName)
    {
        File dir = new File(Environment.getExternalStorageDirectory().getPath(), dirName);

        return createDir(dir);
    }

    public static String readTextFile(String path, String fileName)
    {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        String result = "";
        StringBuilder sb;
        String line;
        InputStreamReader inputStreamReader = null;

        // reading private file
        if (path == null || path.equals(""))
        {
            try
            {
                Log.v(LOG_TAG, "FileSystem.readTextFile: reading private file: " + fileName);
                fileInputStream = TVShowTracker.getAppContext().openFileInput(fileName);
                inputStreamReader = new InputStreamReader(fileInputStream, Constants.ENCODING);
                bufferedReader = new BufferedReader(inputStreamReader);
            }
            catch (Exception ex)
            {
            }
        }
        else
        // reading external file
        {
            try
            {
                String filePath = path + File.separator + fileName;
                Log.v(LOG_TAG, "FileSystem.readTextFile: reading external file: " + filePath);
                fileInputStream = new FileInputStream(filePath);
                inputStreamReader = new InputStreamReader(fileInputStream, Constants.ENCODING);
                bufferedReader = new BufferedReader(inputStreamReader, fileInputStream.available());
            }
            catch (Exception ex)
            {
            }
        }

        try
        {
            sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null)
            {
                sb.append(line.trim());
            }

            result = sb.toString();
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "FileSystem.readTextFile: " + e.getMessage());
        }
        finally
        {
            try
            {
                bufferedReader.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                fileInputStream.close();
                inputStreamReader.close();
            }
            catch (Exception e)
            {
            }
        }

        return result;
    }

    /*public static String readFile(Activity activity, String fileName)
    {
        ByteArrayOutputStream outputStream = null;
        String result = null;

        try
        {
            // Input
            FileInputStream fis = activity.openFileInput(fileName);
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);

            // Output
            outputStream = new ByteArrayOutputStream();
            outputStream.write(buffer);

            // result = new String(outputStream.toByteArray(), "UTF-8");
            // result = outputStream.toString();
            result = outputStream.toString(Constants.ENCODING);
            outputStream.close();
            fis.close();
        }
        catch (Exception e)
        {
            Log.e(LOG_TAG, "FileSystem.readFilePrivate: " + e.getMessage());
        }

        Log.d(LOG_TAG, ">>> LIST: ");
        Log.v(LOG_TAG, result);
        return result;
    }*/
    public static String readFile(String path, String fileName)
    {
        File myFile = new File(path, fileName);
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        String result = null;

        try
        {
            // Input
            inputStream = new BufferedInputStream(new FileInputStream(myFile));
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);

            // Output
            outputStream = new ByteArrayOutputStream();
            outputStream.write(buffer);

            result = outputStream.toString();
            inputStream.close();
            outputStream.close();
        }
        catch (Exception e)
        {
            Log.e(Constants.LOG_TAG, "FileSystem.readFile: " + e.getMessage());
        }

        return result;
    }

    public static void WriteFile(File file, String content)
    {
        if (content != null && file != null)
        {
            try
            {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                byte[] buffer = content.getBytes(Constants.ENCODING);
                bos.write(buffer);
                bos.close();
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "FileSystem.WriteFile: " + e.getMessage());
            }
        }
        else
        {
            Log.e(Constants.LOG_TAG, "FileSystem.WriteFile: some data was null");
        }
    }
// ********************************************************************************
// New
// ********************************************************************************

    public static void WriteInternalTextFile(File file, String content)
    {
    }

    public static void ReadInternalTextFile(File file, String content)
    {
    }

    public static String GetMimeType(Context context, Uri uriImage)
    {
        String strMimeType = null;

        Cursor cursor = context.getContentResolver().query(uriImage, new String[]
                {
                    MediaStore.MediaColumns.MIME_TYPE
                },
                                                           null, null, null);

        if (cursor != null && cursor.moveToNext())
        {
            strMimeType = cursor.getString(0);
        }

        return strMimeType;
    }
}
