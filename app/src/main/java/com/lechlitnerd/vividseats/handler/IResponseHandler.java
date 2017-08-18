package com.lechlitnerd.vividseats.handler;

import org.json.JSONObject;

/**
 * Created by benlechlitner on 3/3/15.
 */
public interface IResponseHandler {

    /**
     * public void processResponse(String action, JSONObject response);
     *
     * Each class that wants to handle a response from the backend should
     * implement this interface and override this method.
     *
     * @param action
     * @param response
     */
    public void processResponse(String action, JSONObject response);

}
