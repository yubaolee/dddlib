package com.dayatang.dsrouter.mappingstrategy;


/**
 * 租户数据库映射器。读取tenant-db-mapping.properties中的映射数据。
 * @author yyang
 *
 */
public interface DbMapper {
	public String getMappingValue(String tenant);
}
