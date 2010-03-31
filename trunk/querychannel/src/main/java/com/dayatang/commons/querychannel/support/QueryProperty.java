package com.dayatang.commons.querychannel.support;

/** 
 * 
 * 
 * @author yshzhong
 *
 */
public class QueryProperty {
	//属性名
	private String propertyName;
    //如eq,gt,lt
	private String operator;
    //第一个值，对于只有一个值的属性，就对应本属性
	private Object firstValue;
    //第二个值，适用于有两个值的属性，如日期用between时
	private Object secondValue;

	public QueryProperty(String propertyName, String operator,
			Object firstValue, Object secondValue) {
		this.propertyName = propertyName;
		this.operator = operator;
		this.firstValue = firstValue;
		this.secondValue = secondValue;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getFirstValue() {
		return firstValue;
	}

	public void setFirstValue(Object firstValue) {
		this.firstValue = firstValue;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Object getSecondValue() {
		return secondValue;
	}

	public void setSecondValue(Object secondValue) {
		this.secondValue = secondValue;
	}

}
