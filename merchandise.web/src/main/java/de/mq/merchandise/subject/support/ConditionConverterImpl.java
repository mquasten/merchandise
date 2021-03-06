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

		
		item.addItemProperty(ConditionCols.Id, new ObjectProperty<>( condition.id().isPresent() ? condition.id().get():  ConditionCols.Id.nvl()) );
		
		addValue(condition, item,condition.conditionDataType(), ConditionCols.DataType);

	   addValue(condition, item, condition.conditionType(), ConditionCols.ConditionType );

		
		return item;
	}

	private void addValue(final Condition condition, final Item item, final Object value, ConditionCols col ) {
		item.addItemProperty(col, new ObjectProperty<>(value != null ? value  : col.nvl() ));
	}

	

}
