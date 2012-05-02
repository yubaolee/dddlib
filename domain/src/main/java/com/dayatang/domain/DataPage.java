package com.dayatang.domain;

import java.util.ArrayList;
import java.util.List;

public class DataPage<T> {
	private List<T> pageData = new ArrayList<T>();
	private int pageIndex;
	private int pageSize;
	private int pageCount;
	private long dataTotalSize;
	public List<T> getPageData() {
		return pageData;
	}
	public void setPageData(List<T> pageData) {
		this.pageData = pageData;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public long getDataTotalSize() {
		return dataTotalSize;
	}
	public void setDataTotalSize(long dataTotalSize) {
		this.dataTotalSize = dataTotalSize;
	}
	
	
}
