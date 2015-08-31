package de.mq.merchandise.subject.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

@Component
@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectToItemConverter)
class CommercialSubjectConverterImpl implements Converter<CommercialSubject, Item> {

	@Override
	public Item convert(final CommercialSubject commercialSubject) {
		final Item item = new PropertysetItem();

		if (commercialSubject.id().isPresent()) {
			item.addItemProperty(CommercialSubjectCols.Id, new ObjectProperty<>(commercialSubject.id().get()));
		}

		item.addItemProperty(CommercialSubjectCols.Name, new ObjectProperty<>(nvl(commercialSubject.name())));

		
		return item;
	}

	private String nvl(final String value) {
		return value == null ? "" : value;
	}

}
