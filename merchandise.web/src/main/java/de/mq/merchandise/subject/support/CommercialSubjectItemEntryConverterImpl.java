package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Map.Entry;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;

@Component
@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.EntriesToConatainerConverter)
class EntryToItemConverterImpl implements Converter<Collection<Entry<Long,String>>, Container> {

	
	@Override
	public Container convert(Collection<Entry<Long, String>> source) {
		final Container container=new IndexedContainer();
		
		container.addContainerProperty(SubjectCols.Name, String.class,"");
		
		source.forEach(e -> {
			
			final Item item = container.addItem(e.getKey());
			@SuppressWarnings("unchecked")
			final Property<Object> property = item.getItemProperty(SubjectCols.Name);
			
			property.setValue(e.getValue());
			
			
			
		});
		
		return container;
	}

	



}
