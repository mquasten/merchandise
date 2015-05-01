package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.util.ItemContainerFactory;
import de.mq.merchandise.util.TableContainerColumns;

@Component
class SimpleItemContainerFactoryImpl implements ItemContainerFactory {
	
	
	
	private static final String VALUES_METHOD_NAME = "values";

	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.support.ItemContainerFactory#assign(com.vaadin.data.fieldgroup.FieldGroup, java.lang.Class)
	 */
	@Override
	public final void assign(final FieldGroup parent,  final Class<? extends  Enum<? extends TableContainerColumns>> colsClass ){
		final Method method = ReflectionUtils.findMethod(colsClass, VALUES_METHOD_NAME);
		
		method.setAccessible(true);
		final Item item = new PropertysetItem();
		Arrays.asList((TableContainerColumns[]) ReflectionUtils.invokeMethod(method, null)).forEach( col -> item.addItemProperty(col, new ObjectProperty<>(col.nvl())));
	
		parent.setItemDataSource(item);
		parent.setBuffered(true);
	
	}

}
