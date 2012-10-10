package com.dayatang.dsrouter;

/**
 * 租户持有器。通过它可以获取当前租户的名称。
 * @author yyang
 *
 */
public interface TenantHolder {

	String getTenant();

}
