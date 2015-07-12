package de.mq.merchandise.subject.support;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.subject.Condition;

public class ConditionConverterTest {

	private static final Optional<Long> ID = Optional.of(19680528L);
	private final Converter<Condition, Item> converter = new ConditionConverterImpl();

	@Test
	public final void convert() {
		final Condition condition = Mockito.mock(Condition.class);

		Mockito.when(condition.id()).thenReturn(ID);
		Mockito.when(condition.conditionType()).thenReturn("Quality");
		Mockito.when(condition.conditionDataType()).thenReturn(ConditionDataType.String);

		final Item item = converter.convert(condition);

		Assert.assertEquals(condition.id().get(), item.getItemProperty(ConditionCols.Id).getValue());
		Assert.assertEquals(condition.conditionType(), item.getItemProperty(ConditionCols.ConditionType).getValue());
		Assert.assertEquals(condition.conditionDataType(), item.getItemProperty(ConditionCols.DataType).getValue());

	}

	@Test
	public final void convertNulls() {
		final Condition condition = Mockito.mock(Condition.class);
		Mockito.when(condition.id()).thenReturn(Optional.empty());
		final Item item = converter.convert(condition);

		item.getItemPropertyIds().stream().forEach(id -> Assert.assertEquals(ConditionCols.valueOf(id.toString()).nvl(), item.getItemProperty(id).getValue()));
	}

}
