package rs.pedjaapps.tvshowtracker.network;

import android.app.*;
import android.util.*;
import java.util.*;
import rs.pedjaapps.tvshowtracker.*;

/**
 * Created by pedja on 2/21/14 10.17.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 *
 *
 *
 * @author Predrag Čokulov
 */
public class NetAction implements ATListener<String, Void, JSONUtility>
{
    public static final int REQUEST_CODE_SOMETHING = 9001;
    private AbsActivity activity;

    private ResponseHandler responseHandler;

    //this array has all tasks currently running
    private final SparseArray<ATNet<String, Void, JSONUtility>> runningTasks = new SparseArray<>();

    //this list holds all tasks that are waiting to be executed
    private final List<Task> taskQueue = new ArrayList<>();

    private ResponseHandlerImpl.ResponseMessagePolicy responseMessagePolicy = ResponseHandlerImpl.DEFAULT_RESPONSE_MESSAGE_POLICY;

    public NetAction(AbsActivity activity)
    {
        if(activity == null)
        {
            throw new IllegalArgumentException("You must pass non null RootActivity object");
        }
        this.activity = activity;
    }

    @Override
    public final JSONUtility doInBackground(int requestCode, String... params)
    {
        JSONUtility jsonUtility = null;
            switch (requestCode)
            {
                case REQUEST_CODE_SOMETHING:
                    /*RequestBuilder builder;
                    builder = new RequestBuilder(RequestBuilder.Method.POST);
                    builder.setCommand(RequestBuilder.CONTROLLER.members, RequestBuilder.ACTION.mobile_login);
                    //builder.setPostMethod(RequestBuilder.PostMethod.FORM_DATA);
                    builder.addParam(RequestBuilder.PARAM.email, params[0]);
                    builder.addParam(RequestBuilder.PARAM.password, params[1]);
                    builder.addParam(RequestBuilder.PARAM.client_id, H2DAccountAuthenticator.H2D_CLIENT_ID);
                    builder.addParam(RequestBuilder.PARAM.client_secret, H2DAccountAuthenticator.H2D_CLIENT_SECRET);
                    builder.addParam(RequestBuilder.PARAM.grant_type, H2DAccountAuthenticator.GRANT_TYPE_PASSWORD);
                    jsonUtility = new JSONUtility(Internet.getInstance().executeHttpRequest(builder));
                    jsonUtility.parseLoginResponse(params[1]);*/
                    break;

                /*case REQUEST_CODE_SAVE_PROFILE:
                    String path = params[0];
                    String mimeType = null;
                    if (!TextUtils.isEmpty(path))
                    {
                        try
                        {
                            Uri uri = Uri.parse(path);
                            InputStream is = activity.getContentResolver().openInputStream(uri);
                            mimeType = activity.getContentResolver().getType(uri);

                            Bitmap bmp = BitmapFactory.decodeStream(is);
                            path = AbsUploadActivity.scaleBitmap(bmp, path, activity.getCacheDir(), true);
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    builder = new RequestBuilder(RequestBuilder.Method.POST);
                    builder.setCommand(RequestBuilder.CONTROLLER.members, RequestBuilder.ACTION.mobile_edit_profile);
                    if(params[1] != null)builder.addParam(RequestBuilder.PARAM.gender, params[1]);
                    builder.addParam(RequestBuilder.PARAM.first_name, params[2]);
                    builder.addParam(RequestBuilder.PARAM.last_name, params[3]);
                    builder.addParam(RequestBuilder.PARAM.street, params[4]);
                    builder.setFilePath(path);
                    builder.setMimeType(mimeType);
                    builder.setFileParamName("file");
                    builder.setPostMethod(RequestBuilder.PostMethod.FORM_DATA);
                    Internet.Response response = Internet.getInstance().executeHttpRequest(activity, builder);
                    jsonUtility = new JSONUtility(response);
                    break;*/
            }
        return jsonUtility;
    }

    @Override
    public void onPostExecute(int requestCode, JSONUtility jsonUtility)
    {
        new ResponseHandlerImpl(jsonUtility, responseMessagePolicy)
        {
            @Override
            public void onResponse(int responseStatus, JSONUtility jsonUtility)
            {
                if(responseHandler != null)responseHandler.onResponse(responseStatus, jsonUtility);
            }
        };

        switch (requestCode)
        {
            case REQUEST_CODE_SOMETHING:
                activity.dismissProgressDialog();
                break;
        }

        runningTasks.remove(requestCode); // remove this task from running tasks list
        
        //We only added tasks to queue if it already exists in runningTasks
        //We check here if there is a task in taskQueue with same id as this one, remove it from queue and execute it
        Task task = Task.getTaskByCode(requestCode, taskQueue);
        if(task != null)
        {
            taskQueue.remove(task);
            execute(task.task, task.params);
        }

    }

