package de.mq.merchandise.rule;

public interface Validator<T> {
	
	String[] parameters();
	
	String resourceKey();
	
	boolean validate(T object);
		
	

}
