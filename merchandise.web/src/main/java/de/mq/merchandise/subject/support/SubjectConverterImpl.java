package de.mq.merchandise.subject.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.subject.Subject;



@Component
class SubjectConverterImpl implements Converter<Subject, Item> {

	@Override
	public Item convert(final Subject subject) {
		final Item item = new PropertysetItem();
		item.addItemProperty(SubjectCols.Id, new  ObjectProperty<>(subject.id()));
		item.addItemProperty(SubjectCols.Name, new  ObjectProperty<>(subject.name()));
		item.addItemProperty(SubjectCols.Description, new  ObjectProperty<>(subject.description()));
		return item;
	}

}
