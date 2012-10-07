package com.dayatang.dsrouter.impl;

import com.dayatang.dsrouter.TenantHolder;

public class TenantHolderThreadLocalImpl implements TenantHolder {
	
	private static final ThreadLocal<String> context = new ThreadLocal<String>();
	
	@Override
	public String getTenantId() {
		return context.get();
	}
	
	public void setTenantId(String tenantId) {
		context.set(tenantId);
	}

	public void removeTenantId() {
		context.remove();
	}
	
}
