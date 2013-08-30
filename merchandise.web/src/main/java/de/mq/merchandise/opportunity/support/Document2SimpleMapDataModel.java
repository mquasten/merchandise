package de.mq.merchandise.opportunity.support;

import org.primefaces.model.SelectableDataModel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.model.support.SimpleMapDataModel;

@Component
public class Document2SimpleMapDataModel  implements Converter<DocumentsAware, SelectableDataModel<? extends Object>>{

	@Override
	
	public SelectableDataModel<? extends Object> convert(final DocumentsAware source) {
	
		 return new SimpleMapDataModel<Object>(source.documents().keySet());
		
	}

}
