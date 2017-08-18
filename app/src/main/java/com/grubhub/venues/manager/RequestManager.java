package com.grubhub.venues.manager;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;

import com.grubhub.venues.constant.PreferenceConstants;
import com.grubhub.venues.constant.RequestConstants;
import com.grubhub.venues.networking.factory.HttpClientFactory;
import com.grubhub.venues.networking.handler.IResponseHandler;
import com.grubhub.venues.networking.threading.ConnectionEventQueue;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.GZIPInputStream;

public class RequestManager {
    public static final String TAG = "RequestDispatcher";
    private static final String ENCODING_GZIP = "gzip";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final boolean mUseGZip = false;

    /**
     * public static void get(ResponseHandlerInterface handler, String
     * customUrl, String action)
     *
     * @param handler
     * @param customUrl
     * @param action
     */
    public static void get(IResponseHandler handler, String customUrl,
                           String action) {
        Log.d(TAG, "Executing get with custom URL...");
        ConnectionEventQueue.queueEvent(
                new ConnectionTask(null, PreferencesManager
                        .getStringPreference(
                                PreferenceConstants.PREF_ENVIRONMENT, false)
                        + customUrl, action,
                        RequestConstants.REQUEST_METHOD_GET, handler),
                ConnectionEventQueue.PRIORITY_HIGH);
    }

    /**
     * private static class ConnectionTask extends AsyncTask<String, Void,
     * String> Inner class for creating asynchronous connections and passing
     * key/value pairs to a supplied URL.
     */
    private static class ConnectionTask extends AsyncTask<String, Void, String> {

        private String mmUrl;
        private List<NameValuePair> mmNameValuePairs;
        private String mmAction = null;
        private String mmRequestMethod = null;

        private JSONObject mmResponse = null;
        private int mmErrorCode = -1;

        private IResponseHandler mmHandler = null;

        private Context mmContext;

        /**
         * public ConnectionTask(List<NameValuePair> nameValuePairs, String url,
         * String action, String requestMethod, CustomActivity parentActivity,
         * PageFragment parentFragment)
         *
         * Constructor
         *
         * @param nameValuePairs
         * @param url
         * @param action
         * @param requestMethod
         */
        public ConnectionTask(List<NameValuePair> nameValuePairs, String url,
                              String action, String requestMethod) {
            this.mmNameValuePairs = nameValuePairs;
            this.mmUrl = url;
            this.mmAction = action;
            this.mmRequestMethod = requestMethod;
        }

        /**
         * public ConnectionTask(List<NameValuePair> nameValuePairs, String url,
         * String action, String requestMethod, ResponseHandlerInterface
         * handler)
         *
         * Constructor
         *
         * @param nameValuePairs
         * @param url
         * @param action
         * @param requestMethod
         * @param handler
         */
        public ConnectionTask(List<NameValuePair> nameValuePairs, String url,
                              String action, String requestMethod, IResponseHandler handler) {
            this.mmNameValuePairs = nameValuePairs;
            this.mmUrl = url;
            this.mmHandler = handler;
            this.mmAction = action;
            this.mmRequestMethod = requestMethod;
        }

        /**
         * protected String doInBackground(String... params)
         *
         * Executes the sending of a message to the web service
         *
         * @param params
         */
        @Override
        protected String doInBackground(String... params) {
            this.mmResponse = sendMessage();
            return null;
        }

        /**
         * protected void onPostExecute(String result)
         *
         * Reports the result to the ResponseHandler or OfflineResponseHandler
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result)
        {
            if (mmHandler != null)
            {
//                if (mmHandler instanceof Activity) //TODO && !((Activity) mmHandler).isChangingConfigurations())
//                {
                    mmHandler.processResponse(mmAction, mmResponse);
                    if (mmErrorCode != -1)
                    {
                        JSONObject errorResponse = new JSONObject();
                        try
                        {
                            errorResponse.put(RequestConstants.ERROR_CODE_KEY, mmErrorCode);
                            mmHandler.processResponse(RequestConstants.ACTION_ERROR,
                                    errorResponse);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
//                    }
                }
            }
        }


        /**
         * protected void onPreExecute()
         *
         * Unused
         */
        @Override
        protected void onPreExecute() {
        }

        /**
         * protected void onProgressUpdate(Void... values)
         *
         * Unused
         *
         * @params values
         */
        @Override
        protected void onProgressUpdate(Void... values) {
        }

