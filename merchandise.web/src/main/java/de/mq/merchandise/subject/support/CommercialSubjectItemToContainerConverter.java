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

@Component
@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.CommercialSubjectItemToContainerConverter)
public class CommercialSubjectItemToContainerConverter implements Converter<Collection<CommercialSubjectItem>, Container> {

	@Override
	public Container convert(final Collection<CommercialSubjectItem> source) {

		Assert.notNull(source, "Source should not be empty");
		final IndexedContainer container = new IndexedContainer();

		Arrays.asList(CommercialSubjectItemCols.values()).forEach(col -> container.addContainerProperty(col, col.target(), col.nvl()));

		source.forEach(condition -> assign(container, condition));

		return container;
	}

	@SuppressWarnings("unchecked")
	private void assign(Container container, final CommercialSubjectItem condition) {
		final Object id = container.addItem();
		Arrays.asList(CommercialSubjectItemCols.values()).stream().filter(col -> fieldValue(condition, col) != null).forEach(col -> container.getContainerProperty(id, col).setValue(fieldValue(condition, col)));	
	}

	private Object fieldValue(final CommercialSubjectItem commercialSubjectItem, final CommercialSubjectItemCols col) {
		
		if( CommercialSubjectItemCols.Subject == col){
			
			return commercialSubjectItem.subject().id().get();
		}
		
		
		final Field field = ReflectionUtils.findField(CommercialSubjectItemImpl.class, StringUtils.uncapitalize(col.name()));
		Assert.notNull(field, String.format("Field not found in CommercialSubjectItem: %s ", StringUtils.uncapitalize(col.name())));
		field.setAccessible(true);
		
		
		
		return ReflectionUtils.getField(field, commercialSubjectItem);
	}

}
