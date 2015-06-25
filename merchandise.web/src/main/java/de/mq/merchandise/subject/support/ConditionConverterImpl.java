package de.mq.merchandise.subject.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.subject.Condition;

@Component
@SubjectModelQualifier(SubjectModelQualifier.Type.ConditionToItemConverter)
class ConditionConverterImpl implements Converter<Condition, Item> {

	
	
	@Override
	public Item convert(final Condition condition) {
		final Item item = new PropertysetItem();

		
		item.addItemProperty(ConditionCols.Id, new ObjectProperty<>( condition.id().isPresent() ? condition.id().get():"" ) );
		
		
		item.addItemProperty(ConditionCols.ConditionType, new ObjectProperty<>(nvl(condition.conditionType())));
		

		item.addItemProperty(ConditionCols.DataType, new ObjectProperty<>(condition.conditionDataType() != null ? condition.conditionDataType() : ConditionDataType.String ));

		
		return item;
	}

	private Object nvl(final Object value) {
		return value == null ? "": value;
	}

}
