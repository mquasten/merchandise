package de.mq.merchandise.subject.support;

public enum ConditionDataType {
	
	IntegralNumber(Long.class),
	String(String.class),
	FractionNumber(Double.class);
	
	Class<?> targetClass;
	
	ConditionDataType(final Class<?> targetClass) {
		this.targetClass=targetClass;
	}
	
	public Class<?> targetClass() {
		return targetClass;
	}
	
}
