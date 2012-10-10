package com.dayatang.dsrouter.tenantholder;

import com.dayatang.dsrouter.TenantHolder;

/**
 * 用线程本地变量来存取租户名称的租户持有器的实现。
 * @author yyang
 *
 */
public class ThreadLocalTenantHolder implements TenantHolder {
	
	private static final ThreadLocal<String> context = new ThreadLocal<String>();
	
	@Override
	public String getTenant() {
		return context.get();
	}
	
	public void setTenant(String tenant) {
		context.set(tenant);
	}

	public void removeTenant() {
		context.remove();
	}
	
}
