package com.lechlitnerd.vividseats.manager;

import android.content.SharedPreferences;
import android.util.Log;

import com.lechlitnerd.vividseats.constant.PreferenceConstants;


public class PreferencesManager
{
	private static final String TAG = "PreferencesManager";
	public static SharedPreferences settings;
	private static EncryptionManager encryptionManager;

	/**
	 * public static String getEncryptedUser()
	 * 
	 * Returns the encrypted username for the logged in user.
	 * 
	 * @return
	 */
	public static String getEncryptedUser()
	{
		try
		{
			return PreferencesManager.settings.getString(PreferenceConstants.PREF_ENC_USERNAME, "");
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * public static String getDecryptedUser()
	 * 
	 * Returns the decrypted username for the logged in user.
	 * 
	 * @return
	 */
	public static String getDecryptedUser()
	{
		try
		{
			String s = PreferencesManager.settings.getString(PreferenceConstants.PREF_ENC_USERNAME,
					"");
			if (s == null || s.equals(""))
				return null;
			return getEncryptionManager().decrypt(s);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * public static void setUser(String user)
	 * 
	 * Stores the encrypted username as a preference
	 * 
	 * @param user
	 */
	public static void setUser(String user)
	{
		try
		{
			storePreference(PreferenceConstants.PREF_ENC_USERNAME, user, false);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * public static void storePreference(String key, String value, boolean
	 * salt)
	 * 
	 * Stores variables of type String into the preferences. All data is
	 * encrypted. The boolean called salt adds the encrypted username to the
	 * beginning of the key to make it user specific.
	 * 
	 * @param key
	 * @param value
	 * @param salt
	 */
	public static void storePreference(String key, String value, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return;
		SharedPreferences.Editor editor = PreferencesManager.settings.edit();
		try
		{
			if (value == null || value.equals(""))
				Log.w(TAG, "VALUE IS NULL FOR " + key);

			if (salt)
				editor.putString(getEncryptedUser() + key, getEncryptionManager().encrypt(value));
			else
				editor.putString(key, getEncryptionManager().encrypt(value));
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		editor.commit();
	}

	/**
	 * public static void storePreference(String key, boolean value, boolean
	 * salt)
	 * 
	 * Stores variables of type boolean into the preferences. All data is
	 * encrypted. The boolean called salt adds the encrypted username to the
	 * beginning of the key to make it user specific.
	 * 
	 * @param key
	 * @param value
	 * @param salt
	 */
	public static void storePreference(String key, boolean value, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return;
		SharedPreferences.Editor editor = PreferencesManager.settings.edit();
		if (salt)
			editor.putBoolean(getEncryptedUser() + key, value);
		else
			editor.putBoolean(key, value);
		editor.commit();
	}

	/**
	 * public static void storePreference(String key, float value, boolean salt)
	 * 
	 * Stores variables of type float into the preferences. All data is
	 * encrypted. The boolean called salt adds the encrypted username to the
	 * beginning of the key to make it user specific.
	 * 
	 * @param key
	 * @param value
	 * @param salt
	 */
	public static void storePreference(String key, float value, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return;
		SharedPreferences.Editor editor = PreferencesManager.settings.edit();
		if (salt)
			editor.putFloat(getEncryptedUser() + key, value);
		else
			editor.putFloat(key, value);
		editor.commit();
	}

	/**
	 * public static void storePreference(String key, int value, boolean salt)
	 * 
	 * Stores variables of type int into the preferences. All data is encrypted.
	 * The boolean called salt adds the encrypted username to the beginning of
	 * the key to make it user specific.
	 * 
	 * @param key
	 * @param value
	 * @param salt
	 */
	public static void storePreference(String key, int value, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return;
		SharedPreferences.Editor editor = PreferencesManager.settings.edit();
		if (salt)
			editor.putInt(getEncryptedUser() + key, value);
		else
			editor.putInt(key, value);
		editor.commit();
	}

	/**
	 * public static void storePreference(String key, long value, boolean salt)
	 * 
	 * Stores variables of type long into the preferences. All data is
	 * encrypted. The boolean called salt adds the encrypted username to the
	 * beginning of the key to make it user specific.
	 * 
	 * @param key
	 * @param value
	 * @param salt
	 */
	public static void storePreference(String key, long value, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return;
		SharedPreferences.Editor editor = PreferencesManager.settings.edit();
		if (salt)
			editor.putLong(getEncryptedUser() + key, value);
		else
			editor.putLong(key, value);
		editor.commit();
	}

	/**
	 * public static boolean getBooleanPreference(String key, boolean salt)
	 * 
	 * Retrieves a boolean preference. Accounts for user specific data using the
	 * boolean salt.
	 * 
	 * @param key
	 * @param salt
	 * @return
	 */
	public static boolean getBooleanPreference(String key, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return false;
		if (salt)
			key = getEncryptedUser() + key;
		return PreferencesManager.settings.getBoolean(key, false);
	}

	/**
	 * public static float getFloatPreference(String key, boolean salt)
	 * 
	 * Retrieves a float preference. Accounts for user specific data using the
	 * boolean salt.
	 * 
	 * @param key
	 * @param salt
	 * @return
	 */
	public static float getFloatPreference(String key, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return 0;

		if (salt)
			key = getEncryptedUser() + key;
		return PreferencesManager.settings.getFloat(key, 0);
	}

	/**
	 * public static long getLongPreference(String key, boolean salt)
	 * 
	 * Retrieves a long preference. Accounts for user specific data using the
	 * boolean salt.
	 * 
	 * @param key
	 * @param salt
	 * @return
	 */
	public static long getLongPreference(String key, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return 0;

		if (salt)
			key = getEncryptedUser() + key;
		return PreferencesManager.settings.getLong(key, 0);
	}

	/**
	 * public static String getStringPreference(String key, boolean salt)
	 * 
	 * Retrieves a String preference. Accounts for user specific data using the
	 * boolean salt.
	 * 
	 * @param key
	 * @param salt
	 * @return
	 */
	public static String getStringPreference(String key, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return null;
		try
		{
			if (salt)
				key = getEncryptedUser() + key;
			String value = PreferencesManager.settings.getString(key, "");
			if (value.equals(""))
				return null;
			return getEncryptionManager().decrypt(value);
		} catch (Exception e)
		{
			Log.e(TAG, "error decrypting value", e);
		}
		return null;
	}

	/**
	 * public static int getIntPreference(String key, boolean salt)
	 * 
	 * Retrieves an int preference. Accounts for user specific data using the
	 * boolean salt.
	 * 
	 * @param key
	 * @param salt
	 * @return
	 */
	public static int getIntPreference(String key, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return 0;
		if (salt)
			key = getEncryptedUser() + key;
		return PreferencesManager.settings.getInt(key, 0);
	}

	/**
	 * public static void removePreference(String key, boolean salt)
	 * 
	 * Removes a specific key from the Preferences. Allows the specification of
	 * a user specific variable.
	 * 
	 * @param key
	 * @param salt
	 */
	public static void removePreference(String key, boolean salt)
	{
		if (PreferencesManager.settings == null)
			return;
		SharedPreferences.Editor editor = PreferencesManager.settings.edit();
		if (salt)
			editor.remove(getEncryptedUser() + key);
		else
			editor.remove(key);
		editor.commit();
	}

	/**
	 * public static void removeAllPreferences()
	 * 
	 * Removes all preferences. This is currently unused, but should be used
	 * when the entire app needs to be erased.
	 */
	public static void removeAllPreferences()
	{
		if (PreferencesManager.settings == null)
			return;
		SharedPreferences.Editor editor = PreferencesManager.settings.edit();
		editor.clear();
		editor.commit();
	}

	/**
	 * private static EncryptionManager getEncryptionManager()
	 * 
	 * Creates an EncryptionManager to aide in storing the preferences
	 * encrypted.
	 * 
	 * @return
	 */
	private static EncryptionManager getEncryptionManager()
	{
		if (encryptionManager == null)
			encryptionManager = new EncryptionManager();
		return encryptionManager;
	}
}