    @Override
    public void onPreExecute(int requestCode)
    {
        ProgressDialog pd = activity.getProgressDialog(activity.getString(R.string.please_wait));
        switch (requestCode)
        {
            case REQUEST_CODE_SOMETHING:
                pd.setCancelable(false);
                pd.show();
                break;
        }
    }

    @Override
    public void onProgressUpdate(int requestCode, Void... values)
    {

    }

    @Override
    public void onCancelled(int requestCode, JSONUtility jsonUtility)
    {

    }

    /**
     * Move thread to another section(inbox, archive)
     *  <a href="https://dev-dating-m1.datingvip.com/api.json?cmd=inbox.moveto&inbox=inbox&thread[3205151]=-1&thread[320453]=-2">/api.json?cmd=inbox.moveto&inbox=inbox&thread[3205151]=-1&thread[320453]=-2</a>
     * */
    /*public final void deleteThread(MessageThread... threadIds)
    {
        aditionalPostParams = new ArrayList<>();
        for(MessageThread thread : threadIds)
        {
            aditionalPostParams.add(new BasicNameValuePair(String.format("thread[%s]", thread.getThreadId()), thread.getThreadStatus() + ""));
        }
        ATNet<String, Void, JSONUtility> task = new ATNet<>(this, REQUEST_CODE_DELETE_THREAD, activity);
        execute(task);
    }*/

    /**
     *list jobs
     * */
    public final void loadNextPage(int requestCode, int page, String... params)
    {
        ATNet<String, Void, JSONUtility> task = new ATNet<>(this, requestCode, activity);
        String[] newParams = new String[params.length + 1];
        newParams[0] = page + "";
        System.arraycopy(params, 0, newParams, 1, params.length);
        execute(task, newParams);
    }

    /**
     *get job details
     * */
    public final void getSomething(String id)
    {
        //ATNet<String, Void, JSONUtility> task = new ATNet<>(this, REQUEST_CODE_JOB_DETAILS, activity);
        //execute(task, id);
    }

    /**
     * Check if this task is executing at the moment
     * */
    private void execute(ATNet<String, Void, JSONUtility> task, String... params)
    {
        if (runningTasks.get(task.getRequestCode()) != null)//task exists
        {
            taskQueue.add(new Task(task, params)); // add task to queue, don't execute it
        }
        else
        {
            runningTasks.put(task.getRequestCode(), task);//add this task to list of running tasks
            task.execute(params);//execute task
        }
    }

    public final void setResponseHandler(ResponseHandlerImpl responseHandler)
    {
        this.responseHandler = responseHandler;
    }

    /**
     * Cancel task with the given id(requestCode)
     * @param cancelOtherQueuedTasksWithSameId whether to also remove all task with same requestOCde from queue
     * @param requestCode requestCode of the task
     * */
    public void cancel(int requestCode, boolean cancelOtherQueuedTasksWithSameId)
    {
        if (runningTasks.get(requestCode) != null)
        {
            runningTasks.get(requestCode).cancel(true);
            runningTasks.remove(requestCode);
            if(cancelOtherQueuedTasksWithSameId)//this will also cancel all other not yet executed tasks with same id
            {
                Task.removeTasksFromList(taskQueue, requestCode);
            }
        }
    }

    /**
     * Class that holds tasks ({@link ATNet}) and its execute params
     * */
    private static class Task
    {
        public final ATNet<String, Void, JSONUtility> task;
        public final String[] params;

        private Task(ATNet<String, Void, JSONUtility> task, String[] params)
        {
            this.task = task;
            this.params = params;
        }

        /**
         * Get task from list by its requestCode
         * @param requestCode requestCode of the task
         * @param tasks list of tasks to iterate
         * @return Task with from list with given requestCode or null if no such task exists in list
         * */
        public static Task getTaskByCode(int requestCode, List<Task> tasks)
        {
            for(Task t : tasks)
            {
                if(t.task.getRequestCode() == requestCode)
                {
                    return t;
                }
            }
            return null;
        }

        /**
         * Remove all tasks from list that have specified requestCode
         * @param tasks list of tasks to iterate
         * @param requestCode requestCode of the tasks to remove
         * @return number of tasks removed from list
         * */
        public static int removeTasksFromList(List<Task> tasks, int requestCode)
        {
            int removedCount = 0;
            List<Task> tasksToRemove = new ArrayList<>();
            for(Task task : tasks)
            {
                //Add all tasks to different list first to avoid ConcurrentModificationException
                  if(task.task.getRequestCode() == requestCode)
                  {
                      tasksToRemove.add(task);
                  }
            }
            for(Task task : tasksToRemove)
            {
                //Remove all tasks from original list
                if(tasks.remove(task)) removedCount++;
            }
            return removedCount;
        }
        
    }

    public void setResponseMessagePolicy(ResponseHandlerImpl.ResponseMessagePolicy responseMessagePolicy)
    {
        this.responseMessagePolicy = responseMessagePolicy;
    }
}
