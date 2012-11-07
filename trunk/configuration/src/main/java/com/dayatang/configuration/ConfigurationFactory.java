package com.dayatang.configuration;

import java.io.File;
import java.net.URL;

import javax.sql.DataSource;

/**
 * 数据库工厂，用于隐藏配置类的具体实现
 * @author yyang
 *
 */
public class ConfigurationFactory {

	public WritableConfiguration fromDatabase(DataSource dataSource) {
		return new ConfigurationDbImpl(dataSource);
	}
	
	public WritableConfiguration fromDatabase(DataSource dataSource, String tableName) {
		return new ConfigurationDbImpl(dataSource, tableName);
	}
	
	public WritableConfiguration fromDatabase(DataSource dataSource, String tableName, String keyColumn, String valueColumn) {
		return new ConfigurationDbImpl(dataSource, tableName, keyColumn, valueColumn);
	}

	public WritableConfiguration fromClasspath(String fileName) {
		return ConfigurationFileImpl.fromClasspath(fileName);
	}

	public WritableConfiguration fromFileSystem(String fileName) {
		return ConfigurationFileImpl.fromFileSystem(fileName);
	}

	public WritableConfiguration fromFileSystem(String dirPath, String fileName) {
		return ConfigurationFileImpl.fromFileSystem(dirPath, fileName);
	}

	public WritableConfiguration fromClasspath(File file) {
		return ConfigurationFileImpl.fromFileSystem(file);
	}

	public Configuration fromUrl(String url) {
		return ConfigurationUrlImpl.fromUrl(url);
	}

	public Configuration fromUrl(URL url) {
		return ConfigurationUrlImpl.fromUrl(url);
	}
	
}
