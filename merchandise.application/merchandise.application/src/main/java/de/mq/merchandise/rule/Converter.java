package de.mq.merchandise.rule;


public interface Converter<S,T>  extends org.springframework.core.convert.converter.Converter<S,T>, ParameterNamesAware<T> {
	
}
