package com.dayatang.domain;

import java.util.ArrayList;
import java.util.List;

public class DataPage<T> {
	private List<T> data = new ArrayList<T>();
	private int pageSize;
	private int pageIndex;
	private int pageCount;
	private long dataTotalSize;
}
