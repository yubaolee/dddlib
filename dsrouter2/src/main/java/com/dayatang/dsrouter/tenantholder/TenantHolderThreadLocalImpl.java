package com.dayatang.dsrouter.tenantholder;

import com.dayatang.dsrouter.TenantHolder;

public class TenantHolderThreadLocalImpl implements TenantHolder {
	
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
