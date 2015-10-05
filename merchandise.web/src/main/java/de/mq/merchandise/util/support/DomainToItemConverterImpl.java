package de.mq.merchandise.util.support;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.support.BasicEntity;
import de.mq.merchandise.util.TableContainerColumns;


public class DomainToItemConverterImpl<T> implements Converter<T, Item> {
	
	

	private final Collection<Enum<? extends TableContainerColumns>> cols = new ArrayList<>();
	
	
	
	private final Collection<Enum<? extends TableContainerColumns>> childs = new HashSet<>(); ; 

	
	public DomainToItemConverterImpl<T> withChild(Enum<? extends TableContainerColumns> col) {
		childs.add(col);
		return this;
		
	}

	@SuppressWarnings("unchecked")
	public DomainToItemConverterImpl(Class<? extends Enum<? extends TableContainerColumns>> colClass) {
		this.cols.addAll(Arrays.asList((Enum<? extends TableContainerColumns>[]) ReflectionUtils.invokeMethod(valuesMethod(colClass), null)));
	}
	
	public DomainToItemConverterImpl(Enum<? extends TableContainerColumns>[] cols) {
		this.cols.addAll(Arrays.asList(cols));
	}

	private Method valuesMethod(Class<? extends Enum<? extends TableContainerColumns>> colClass) {
		final Method method = ReflectionUtils.findMethod(colClass, "values");

		method.setAccessible(true);
		return method;
	}

	@Override
	public Item convert(final T domain) {
		final Item item = new PropertysetItem();
		cols.forEach( col -> item.addItemProperty(col, new ObjectProperty<>(((TableContainerColumns) col).nvl())));
		
		cols.forEach(col -> {
			
			ReflectionUtils.doWithFields(domain.getClass(), field -> {
				field.setAccessible(true);
				handleValue(item, col, field.get(domain));
				
			}, field -> field.getName().endsWith(StringUtils.uncapitalize(col.name())));
			
		});
			
		return item;
	}

	@SuppressWarnings("unchecked")
	private void handleValue(final Item item, Enum<? extends TableContainerColumns> col, final Object value) {
		
		if( value == null){
			return;
		}
		if( ! childs.contains(col)) {
			 item.getItemProperty(col).setValue( value);	
			return;
		}
		
		if (value instanceof BasicEntity) {
			final Optional<Long>  identifier  = ((BasicEntity)value).id();
			if( ! identifier.isPresent()) {
				return;
			}
			item.getItemProperty(col).setValue(identifier.get());
			return;
			
		}
		
		
	}

	
	

}
