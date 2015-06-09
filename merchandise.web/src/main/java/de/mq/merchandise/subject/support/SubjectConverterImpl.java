package de.mq.merchandise.subject.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.subject.Subject;

@Component
@SubjectModelQualifier(SubjectModelQualifier.Type.SubjectToItemConverter)
class SubjectConverterImpl implements Converter<Subject, Item> {

	@Override
	public Item convert(final Subject subject) {
		final Item item = new PropertysetItem();

		if (subject.id().isPresent()) {
			item.addItemProperty(SubjectCols.Id, new ObjectProperty<>(subject.id().get()));
		}

		item.addItemProperty(SubjectCols.Name, new ObjectProperty<>(nvl(subject.name())));

		item.addItemProperty(SubjectCols.Description, new ObjectProperty<>(nvl(subject.description())));

		if (subject.created() != null) {
			item.addItemProperty(SubjectCols.DateCreated, new ObjectProperty<>(subject.created()));
		}
		return item;
	}

	private String nvl(final String value) {
		return value == null ? "" : value;
	}

}
