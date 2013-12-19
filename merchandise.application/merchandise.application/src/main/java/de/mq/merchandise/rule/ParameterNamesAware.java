package de.mq.merchandise.rule;

import groovy.lang.GroovyObject;

public interface ParameterNamesAware<T>  extends GroovyObject {

	public abstract String[] parameters();

}