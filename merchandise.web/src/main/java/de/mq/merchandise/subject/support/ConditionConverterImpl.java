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

		
		item.addItemProperty(ConditionCols.Id, new ObjectProperty<>(condition.id().orElse((Long)ConditionCols.Id.nvl())));
	

		item.addItemProperty(ConditionCols.ConditionType, new ObjectProperty<>(nvl(condition.conditionType(),  ConditionCols.ConditionType.nvl())));

		item.addItemProperty(ConditionCols.DataType, new ObjectProperty<>(nvl(condition.conditionDataType(), ConditionCols.DataType.nvl())));

		
		return item;
	}

	private Object nvl(final Object value, final Object nvl ) {
		return value == null ? nvl : value;
	}

}
