package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;

import de.mq.merchandise.subject.Subject;

@Component
@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.EntriesToConatainerConverter)
class SubjectListToItemConverterImpl implements Converter<Collection<Subject>, Container> {

	
	@Override
	public Container convert(Collection<Subject> source) {
		final Container container=new IndexedContainer();
		
		container.addContainerProperty(SubjectCols.Name, String.class,"");
		
		
		source.forEach(e -> {
			
			final Item item = container.addItem(e.id().get());
		
			@SuppressWarnings("unchecked")
			final Property<String> property = item.getItemProperty(SubjectCols.Name);
			
			
			property.setValue(e.name());
			
			
			
		});
		
		return container;
	}

	



}
