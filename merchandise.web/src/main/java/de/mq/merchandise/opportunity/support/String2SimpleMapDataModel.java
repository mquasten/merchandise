package de.mq.merchandise.opportunity.support;

import java.util.Collection;

import org.primefaces.model.SelectableDataModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.model.support.SimpleMapDataModel;

@Component
public class String2SimpleMapDataModel implements Converter<Collection<? extends Object>, SelectableDataModel<? extends Object>>{

	@Override
	public SelectableDataModel<? extends Object> convert(Collection<? extends Object> source) {
		return new SimpleMapDataModel<Object>(source);
	}

	
	
}