        /**
         * private Response sendMessage()
         *
         * Creates an HTTPS request of type POST, GET, PUT, and DELETE. Sends
         * key/value pairs to the web service. Attaches appropriate headers and
         * values. Detects errors and stores the result for post processing. Not
         * currently being used for login but the code should be working.
         */
        private JSONObject sendMessage() {
            Context context = null;
            if (mmHandler != null) {
                if (mmHandler instanceof android.support.v4.app.Fragment)
                    context = ((android.support.v4.app.Fragment) mmHandler).getActivity();
                else if (mmHandler instanceof Activity)
                    context = (Activity) mmHandler;
            }

            if (context != null) {
                ConnectivityManager cm = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                if (cm == null || cm.getActiveNetworkInfo() == null
                        || !cm.getActiveNetworkInfo().isConnected()) {
                    // NOT CONNECTED TO INTERNET
                    mmErrorCode = RequestConstants.ERROR_CODE_NO_CONNECTION;
                    return null;
                }
            }

            // Create a new HttpClient and Post Header
            DefaultHttpClient httpclient;
//            if (PreferencesManager.getStringPreference(
//                    PreferenceConstants.PREF_ENVIRONMENT, false).equals(
//                    RequestConstants.SERVER_URL_DEV))
//                httpclient = HttpClientFactory.getCertPinClient();
//            else
                httpclient = HttpClientFactory.getUnsecuredClient();
            // START Adding GZIP Encoding
            if (mUseGZip) {
                httpclient.addRequestInterceptor(new HttpRequestInterceptor() {
                    public void process(HttpRequest request, HttpContext context) {
                        // Add header to accept gzip content
                        if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
                            request.addHeader(HEADER_ACCEPT_ENCODING,
                                    ENCODING_GZIP);
                        }
                    }
                });

                httpclient
                        .addResponseInterceptor(new HttpResponseInterceptor() {
                            public void process(HttpResponse response,
                                                HttpContext context) {
                                // Inflate any responses compressed with gzip
                                final HttpEntity entity = response.getEntity();
                                if (entity != null) {
                                    final Header encoding = entity
                                            .getContentEncoding();
                                    if (encoding != null) {
                                        for (HeaderElement element : encoding
                                                .getElements()) {
                                            if (element.getName()
                                                    .equalsIgnoreCase(
                                                            ENCODING_GZIP)) {
                                                response.setEntity(new InflatingEntity(
                                                        response.getEntity()));
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        });
            }
            // END Added GZIP Encoding

            Log.d(TAG, "Attempting to connect to: " + mmUrl);

            HttpResponse response = null;
            try {

                if (mmRequestMethod.equals(RequestConstants.REQUEST_METHOD_PUT)) {
                    HttpPut httpPut = new HttpPut(mmUrl);
                    httpPut.setHeader("Content-Type", "application/json");
                    httpPut.setHeader("Accept", "application/json");

                    if (mmNameValuePairs != null) {
                        httpPut.setEntity(new StringEntity(mmNameValuePairs
                                .get(0).getValue()));
                    }

                    // Execute HTTP Post Request
                    response = httpclient.execute(httpPut);
                } else if (mmRequestMethod
                        .equals(RequestConstants.REQUEST_METHOD_LOGIN)) {

                    HttpPost httpPost = new HttpPost(mmUrl);
                    httpPost.setHeader("Content-Type",
                            "application/x-www-form-urlencoded");
                    httpPost.setHeader("Accept", "application/json");

                    // Add Data
                    Log.d(TAG, "Adding value pairs");
                    httpPost.setEntity(new UrlEncodedFormEntity(
                            mmNameValuePairs));

                    // Execute HTTP Post Request
                    Log.d(TAG, "Starting login execute");
                    response = httpclient.execute(httpPost);
                    Log.d(TAG, "Ending login execute");
                } else if (mmRequestMethod
                        .equals(RequestConstants.REQUEST_METHOD_POST)) {
                    HttpPost httppost = new HttpPost(mmUrl);
                    httppost.setHeader("Content-Type", "application/json");
                    httppost.setHeader("Accept", "application/json");

                    // Add Data
                    if (mmNameValuePairs != null) {
                        Log.d(TAG, "Adding value pairs");
                        httppost.setEntity(new StringEntity(mmNameValuePairs
                                .get(0).getValue()));
                    }

                    response = httpclient.execute(httppost);
                } else if (mmRequestMethod
                        .equals(RequestConstants.REQUEST_METHOD_POST_SELECT)) {
                    HttpPost httppost = new HttpPost(mmUrl);
                    httppost.setHeader("Content-Type", "application/x-www-form-urlencoded");
                    httppost.setHeader("Accept", "application/json");

                    // Add Data
                    if (mmNameValuePairs != null) {
                        Log.d(TAG, "Adding value pairs");
                        httppost.setEntity(new UrlEncodedFormEntity(
                                mmNameValuePairs));
                        Log.d(TAG, "Request Header: Entity = " + httppost.getEntity().toString());
                    }

                    response = httpclient.execute(httppost);
                } else if (mmRequestMethod
                        .equals(RequestConstants.REQUEST_METHOD_DELETE)) {
                    HttpDelete httpdelete = new HttpDelete(mmUrl);
                    httpdelete.setHeader("Content-Type", "application/json");
                    httpdelete.setHeader("Accept", "application/json");

                    Log.d(TAG, "Starting delete execute");
                    response = httpclient.execute(httpdelete);
                    Log.d(TAG, "Ending delete execute");
                } else if (mmRequestMethod
                        .equals(RequestConstants.REQUEST_METHOD_GET)) {
                    HttpGet httpget = new HttpGet(mmUrl);
                    httpget.setHeader("Content-Type", "application/json");
                    httpget.setHeader("Accept", "application/json");

                    // Execute HTTP Get Request
                    response = httpclient.execute(httpget);
                } else {
                    throw new Exception(
                            "NO REQUEST METHOD SPECIFIED IN REQUESTDISPATCHER.");
                }

                // Debugging code
                Log.d(TAG, "Response Status Code: "
                        + response.getStatusLine().getStatusCode());
                Header[] headers = response.getAllHeaders();
                for (Header header : headers) {
                    Log.d(TAG, "Response Header: " + header.getName() + " = "
                            + header.getValue());
                }
                // End Debugging code

                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    String jsonResponse = EntityUtils.toString(response
                            .getEntity());
                    Log.d(TAG, "Response: " + jsonResponse);

                    try {
                        if (jsonResponse.startsWith("["))
                        {
                            JSONArray array = new JSONArray(jsonResponse);
                            JSONObject wrappedResponse = new JSONObject();
                            wrappedResponse.put("response", array);

                            return wrappedResponse;
                        }
                        else {
                            return new JSONObject(jsonResponse);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "Response: " + jsonResponse);
                    }
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
                    String jsonResponse = "";

                    try {
                        jsonResponse = EntityUtils.toString(response
                                .getEntity());
                    } catch (Exception e) {

                    }

                    if (jsonResponse != null && jsonResponse.length() > 0) {
                        Log.d(TAG, "Response: CODE: " + HttpStatus.SC_CREATED);
                        Log.d(TAG, "Response: " + jsonResponse);
                        return new JSONObject(jsonResponse);
                    } else {
                        Log.d(TAG, "Response: CODE: " + HttpStatus.SC_CREATED
                                + " - RETURNING {}");
                        return new JSONObject("{}");
                    }
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT) {
                    return new JSONObject();
                } else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_UNAUTHORIZED
                        || response.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                    mmErrorCode = RequestConstants.ERROR_CODE_NO_AUTHORIZATION;
                    try {
                        String jsonResponse = EntityUtils.toString(response
                                .getEntity());
                        Log.d(TAG, "Response: " + jsonResponse);
                        return new JSONObject(jsonResponse);
                    } catch (Exception e) {
                        Log.e(TAG, "Caught exception", e);
                        return null;
                    }
                } else {
                    try {
                        String jsonResponse = EntityUtils.toString(response
                                .getEntity());
                        Log.d(TAG, "Response: " + jsonResponse);
                    } catch (Exception e) {
                        Log.e(TAG, "Caught exception", e);
                        mmErrorCode = RequestConstants.ERROR_CODE_UNKNOWN;
                        return null;
                    }
                }

            } catch (Exception e) {
                Log.e(TAG, "RequestDispatcher::ConnectionTask::Exception", e);
            }
            mmErrorCode = RequestConstants.ERROR_CODE_CONNECTION_TIMEOUT;
            return null;
        }

        private static class InflatingEntity extends HttpEntityWrapper {
            public InflatingEntity(HttpEntity wrapped) {
                super(wrapped);
            }

            @Override
            public InputStream getContent() throws IOException {
                return new GZIPInputStream(wrappedEntity.getContent());
            }

            @Override
            public long getContentLength() {
                return -1;
            }
        }
    }
}
