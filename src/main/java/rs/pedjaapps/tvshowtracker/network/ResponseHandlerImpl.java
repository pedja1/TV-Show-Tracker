package rs.pedjaapps.tvshowtracker.network;



/**
 * Created by pedja on 2/19/14 10.17.
 * This class is part of the ${PROJECT_NAME}
 * Copyright © 2014 ${OWNER}
 *
 * <br>
 * Implementation of {@link com.preezm.preezm.network.ResponseHandler} for general purpose response handling
 *
 * @author Predrag Čokulov
 */
import android.support.annotation.*;
import rs.pedjaapps.tvshowtracker.*;
import rs.pedjaapps.tvshowtracker.utils.*;

public class ResponseHandlerImpl implements ResponseHandler
{
    public static final ResponseMessagePolicy DEFAULT_RESPONSE_MESSAGE_POLICY = new ResponseMessagePolicy();

    public ResponseHandlerImpl(JSONUtility jsonUtility)
    {
        this(jsonUtility, DEFAULT_RESPONSE_MESSAGE_POLICY);
    }

    public ResponseHandlerImpl(JSONUtility jsonUtility, @NonNull ResponseMessagePolicy responseMessagePolicy)
    {
        if(jsonUtility == null)return;
        if (jsonUtility.isSuccess())
        {
            if (jsonUtility.checkResponse())
            {
                onResponse(RESPONSE_STATUS_OK, jsonUtility);
            }
            else
            {
                onResponse(RESPONSE_STATUS_ERROR, jsonUtility);
            }
            if(jsonUtility.getResponse() != null && jsonUtility.getResponse().message != null && responseMessagePolicy.showSuccessMessages)Utility.showToast(MainApp.getContext(), jsonUtility.getResponse().message);
        }
        else
        {
            Internet.Response response = jsonUtility.getServerResponse();
            if(response != null && responseMessagePolicy.showErrorMessages)
            {
                Utility.showToast(MainApp.getContext(),
                        response.responseMessage != null && !response.isResponseOk() ? response.responseMessage : MainApp.getContext().getString(R.string.unknown_error));
            }
            onResponse(RESPONSE_STATUS_ERROR, jsonUtility);
        }
    }

    public ResponseHandlerImpl()
    {

    }

    @Override
    public void onResponse(int responseStatus, JSONUtility jsonUtility)
    {

    }

    public static class ResponseMessagePolicy
    {
        public boolean showSuccessMessages = true;
        public boolean showErrorMessages = true;
        /**
         * Not supported yet*/
        public ResponseMessageFormat responseMessageFormat = ResponseMessageFormat.show_as_toast;

        public ResponseMessagePolicy setShowSuccessMessages(boolean showSuccessMessages)
        {
            this.showSuccessMessages = showSuccessMessages;
            return this;
        }

        public ResponseMessagePolicy setShowErrorMessages(boolean showErrorMessages)
        {
            this.showErrorMessages = showErrorMessages;
            return this;
        }

        public ResponseMessagePolicy setResponseMessageFormat(ResponseMessageFormat responseMessageFormat)
        {
            this.responseMessageFormat = responseMessageFormat;
            return this;
        }

        public enum ResponseMessageFormat
        {
            show_as_toast, show_as_dialog, show_as_notification/*this one is stupid*/
        }
    }

}
