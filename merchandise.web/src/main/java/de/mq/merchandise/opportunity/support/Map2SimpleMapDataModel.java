package de.mq.merchandise.opportunity.support;

import java.util.Map;

import org.primefaces.model.SelectableDataModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.model.support.SimpleMapDataModel;

@Component
public class Map2SimpleMapDataModel  implements Converter<Map<String,?>, SelectableDataModel<? extends Object>>{

	@Override
	
	public SelectableDataModel<? extends Object> convert(final Map<String, ?> source) {
		 return new SimpleMapDataModel<Object>(source.keySet());
		
	}

}
