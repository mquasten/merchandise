package de.mq.merchandise.subject.support;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;

import de.mq.merchandise.subject.Condition;

@Component
@SubjectModelQualifier(SubjectModelQualifier.Type.ConditionToContainerConverter)
public class ConditionToContainerConverter implements Converter<Collection<Condition>, Container> {

	@Override
	public Container convert(final Collection<Condition> source) {

		Assert.notNull(source, "Source should not be empty");
		final IndexedContainer container = new IndexedContainer();

		Arrays.asList(ConditionCols.values()).forEach(col -> container.addContainerProperty(col, col.target(), col.nvl()));

		source.forEach(condition -> assign(container, condition));

		return container;
	}

	@SuppressWarnings("unchecked")
	private void assign(Container container, final Condition condition) {
		final Object id =  condition.id().isPresent() ? condition.id().get() : container.addItem();
		condition.id().ifPresent(v -> container.addItem(v));
		

		
		Arrays.asList(ConditionCols.values()).stream().filter(col -> fieldValue(condition, col) != null).forEach(col -> container.getContainerProperty(id, col).setValue(fieldValue(condition, col)));

	}

	private Object fieldValue(final Condition condition, final ConditionCols col) {
		final Field field = ReflectionUtils.findField(ConditionImpl.class, StringUtils.uncapitalize(col.name()));
		Assert.notNull(field, String.format("Field not found in Condition: %s ", StringUtils.uncapitalize(col.name())));
		field.setAccessible(true);
		return ReflectionUtils.getField(field, condition);
	}

}
