package de.mq.merchandise.subject.support;

import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

import de.mq.merchandise.util.TableContainerColumns;

enum ConditionValueCols implements TableContainerColumns {
	Id(false,Long.class, -1L,  TextField.class),
	InputValue(true, String.class, "", TextField.class),
	
	Condition(true, Long.class, -1L, ComboBox.class);

	private final boolean visible ;
	private final Class<?> type;
	private final Object defaultValue;
	private  Class<?extends Field<?>> clazz;
	
	
	
	ConditionValueCols(final boolean visible, final Class<?> type, final Object defaultValue, final Class<?extends Field<?>> clazz ) {
		this.visible=visible;
		this.type=type;
		this.defaultValue=defaultValue;
		this.clazz=clazz;
	}

	@Override
	public boolean visible() {
		
		return this.visible;
	}

	@Override
	public Class<?> target() {
		return type;
	}

	@Override
	public boolean sortable() {
		return true;
	}

	@Override
	public String orderBy() {
		return StringUtils.uncapitalize(name());
	}

	@Override
	public Object nvl() {
		return defaultValue;
	}
	
	public final Field<?> newField() {
		return BeanUtils.instantiateClass(clazz);
	}
	
	
}
