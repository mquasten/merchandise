package de.mq.merchandise.subject.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

@Component
@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemToItemConverter)
public class CommercialSubjectItemConverterImpl implements Converter<CommercialSubjectItem, Item> {

	@Override
	public Item convert(final CommercialSubjectItem commercialSubjectItem) {
		final Item item = new PropertysetItem();

		if (commercialSubjectItem.id().isPresent()) {
			item.addItemProperty(CommercialSubjectItemCols.Id, new ObjectProperty<>(commercialSubjectItem.id().get()));
		} else {
			item.addItemProperty(CommercialSubjectItemCols.Id, new ObjectProperty<>(CommercialSubjectItemCols.Id.nvl()));
		}

		item.addItemProperty(CommercialSubjectItemCols.Name, new ObjectProperty<>(nvl(commercialSubjectItem.name())));

		item.addItemProperty(CommercialSubjectItemCols.Mandatory ,  new ObjectProperty<>(commercialSubjectItem.mandatory()));
		
		if (commercialSubjectItem.subject() != null) {
			item.addItemProperty(CommercialSubjectItemCols.Subject, new ObjectProperty<>(commercialSubjectItem.subject().id().get()));
		} else {
			
			item.addItemProperty(CommercialSubjectItemCols.Subject, new ObjectProperty<>(CommercialSubjectItemCols.Subject.nvl()));
		}
		return item;
	}

	
	private String nvl(final String value) {
		return value == null ? "" : value;
	}
	
}
