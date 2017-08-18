package com.grubhub.venues.networking.factory;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class CustomUnsecuredSocketFactory extends SSLSocketFactory
{
	SSLContext sslContext = SSLContext.getInstance("TLS");

	/**
	 * public CustomUnsecuredSocketFactory(KeyStore truststore) throws
	 * NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
	 * UnrecoverableKeyException
	 * 
	 * Creates an X509Certificate to access a web service via HTTPS
	 * 
	 * @param truststore
	 * @throws java.security.NoSuchAlgorithmException
	 * @throws java.security.KeyManagementException
	 * @throws java.security.KeyStoreException
	 * @throws java.security.UnrecoverableKeyException
	 */
	public CustomUnsecuredSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
			KeyManagementException, KeyStoreException, UnrecoverableKeyException
	{
		super(truststore);

		TrustManager tm = new X509TrustManager()
		{
			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType)
					throws CertificateException
			{

			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType)
					throws CertificateException
			{

			}

			@Override
			public X509Certificate[] getAcceptedIssuers()
			{
				return null;
			}
		};
		sslContext.init(null, new TrustManager[] { tm }, null);
	}

	/**
	 * public Socket createSocket(Socket socket, String host, int port, boolean
	 * autoClose) throws IOException, UnknownHostException (non-Javadoc)
	 *
	 * @see org.apache.http.conn.ssl.SSLSocketFactory#createSocket(java.net.Socket,
	 *      String, int, boolean)
	 */
	@Override
	public Socket createSocket(Socket socket, String host, int port, boolean autoClose)
			throws IOException, UnknownHostException
	{
		return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
	}

	/**
	 * public Socket createSocket() throws IOException (non-Javadoc)
	 *
	 * @see org.apache.http.conn.ssl.SSLSocketFactory#createSocket()
	 */
	@Override
	public Socket createSocket() throws IOException
	{
		return sslContext.getSocketFactory().createSocket();
	}
}