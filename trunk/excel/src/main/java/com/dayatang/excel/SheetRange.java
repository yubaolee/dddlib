package com.dayatang.excel;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来存放从Excel中读出的数据
 * @author yyang
 *
 */
public class SheetRange {
	private int rowCount, colCount;
	private int currentRow, currentCol;
	private boolean isFull = false;
	private List<Object[]> data = new ArrayList<Object[]>();
	
	public SheetRange(int rowCount, int colCount) {
		this.rowCount = rowCount;
		this.colCount = colCount;
		initData();
	}

	private void initData() {
		for (int i = 0; i < rowCount; i++) {
			data.add(new Object[colCount]);
		}
	}


	public List<Object[]> getData() {
		return data;
	}
	
	public Object[] getRow(int rowIndex) {
		return data.get(rowIndex);
	}
	
	public Object[] getCol(int colIndex) {
		Object[] results = new Object[rowCount];
		for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
			results[rowIndex] = getRow(rowIndex)[colIndex];
		}
		return results;
	}

	public Object getCell(int rowIndex, int colIndex) {
		return getRow(rowIndex)[colIndex];
	}
	
	public int getRowCount() {
		return rowCount;
	}

	public int getColCount() {
		return colCount;
	}

	public void addData(Object... value) {
		for (Object each : value) {
			addData(each);
		}
	}

	public void addData(Object value) {
		if (isFull) {
			throw new RuntimeException("Container is full!!!");
		}
		data.get(currentRow)[currentCol] = value;
		currentCol++;
		if (currentCol == colCount) {
			currentRow++;
			currentCol = 0;
		}
		if (currentRow == rowCount) {
			isFull = true;
		}
	}
}
