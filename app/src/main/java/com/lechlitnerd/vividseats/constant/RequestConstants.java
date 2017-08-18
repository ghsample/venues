package com.lechlitnerd.vividseats.constant;

/**
 * Created by benlechlitner on 3/3/15.
 */
public class RequestConstants {

    public static final String SERVER_URL_DEV = "https://webservices.vividseats.com/rest/mobile/v1/home/";

    public static final int CONFIG_CONNECTION_TIMEOUT_MILLIS = 0;
    public static final int CONFIG_SOCKET_TIMEOUT_MILLIS = 0;

    public static final String CERTIFICATE_PUBLIC_KEY = "";

    public static final String GET_EVENTS = "browse?includeSuggested=true";

    public static final String ACTION_LOAD = "LOAD";
    public static final String ACTION_ERROR = "ERROR";

    public static final String REQUEST_METHOD_GET = "get";
    public static final String REQUEST_METHOD_POST = "post";
    public static final String REQUEST_METHOD_POST_SELECT = "postselect";
    public static final String REQUEST_METHOD_PUT = "put";
    public static final String REQUEST_METHOD_DELETE = "delete";
    public static final String REQUEST_METHOD_LOGIN = "login";

    public static final int ERROR_CODE_NO_CONNECTION = 1;
    public static final int ERROR_CODE_CONNECTION_TIMEOUT = 2;
    public static final int ERROR_CODE_NO_AUTHORIZATION = 3;
    public static final int ERROR_CODE_UNKNOWN = 4;
    public static final String ERROR_MESSAGE_NO_CONNECTION_HEADER = "NO CONNECTION AVAILABLE";
    public static final String ERROR_MESSAGE_NO_CONNECTION_DESC = "Your connection has been lost. Please press retry once you have been reconnected, or switch to offline mode.";
    public static final String ERROR_MESSAGE_UNKNOWN_DESC = "An unknown error has occurred.";
    public static final String ERROR_CODE_KEY = "errorCode";
}
