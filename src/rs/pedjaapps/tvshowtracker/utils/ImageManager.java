package rs.pedjaapps.tvshowtracker.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import rs.pedjaapps.tvshowtracker.R;
import rs.pedjaapps.tvshowtracker.TVShowTracker;

// ********************************************************************************
// ImageManager v5.05
// ********************************************************************************
public class ImageManager
{
    private static ImageManager imageManager = null;
    private HashMap<String, SoftReference<Bitmap>> webImageMap = new HashMap<String, SoftReference<Bitmap>>();
    public File cacheDirImages = null;
    private ImageQueue imageQueue;
    private boolean cacheImages = true;
    private boolean inDither = true;
    private final int bgColor = Color.WHITE;

    private ImageManager()
    {
        Log.e(Constants.LOG_TAG, ">>> ImageManager Constructor");

        imageQueue = new ImageQueue();

        // Find the dir to save cached images
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            cacheDirImages = new File(Environment.getExternalStorageDirectory(),
                    Constants.EXTERNAL_FOLDER_ROOT + File.separator + Constants.CACHE_FOLDER);
        } else
        {
            cacheDirImages = TVShowTracker.getAppContext().getFilesDir();
        }
        Log.v(Constants.LOG_TAG, "cacheDir: [" + cacheDirImages + "]");

