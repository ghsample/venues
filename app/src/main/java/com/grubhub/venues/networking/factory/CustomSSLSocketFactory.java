package com.grubhub.venues.networking.factory;

import com.grubhub.venues.constant.RequestConstants;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CustomSSLSocketFactory extends SSLSocketFactory
{
	SSLContext sslContext = SSLContext.getInstance("TLS");

	/**
	 * public CustomSSLSocketFactory(KeyStore truststore) throws
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
	public CustomSSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException,
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
				if (chain == null)
				{
					throw new IllegalArgumentException(
							"checkServerTrusted::X509Certificate array is null");
				}

				if (!(chain.length > 0))
				{
					throw new IllegalArgumentException(
							"checkServerTrusted::X509Certificate is empty");
				}

				if (!(null != authType && authType.equalsIgnoreCase("RSA")))
				{
					throw new CertificateException("checkServerTrusted::AuthType is not RSA");
				}

				// Perform customary SSL/TLS checks
				try
				{
					TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
					tmf.init((KeyStore) null);

					for (TrustManager trustManager : tmf.getTrustManagers())
					{
						((X509TrustManager) trustManager).checkServerTrusted(chain, authType);
					}
				}
				catch (Exception e)
				{
					throw new CertificateException(e);
				}

				// Hack ahead: BigInteger and toString(). We know a DER encoded
				// Public Key begins with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED),
				// so there is no leading 0x00 to drop.
				RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
				String encoded = new BigInteger(1, pubkey.getEncoded()).toString(16);

				// Pin it
				final boolean expected = RequestConstants.CERTIFICATE_PUBLIC_KEY
						.equalsIgnoreCase(encoded);
				if (!expected)
				{
					throw new CertificateException("checkServerTrusted::Expected public key: "
							+ RequestConstants.CERTIFICATE_PUBLIC_KEY + ", got public key:"
							+ encoded);
				}
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