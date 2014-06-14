package rs.pedjaapps.tvshowtracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.crashlytics.android.Crashlytics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rs.pedjaapps.tvshowtracker.utils.AsyncTask;
import rs.pedjaapps.tvshowtracker.utils.Constants;
import rs.pedjaapps.tvshowtracker.utils.Utility;

/**
 * Created by pedja on 3/14/14 10.11.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 */
public abstract class AbsUploadActivity extends Activity
{
    private static final int REQUEST_CODE_PICK_PHOTO = 9006;
    private static final int REQUEST_CODE_TAKE_PHOTO = 9007;
    private File takePhotoFile = null;

    protected final void startTakePhotoActivity()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        {
            try
            {
                takePhotoFile = createImageFile();
            }
            catch (IOException ex)
            {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (takePhotoFile != null)
            {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(takePhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    protected final void startPickPhotoActivity()
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_PICK_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK)
        {
            switch(requestCode)
            {
                case REQUEST_CODE_PICK_PHOTO:
                    handlePhoto(intent.getData());
                    break;
                case REQUEST_CODE_TAKE_PHOTO:
                    handlePhoto();
                    break;
            }
        }
    }

    /**
     * Handle photo returned from gallery/camera
     * <br>  * Checks if subclasses handled photo: {@link #onPhotoSelected(String)} and if not performs default action(upload to server)
     * <br>  * Checks if photo is to big(more than 1024x1024) and resize it*/
    private void handlePhoto(Uri photoUri)
    {
        String selectedImage = Utility.getActualPathFromUri(photoUri, this);
        new GetBitmapTask().execute(selectedImage);
    }

    /**
     * Handle photo returned from gallery/camera
     * <br>  * Checks if subclasses handled photo: {@link #onPhotoSelected(String)} and if not performs default action(upload to server)
     * <br>  * Checks if photo is to big(more than 1024x1024) and resize it*/
    private void handlePhoto()
    {
        new GetBitmapTask().execute(takePhotoFile.getAbsolutePath());
    }

    /**
     *  */
    protected abstract void onPhotoSelected(String imagePath);

    private final class GetBitmapTask extends AsyncTask<String, Void, String>
    {
        String photoPath;
        ProgressDialog pd;
        @Override
        protected void onPreExecute()
        {
            pd = new ProgressDialog(AbsUploadActivity.this);
            pd.setMessage(getString(R.string.processing_image));
            pd.show();
        }

        /**Check if image is too big(>1024x1024) and resize
         * @return if image has been resized return new path else returns original path*/
        @Override
        protected String doInBackground(String... params)
        {
            photoPath = params[0];
            Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            if(bitmap != null)
            {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                int maxSize = 400;
                //if any dimension is larger than maxSize, do resize
                if (width > maxSize || height > maxSize)
                {
                    //check which dimension is larger, if they are equal any will do
                    boolean scaleByWidth = width > height;
                    float aspectRatio = scaleByWidth ? (float)width/(float)height : (float)height/(float)width;

                    //scale bitmap keeping aspect ratio
                    int diff = (scaleByWidth ? width : height) - maxSize;
                    width = scaleByWidth ? maxSize : (int) (width - diff / aspectRatio);
                    height = scaleByWidth ? (int) (height - diff / aspectRatio) : maxSize;
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);

                    //generate new path for image
                    File origPhotoFile = new File(photoPath);
                    File newPhotoFile = new File(getCacheDir(), origPhotoFile.getName());

                    //save image to cache directory
                    FileOutputStream fos = null;
                    try
                    {
                        //TODO [LOW] maybe check first if image already exists
                        fos = new FileOutputStream(newPhotoFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                        return newPhotoFile.getAbsolutePath();
                    }
                    catch (FileNotFoundException e)
                    {
                        if(BuildConfig.DEBUG)Log.e(Constants.LOG_TAG, "Failed to save image: " + e.getMessage());
                        Crashlytics.logException(e);
                        if(BuildConfig.DEBUG)e.printStackTrace();
                    }
                    finally
                    {
                        try
                        {
                            fos.close();
                        }
                        catch(Throwable ignore)
                        {}
                    }
                }
            }
            return photoPath;
        }

        @Override
        protected void onPostExecute(String imagePath)//new image path if resized or original if not or if resize failed
        {
            if(pd != null)pd.dismiss();
            onPhotoSelected(photoPath);
        }
    }

    private File createImageFile() throws IOException
    {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }

}
