package com.lechlitnerd.vividseats.factory;

import android.util.Log;

import com.lechlitnerd.vividseats.constant.RequestConstants;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.security.KeyStore;

public class HttpClientFactory
{
	public static final String TAG = "HttpClientFactory";

	/**
	 * public synchronized static DefaultHttpClient getCertPinClient()
	 * 
	 * Get Certificate Pinned client
	 * 
	 * @return
	 */
	public synchronized static DefaultHttpClient getCertPinClient()
	{
		try
		{
			// setup to handle SSL connections
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			SSLSocketFactory sf = new CustomSSLSocketFactory(keyStore);
			sf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects", false);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			// params.setParameter(CoreProtocolPNames.USER_AGENT,
			// getUserAgent());
			HttpClientParams.setRedirecting(params, false);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			// Set the timeout in milliseconds until a connection is
			// established.
			HttpConnectionParams.setConnectionTimeout(params,
					RequestConstants.CONFIG_CONNECTION_TIMEOUT_MILLIS);
			// Set the default socket timeout (SO_TIMEOUT)
			HttpConnectionParams.setSoTimeout(params,
					RequestConstants.CONFIG_SOCKET_TIMEOUT_MILLIS);

			return new DefaultHttpClient(ccm, params);
		}
		catch (Exception e)
		{
			Log.e(TAG, "Error instatiating an HttpClient instance", e);
		}

		return null;
	}

	/**
	 * public synchronized static DefaultHttpClient getUnsecuredClient()
	 * 
	 * Get standard allow-all hostname, SSL client
	 * 
	 * @return A thread-safe DefaultHttpClient.
	 */
	public synchronized static DefaultHttpClient getUnsecuredClient()
	{

		try
		{
			// setup to handle SSL connections
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new CustomUnsecuredSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects", false);
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			// params.setParameter(CoreProtocolPNames.USER_AGENT,
			// getUserAgent());
			HttpClientParams.setRedirecting(params, false);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

			// Set the timeout in milliseconds until a connection is
			// established.
			HttpConnectionParams.setConnectionTimeout(params,
					RequestConstants.CONFIG_CONNECTION_TIMEOUT_MILLIS);
			// Set the default socket timeout (SO_TIMEOUT)
			HttpConnectionParams.setSoTimeout(params,
					RequestConstants.CONFIG_SOCKET_TIMEOUT_MILLIS);

			return new DefaultHttpClient(ccm, params);
		}
		catch (Exception e)
		{
			Log.e(TAG, "Error instatiating an HttpClient instance", e);
		}

		return null;
	}

}