package de.mq.merchandise.rule.support;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.util.Parameter;
import de.mq.merchandise.util.ParameterImpl;

@Component
public class Map2ParameterCollectionConverter<T>  implements Converter<Map.Entry<String,T>, Parameter<T>> {

	@Override
	public Parameter<T> convert(final Entry<String, T> source) {
	
		return new ParameterImpl<>(source.getKey(), source.getValue());
	}




}
