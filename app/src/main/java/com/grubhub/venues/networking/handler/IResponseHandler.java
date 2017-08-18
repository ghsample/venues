package com.grubhub.venues.networking.handler;

import org.json.JSONObject;

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
