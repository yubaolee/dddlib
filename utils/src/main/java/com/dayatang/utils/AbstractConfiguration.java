package com.dayatang.utils;

import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.lang3.StringUtils;

public abstract class AbstractConfiguration implements Configuration {
	private String prefix = "";

	/**
	 * 激活配置前缀功能
	 * 
	 * @param prefix 如"com.dayatang.mes."
	 */
	public void usePrefix(final String prefix) {
		if (StringUtils.isNotBlank(prefix)) {
			this.prefix = prefix.endsWith(".") ? prefix : prefix + ".";
		}
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String key, String defaultValue) {
		Assert.notBlank(key, "Key is null or empty!");
		String result = getHashtable().get(key);
		if (result == null) {
			result = (String) getHashtable().get(prefix + key);
		}
		return result == null ? defaultValue : result;
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		return getString(key, "");
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setString(java.lang.String, java.lang.String)
	 */
	@Override
	public void setString(String key, String value) {
		Assert.notBlank(key, "Key is null or empty!");
		if (StringUtils.isBlank(value)) {
			getHashtable().remove(key);
			return;
		}
		getHashtable().put(key, StringPropertyReplacer.replaceProperties(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(String key, int defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return StringUtils.isBlank(result) ? defaultValue : Integer.parseInt(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String key) {
		return getInt(key, 0);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setInt(java.lang.String, int)
	 */
	@Override
	public void setInt(String key, int value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getLong(java.lang.String, long)
	 */
	@Override
	public long getLong(String key, long defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return StringUtils.isBlank(result) ? defaultValue : Long.parseLong(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getLong(java.lang.String)
	 */
	@Override
	public long getLong(String key) {
		return getLong(key, 0);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setLong(java.lang.String, long)
	 */
	@Override
	public void setLong(String key, long value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDouble(java.lang.String, double)
	 */
	@Override
	public double getDouble(String key, double defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return StringUtils.isBlank(result) ? defaultValue : Double.parseDouble(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDouble(java.lang.String)
	 */
	@Override
	public double getDouble(String key) {
		return getDouble(key, 0);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setDouble(java.lang.String, double)
	 */
	@Override
	public void setDouble(String key, double value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		String result = getString(key, String.valueOf(defaultValue));
		return StringUtils.isBlank(result) ? defaultValue : Boolean.parseBoolean(result);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setBoolean(java.lang.String, boolean)
	 */
	@Override
	public void setBoolean(String key, boolean value) {
		setString(key, String.valueOf(value));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDate(java.lang.String, java.util.Date)
	 */
	@Override
	public Date getDate(String key, Date defaultValue) {
		String result = getString(key);
		return StringUtils.isBlank(result) ? defaultValue : new Date(Long.parseLong(result));
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.Configuration#getDate(java.lang.String)
	 */
	@Override
	public Date getDate(String key) {
		return getDate(key, null);
	}

	/* (non-Javadoc)
	 * @see com.dayatang.utils.WritableConfiguration#setDate(java.lang.String, java.util.Date)
	 */
	@Override
	public void setDate(String key, Date value) {
		if (value == null) {
			setString(key, "");
		}
		setString(key, String.valueOf(value.getTime()));
	}

	public abstract Hashtable<String, String> getHashtable();
}