        if (!cacheDirImages.exists())
        {
            cacheDirImages.mkdirs();
        }
    }

    public static synchronized ImageManager getInstance()
    {
        if (imageManager == null)
        {
            imageManager = new ImageManager();
        }
        return imageManager;
    }

    /**
     * Main method
     */
    public void getWebImage(ImageReference imageReference)
    {
        // Log.v(Constants.LOG_TAG, "getWebImage: " + imageReference.getImageNameHumanReadable());
        Boolean imageFound = false;

        // test if url is valid
        String[] actualUrlArray = (imageReference.getImageName() + "_string_end").split(Constants.URL_ROOT);
        String actualUrl = actualUrlArray[actualUrlArray.length - 1];
        actualUrl = actualUrl.replaceAll("/", "_");
        actualUrl = actualUrl.replaceAll("_string_end", "");
        if (actualUrl.equalsIgnoreCase(""))
        {
            imageReference.setNoImageId(R.drawable.noimage);
            setImageBitmap(imageReference, imageReference.getNoImageId());
            imageFound = true;
        }

        // If image has no nameimageReference.getImageURI()
        String dontLoadImageWithThisName = "no_image";
        if (imageReference.getImageURI().contains(dontLoadImageWithThisName))
        {
            // Log.v(Constants.LOG_TAG, "getWebImage :: dontLoadImageWithThisName");
            setImageBitmap(imageReference, imageReference.getNoImageId());
            imageFound = true;
        }
        // If we have an image in the map, give it to the ImageView
        else if (webImageMap.containsKey(imageReference.getImageName()))
        {
            Bitmap bitmap = webImageMap.get(imageReference.getImageName()).get();
            if (bitmap != null)
            {
                if (!bitmap.isRecycled())
                {
                    // Log.v(Constants.LOG_TAG, "getWebImage :: getting image from memory: [" + imageReference.getImageNameHumanReadable() + "]");
                    setImageBitmap(imageReference, bitmap);
                    imageFound = true;

                    if (imageReference.getHasBackground())
                    {
                        LinearLayout ll = (LinearLayout) imageReference.getImageView().getParent();
                        ll.setBackgroundColor(bgColor);
                    }
                } else
                {
                    Log.e(Constants.LOG_TAG, "getWebImage: bitmap was recycled: [" + imageReference.getImageNameHumanReadable() + "]");
                }
            } else
            {
                Log.w(Constants.LOG_TAG, "getWebImage: found in map with no pic: [" + imageReference.getImageNameHumanReadable() + "]");
            }
        }

        // else, que it
        if (!imageFound)
        {
            // We queue the image (first checking if its not in the queue)
            imageQueue.addToQue(imageReference);

            // we set a "downloading" image
            imageReference.getImageView().setImageResource(imageReference.loadingImageId);

            // animation
    /*if (imageReference.isAniLoading())
    {
    try
    {
    imageReference.getImageView().setBackgroundResource(R.anim.loadinganim);
    MyAnimationRoutine mar = new MyAnimationRoutine(imageReference.getImageView());
    Timer t = new Timer(false);
    t.schedule(mar, 100);
    imageReference.getImageView().setImageBitmap(null);
    }
    catch (Exception e)
    {
    imageReference.getImageView().setImageResource(imageReference.loadingImageId);
    }
    }
    else
    {
    imageReference.getImageView().setImageResource(imageReference.loadingImageId);
    }*/
        }
    }

    private void displayImage(ImageReference imageToLoad, Bitmap bitmap)
    {
        BitmapDisplayer bmpDisplayer = new BitmapDisplayer(bitmap, imageToLoad);
        Activity activity = (Activity) imageToLoad.getImageView().getContext();
        activity.runOnUiThread(bmpDisplayer);
    }

    private void setImageBitmap(ImageReference imageReference, Bitmap bitmap)
    {
        imageDimensions(bitmap.getWidth(), bitmap.getHeight(), imageReference);

        if (imageReference.scaleToHeightPercent == 0 && imageReference.scaleToHeight != 0 && imageReference.scaleToHeight != bitmap.getHeight())
        {
            Bitmap tempBitmap = Bitmap.createScaledBitmap(bitmap, imageReference.scaleToWidth, imageReference.scaleToHeight, true);
            imageReference.getImageView().setImageBitmap(tempBitmap);
        } else
        {
            imageReference.getImageView().setImageBitmap(bitmap);

        }
    /*int imageWidth = imageReference.getImageView().getWidth();
    int imageHeight = (int) ( imageWidth/Constants.ASPECT_RATIO );
    //if(imageWidth != 0)
    imageReference.getImageView().getLayoutParams().height = imageHeight;
    System.out.println(imageWidth+"x"+imageHeight);*/
    }

    // **************************************************
    // Getter & Setter
    // **************************************************
    public boolean isInDither()
    {
        return inDither;
    }

    public void setInDither(boolean inDither)
    {
        this.inDither = inDither;
    }

    public boolean isCacheImages()
    {
        return cacheImages;
    }

    public void setCacheImages(boolean cacheImages)
    {
        this.cacheImages = cacheImages;
    }

    // **************************************************
    // Private
    // **************************************************
    private int computeSampleSize(InputStream stream, ImageReference imageReference)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inSampleSize = 10;
        BitmapFactory.decodeStream(stream, null, options);
        double sourceWidth = options.outWidth;
        double sourceHeight = options.outHeight;
        Log.v(Constants.LOG_TAG, "image original w|h: " + sourceWidth + "|" + sourceHeight);
        if (sourceWidth < 1 || sourceHeight < 1)
        {
            Log.e(Constants.LOG_TAG, "invalid image dimensions: " + imageReference.getImageNameHumanReadable());
        }

        // either of maxW and maxH can be 0
        // so we calculate the other param by proportion
        imageDimensions((int) sourceWidth, (int) sourceHeight, imageReference);

        int sampleSize = (int) Math.floor(Math.max(sourceWidth / imageReference.scaleToWidth, sourceHeight / imageReference.scaleToHeight));
        Log.d(Constants.LOG_TAG, "ImageManager imageSize = " + sampleSize);
        return sampleSize;
    }

    private void imageDimensions(int sourceWidth, int sourceHeight, ImageReference imageReference)
    {
        WindowManager wm = (WindowManager) TVShowTracker.getAppContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth(); // deprecated
        int height = display.getHeight();
        if (sourceWidth <= 0)
        {
            sourceWidth = 1;
        }
        if (sourceHeight <= 0)
        {
            sourceHeight = 1;
        }
        boolean byWidth = imageReference.scaleToWidth > 0 || imageReference.scaleToWidthPercent > 0;
        boolean byHeight = imageReference.scaleToHeight > 0 || imageReference.scaleToHeightPercent > 0;

        // scale image to the percent of the screen
        if (imageReference.scaleToWidthPercent > 0)
        {
            imageReference.scaleToWidth = width * imageReference.scaleToWidthPercent / 100;
        }
        if (imageReference.scaleToHeightPercent > 0)
        {
            imageReference.scaleToHeight = height * imageReference.scaleToHeightPercent / 100;
        }

        // keep aspect ratio
        if (!(byWidth && byHeight))
        {
            if (byWidth)
            {
                imageReference.scaleToHeight = imageReference.scaleToWidth * sourceHeight / sourceWidth;

            }
            if (byHeight)
            {
                imageReference.scaleToWidth = imageReference.scaleToHeight * sourceWidth / sourceHeight;
            }
        }
    /*if(imageReference.isImageWide) {
    imageReference.scaleToWidth = sourceWidth;
    imageReference.scaleToHeight = (int) ( sourceWidth/Constants.ASPECT_RATIO );
    }*/
        Log.v(Constants.LOG_TAG, "rescaled w|h: " + imageReference.scaleToWidth + "|" + imageReference.scaleToHeight);
    }


    // TODO :: ovo se poziva previse puta
    private Bitmap getBitmap(ImageReference imageReference)
    {
        String myFilename = urlIntoFileName(imageReference.getImageName());
        File myFile = new File(cacheDirImages, myFilename);

        Log.e(Constants.LOG_TAG, "ImageManager :: getBitmap(): " + imageReference.getImageNameHumanReadable());

        // TODO :: moram i ovde da radim sample, bez obzira sto nije sa weba
        Bitmap bitmap = BitmapFactory.decodeFile(myFile.getPath());

        // Is the bitmap in our cache?
        if (bitmap != null)
        {
            Log.i(Constants.LOG_TAG, "ImageManager :: got image from cache: [" + imageReference.getImageNameHumanReadable() + "]");
            return bitmap;
        }
        // Nope, have to download it
        else
        {
            Log.d(Constants.LOG_TAG, "ImageManager :: downloading: [" + imageReference.getImageNameHumanReadable() + "]");
            try
            {
                // trying to make GC clean up
                // *****
                if (null != bitmap && !bitmap.isRecycled())
                {
                    bitmap.recycle();
                }
                bitmap = null;
                if (Build.VERSION.SDK_INT < 12)
                {
                    System.gc();
                }
                // *****

                URL url = new URL(imageReference.getImageURI());
                // TODO :: emergency :: ne treba svaki put da otvara konekciju
                URLConnection urlConnection = url.openConnection();
                urlConnection.setConnectTimeout(Constants.CONN_TIMEOUT);
                urlConnection.setReadTimeout(Constants.CONN_TIMEOUT);
                // We don't want it scaled
                if (!imageReference.doScale())
                {
                    bitmap = BitmapFactory.decodeStream(urlConnection.getInputStream());
                } else
                {
                    // Get Image width and height
                    // InputStream inputStream = new URL(imageReference.getImageURI()).openStream();
                    InputStream inputStream = urlConnection.getInputStream();
                    int scale = computeSampleSize(inputStream, imageReference);
                    inputStream.close();

                    // DecodeStream
                    inputStream = new URL(imageReference.getImageURI()).openStream();
                    BitmapFactory.Options newBmpOptions = new BitmapFactory.Options();
                    //newBmpOptions.inPreferredConfig = Bitmap.Config.RGB_565;
                    //newBmpOptions.inSampleSize = 20;
                    if (scale > 0)
                    {
                        newBmpOptions.inSampleSize = scale;
                        newBmpOptions.inDither = isInDither();
                    }
                    newBmpOptions.inScaled = false;

                    if (Build.VERSION.SDK_INT < 12)
                    {
                        System.gc();
                    }

                    Bitmap tempBitmap = BitmapFactory.decodeStream(inputStream, null, newBmpOptions);
                    bitmap = Bitmap.createScaledBitmap(tempBitmap, imageReference.scaleToWidth, imageReference.scaleToHeight, true);

                    if (null != bitmap && !bitmap.isRecycled())
                    {
                        // tempBitmap.recycle();
                    }

                    inputStream.close();
                }

                if (cacheImages && !myFile.exists())
                {
                    Log.d(Constants.LOG_TAG, "ImageManager writing cache: [" + myFile + "]");
                    ATWriteImageFile aTWriteImageFile = new ATWriteImageFile(bitmap, myFile);
                    aTWriteImageFile.execute();
                }

                return bitmap;
            } catch (Exception ex)
            {
                Log.e(Constants.LOG_TAG, "ImageManager :: getBitmap :: error: " + ex.getMessage());

                return imageReference.noImage;
            }
        }
    }

    public static String urlIntoFileName(String url)
    {
        String result = "";

        String[] ext = url.split("\\.");

        String extension = ext[ext.length - 1];
        if (extension.length() != 3 && extension.length() != 4)
        {
            Log.e(Constants.LOG_TAG, "unexpected extension: " + extension);
        }
        // extension = extension.substring(0, 3);
        result = url.trim().toLowerCase();

        result = result.replaceAll("http", "");
        result = result.replaceAll(":", "");
        result = result.replaceAll("\\+", "_");
        result = result.replaceAll("/", "_");
        result = result.replaceAll("=", "_");
        result = result.replaceAll("-", "_");
        result = result.replaceAll("\\.", "_");
        result = result.replaceAll(" ", "_");
        result = result.replaceAll("\\?", "_");
        result = result.replaceAll("&amp\\;", "_");

        result = result.replaceAll(extension, "");

        while (result.contains("__"))
        {
            result = result.replaceAll("__", "_");
        }
        if (result.endsWith("_"))
        {
            result = result.substring(0, result.length() - 1);
        }
        if (result.startsWith("_"))
        {
            result = result.substring(1, result.length());
        }

        result = result.trim() + "." + extension;
        result = result.trim().toLowerCase();

        return result;
    }

    public void clearCache()
    {
        // Clear memory cache
        webImageMap.clear();

        // Clear SD cache
        FileSystem.deleteRecursive(cacheDirImages);
        // File[] files = cacheDir.listFiles();
        // for (File f : files)
        // {
        // f.delete();
        // }
    }

    // **************************************************
    // Classes
    // **************************************************
    public static class ImageReference
    {
        private String imageURI;
        private ImageView imageView;
        public int scaleToWidth = 0;
        public int scaleToHeight = 0;
        public int scaleToWidthPercent = 0;
        public int scaleToHeightPercent = 0;
        private int loadingImageIdDefault = R.drawable.noimage;
        private int loadingImageIdAlt = R.drawable.noimage;
        private int loadingImageId = loadingImageIdDefault;
        private Bitmap noImage;
        private boolean hasBackground = false;
        private boolean aniLoading = false;
        private boolean isImageWide = false;


        private String[] extensions =
                {
                        ".jpg", ".jpeg", ".gif", ".png"
                };
        public boolean loading;

        /**
         * @param imageURI - web address of the image
         * @param imageView - imageView that will hold the image
         */
        public ImageReference(String imageURI, ImageView imageView)
        {
            this.imageURI = imageURI.trim();
            this.imageView = imageView;
            this.loading = false;
        }

        public ImageView getImageView()
        {
            return imageView;
        }

        /**
         * Get web address for this image
         *
         * @return working url
         */
        public String getImageURI()
        {
            return this.imageURI;
        }

        /**
         * Actual name of the image, we should reference by this
         *
         * @return image name
         */
        public String getImageName()
        {
            String widthTag = "" + this.scaleToWidth;
            if (doScaleInPercent())
            {
                if (this.scaleToWidthPercent > 0)
                {
                    widthTag = "" + this.scaleToWidthPercent;
                } else if (this.scaleToHeightPercent > 0)
                {
                    widthTag = "" + this.scaleToHeightPercent;
                }
            } else
            {
                if (this.scaleToWidth > 0)
                {
                    widthTag = "" + this.scaleToWidth;
                } else if (this.scaleToHeight > 0)
                {
                    widthTag = "" + this.scaleToHeight;
                }
            }
            return "" + this.doScale() + widthTag + this.doScaleInPercent() + this.imageURI;
        }

        public String getImageNameHumanReadable()
        {
            String result = getImageName();

            for (int i = 0; i < extensions.length; i++)
            {
                int positionEnd = result.indexOf(extensions[i]);
                if (positionEnd >= 0)
                {
                    result = result.substring(0, positionEnd);
                    int positionStart = result.lastIndexOf("/");
                    result = result.substring(positionStart + 1, positionEnd) + extensions[i];
                    return result;
                }
            }

            return result;
        }

        public boolean doScale()
        {
            return (this.scaleToHeight > 0 || this.scaleToHeightPercent > 0 || this.scaleToWidth > 0 || this.scaleToWidthPercent > 0);
        }

        public boolean doScaleInPercent()
        {
            return (this.scaleToHeightPercent > 0 || this.scaleToWidthPercent > 0);
        }

        public void setLoadingImageId(int loadingImageId)
        {
            this.loadingImageId = loadingImageId;
        }

        public void setLoadingImageAlt()
        {
            this.loadingImageId = loadingImageIdAlt;
        }

        public boolean getHasBackground()
        {
            return hasBackground;
        }

        public void setHasBackground(boolean hasBackground)
        {
            this.hasBackground = hasBackground;
        }

        public boolean isAniLoading()
        {
            return aniLoading;
        }

        public void setAniLoading(boolean aniLoading)
        {
            this.aniLoading = aniLoading;
        }

        public Bitmap getNoImageId()
        {
            return noImage;
        }

        public boolean isImageWide()
        {
            return isImageWide;
        }

        public void setImageWide(boolean isImageWide)
        {
            this.isImageWide = isImageWide;
        }

        /**
         * Image to display when image is not found. Set to 0 if you don't want
         * an image to be shown
         *
         * @param noImageId id of the image to show
         */
        public void setNoImageId(int noImageId)
        {
            // TODO :: looks like a leak
            if (noImageId > 0)
            {
                this.noImage = BitmapFactory.decodeResource(TVShowTracker.getAppContext().getResources(), noImageId);
            } else
            {
                this.noImage = null;
            }
        }
    }

    // Stores list of images to download
    private class ImageQueue
    {
        private ArrayList<ImageReference> imagesInQueue;

        public ImageQueue()
        {
            imagesInQueue = new ArrayList<ImageReference>();
        }

        public void removeOldReferencesFromQueue(String imageName)
        {
            int i = 0;
            if (imagesInQueue != null && imageName != null)
            {
                while (imagesInQueue != null && i < imagesInQueue.size())
                {
                    if (imagesInQueue.get(i) != null && imagesInQueue.get(i).getImageName() != null)
                    {
                        if (imagesInQueue.get(i).getImageName().equalsIgnoreCase(imageName) && !imagesInQueue.get(i).loading)
                        {
                            imagesInQueue.remove(i);
                        } else
                        {
                            i++;
                        }
                    }
                }
            }
        }

        /**
         * @param imageReference
         * @return true if image was added to the queue and is not loading atm
         */
        public void addToQue(ImageReference imageReference)
        {
            removeOldReferencesFromQueue(imageReference.getImageName());

            imagesInQueue.add(imageReference);

            // Log.v(Constants.LOG_TAG, "ImageManager :: Queue image: [" + imageReference.getImageNameHumanReadable() + "]");
            LoadImage li = new LoadImage(imageReference);
            GeneralPurposeLooper.getInstance().post(li);
        }
    }

    private class LoadImage implements Runnable
    {
        private int threadId;
        private String threadSign = "";
        private ImageReference imageReference;

        public LoadImage(ImageReference imageReference)
        {
            this.imageReference = imageReference;
            this.threadId = (int) (Math.random() * 10000);
            threadSign = "[T:" + threadId + "] ";
        }

        public void run()
        {
            imageReference.loading = true;

            Bitmap bitmap = getBitmap(imageReference);

            webImageMap.put(imageReference.getImageName(), new SoftReference<Bitmap>(bitmap));
            Object tag = imageReference.getImageView().getTag();
            // if (tag != null)
            // {
            // Log.e(Constants.LOG_TAG, "Tag: " + tag);
            // }
            // else
            // {
            // Log.e(Constants.LOG_TAG, "Tag: " + "null");
            // }
            // Log.e(Constants.LOG_TAG, "Name: " + imageReference.getImageName());

            // Make sure we have the right view - thread safety defender
            if (tag != null && ((String) tag).equalsIgnoreCase(imageReference.getImageName()))
            {
                // Log.v(Constants.LOG_TAG, "displayImage");
                displayImage(imageReference, bitmap);
            }

            // remove page from queue
            imageReference.loading = false;
            imageQueue.imagesInQueue.remove(imageReference);

            // Log.v(Constants.LOG_TAG, threadSign + "Map size: " + webImageMap.size() + " | " + "Images in queue: " + imageQueue.imagesInQueue.size());
            // Log.v(Constants.LOG_TAG, threadSign + "Image " + imageReference.getImageNameHumanReadable() + " prepared in " + myTimer.get());
        }
    }

    // Used to display myBitmap in the UI thread
    private class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        ImageReference imageReference;

        public BitmapDisplayer(Bitmap bitmap, ImageReference imageReference)
        {
            this.bitmap = bitmap;
            this.imageReference = imageReference;
        }

        public void run()
        {
            if (bitmap != null)
            {
                setImageBitmap(imageReference, bitmap);
                imageReference.getImageView().setVisibility(View.VISIBLE);

            } else
            {
                // imageReference.getImageView().setImageResource(R.drawable.ic_no_image);
                if (imageReference.getImageURI().equals(""))
                {
                    // imageReference.getImageView().setVisibility(View.GONE);
                }
            }

            if (imageReference.getHasBackground())
            {
                LinearLayout ll = (LinearLayout) imageReference.getImageView().getParent();
                ll.setBackgroundColor(bgColor);
            }
        }
    }

}
