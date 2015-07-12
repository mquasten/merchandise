package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;

import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.data.Container;
import com.vaadin.data.Item;

import de.mq.merchandise.subject.Condition;

public class ConditionToContainerConverterTest {

	private static final String CONDITION_QUALITY = "Quality";
	private static final long ID = 19680528L;
	private final Converter<Collection<Condition>, Container> converter = new ConditionToContainerConverter();

	@Test
	public final void convert() {
		final Collection<Condition> conditions = new ArrayList<>();
		final Condition condition = new ConditionImpl(Mockito.mock(SubjectImpl.class), CONDITION_QUALITY, ConditionDataType.String);
		ReflectionTestUtils.setField(condition, "id", ID);
		conditions.add(condition);

		final Container results = converter.convert(conditions);
		Assert.assertEquals(1, results.getItemIds().size());
		final Optional<?> id = (Optional<?>) results.getItemIds().stream().findFirst();
		Assert.assertTrue(id.isPresent());

		final Item item = results.getItem(id.get());
		Assert.assertEquals(condition.id().get(), item.getItemProperty(ConditionCols.Id).getValue());
		Assert.assertEquals(condition.conditionDataType(), item.getItemProperty(ConditionCols.DataType).getValue());
		Assert.assertEquals(condition.conditionType(), item.getItemProperty(ConditionCols.ConditionType).getValue());

	}

	@Test
	public final void convertNulls() {
		final Collection<Condition> conditions = new ArrayList<>();
		final Condition condition = BeanUtils.instantiateClass(ConditionImpl.class);
		conditions.add(condition);

		final Container results = converter.convert(conditions);

		Assert.assertEquals(1, results.getItemIds().size());
		final Optional<?> id = (Optional<?>) results.getItemIds().stream().findFirst();
		Assert.assertTrue(id.isPresent());

		final Item item = results.getItem(id.get());

		Arrays.asList(ConditionCols.values()).stream().forEach(col -> Assert.assertEquals(col.nvl(), item.getItemProperty(col).getValue()));
	}

}
