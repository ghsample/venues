package com.lechlitnerd.vividseats.manager;

import android.text.TextUtils;
import android.util.Log;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class EncryptionManager
{
	private StringBuilder mIv;

	private static final String TAG = "EncryptionManager";

	private IvParameterSpec ivspec;
	private SecretKeySpec keyspec;
	private Cipher cipher;

	private boolean locked = false;

	/**
	 * public EncryptionManager()
	 * 
	 * Constructor
	 */
	public EncryptionManager()
	{
		mIv = new StringBuilder("4161AC0192938BCA");
		ivspec = new IvParameterSpec(mIv.toString().getBytes());
		keyspec = new SecretKeySpec(mIv.toString().getBytes(), "AES");
		try
		{
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e)
		{
			e.printStackTrace();
		} catch (NoSuchPaddingException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * public String encrypt(String text) throws Exception
	 * 
	 * @param text
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String text) throws Exception
	{
		if (text == null || text.length() == 0)
			throw new Exception("Empty string");
		byte[] encrypted = null;
		try
		{
			cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);

			encrypted = cipher.doFinal(text.getBytes("UTF-8"));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return bytesToHex(encrypted);
	}

	/**
	 * public String decrypt(String code) throws Exception
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String decrypt(String code) throws Exception
	{
		while (locked)
		{
			synchronized (this)
			{
				this.wait(100);
			}
		}
		locked = true;
		if (TextUtils.isEmpty(code))
			throw new Exception("Empty string");

		byte[] decrypted = null;
		try
		{
			cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
			byte[] temp = hexToBytes(code);
			decrypted = cipher.doFinal(temp);
		} catch (Exception e)
		{
			Log.e(TAG, "Error performing decrypt", e);
		}
		locked = false;
		return new String(decrypted);
	}

	/**
	 * public static String bytesToHex(byte[] data)
	 * 
	 * @param data
	 * @return
	 */
	public static String bytesToHex(byte[] data)
	{
		if (data == null)
			return null;
		int len = data.length;
		String str = "";
		for (int i = 0; i < len; i++)
		{
			if ((data[i] & 0xFF) < 16)
				str = str + "0" + Integer.toHexString(data[i] & 0xFF);
			else
				str = str + Integer.toHexString(data[i] & 0xFF);
		}
		return str;
	}

	/**
	 * public static byte[] hexToBytes(String str)
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hexToBytes(String str)
	{
		if (str == null)
			return null;
		else if (str.length() < 2)
			return null;
		else
		{
			int len = str.length() / 2;
			byte[] buffer = new byte[len];
			for (int i = 0; i < len; i++)
			{
				buffer[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
			}
			return buffer;
		}
	}
}
