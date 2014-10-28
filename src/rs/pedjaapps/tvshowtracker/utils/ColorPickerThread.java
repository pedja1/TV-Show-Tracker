/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package rs.pedjaapps.tvshowtracker.utils;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Process;

import java.util.concurrent.BlockingQueue;

public class ColorPickerThread extends Thread
{
    /**
     * The queue of requests to service.
     */
    private final BlockingQueue<ColorPickerHelper.Holder> mQueue;
    /**
     * Used for telling us to die.
     */
    private volatile boolean mQuit = false;

    /**
     * Creates a new cache thread.  You must call {@link #start()}
     * in order to begin processing.
     *
     * @param queue    Queue of incoming requests for triage
     */
    public ColorPickerThread(BlockingQueue<ColorPickerHelper.Holder> queue)
    {
        mQueue = queue;
    }

    /**
     * Forces this thread to quit immediately.  If any requests are still in
     * the queue, they are not guaranteed to be processed.
     */
    public void quit()
    {
        mQuit = true;
        interrupt();
    }

    @Override
    public void run()
    {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        ColorPickerHelper.Holder holder;
        while (true)
        {
            try
            {
                // Take a request from the queue.
                holder = mQueue.take();
                ColorPickerHelper.getInstance().startProcessing(holder.show.getTvdb_id());
            }
            catch (InterruptedException e)
            {
                // We may have been interrupted because it was time to quit.
                if (mQuit)
                {
                    return;
                }
                continue;
            }

            MyTimer timer = new MyTimer();

            Bitmap bitmap = holder.bitmap;
            if(bitmap != null)
            {
                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 1, 1, true);
                int tmp = scaled.getPixel(0, 0);
                final int color = Color.argb(100, Color.red(tmp), Color.green(tmp), Color.blue(tmp));
                final ColorPickerHelper.Holder finalHolder = holder;
                ColorPickerHelper.getInstance().uiHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        finalHolder.view.setBackgroundColor(color);
                        finalHolder.view.setTag(finalHolder.show.getTvdb_id());
                        finalHolder.show.setPosterMainColor(color);
                    }
                });
            }
            ColorPickerHelper.getInstance().finishProcessing(holder.show.getTvdb_id());

            timer.log("ColorPickerThread: picked color in");
        }
    }
}
