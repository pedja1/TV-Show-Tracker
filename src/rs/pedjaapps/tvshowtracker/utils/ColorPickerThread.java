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
import android.os.Process;

import com.wms.wamoos.MainApp;
import com.wms.wamoos.model.AppUser;
import com.wms.wamoos.model.AppUserBase;
import com.wms.wamoos.model.PostParams;
import com.wms.wamoos.model.UserCacheCollection;

import java.util.concurrent.BlockingQueue;

public class ColorPickerThread extends Thread
{
    /**
     * The queue of requests to service.
     */
    private final BlockingQueue<Bitmap> mQueue;
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
    public ColorPickerThread(BlockingQueue<Bitmap> queue)
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
        int userId;
        while (true)
        {
            try
            {
                // Take a request from the queue.
                userId = mQueue.take();
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
            UserCacheCollection.getInstance().processingUser(userId);
            PostParams pp = new PostParams();
            // sending my mail, id of the user i want data for, my id
            pp.setParamsForGetUserData(
                    AppUser.getInstance().getEmail(),
                    AppUser.getInstance().getMemberId(),
                    userId,
                    DisplayHelper.Screen.width,
                    DisplayHelper.Screen.height);
            String result = Internet.getInstance().httpPost(AppData.URL_NEARBY_USER_DATA + MainApp.getInstance().getLanguage(), pp);
            AppUserBase user = AppUserBase.getFromJSONString(result);
            if(user != null)
            {
                UserCacheCollection.getInstance().addToCache(userId, user);
            }
            if(UserCacheCollection.getInstance().lock != null)synchronized (UserCacheCollection.getInstance().lock)
            {
                try
                {
                    if(UserCacheCollection.getInstance().lock == userId)
                        UserCacheCollection.getInstance().lock.notify();
                }
                catch (Exception e)
                {
                    if(AppData.DEBUG_MODE)e.printStackTrace();
                }
            }
            UserCacheCollection.getInstance().finishedProcessingUser(userId);
            timer.log("UserCacheThread: downloaded user in:");
        }
    }
}
