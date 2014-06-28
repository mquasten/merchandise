package de.mq.merchandise.order.support;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.util.EntityUtil;

public class ConditionReflectionTemplate implements ConditionOperations {
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ConditionOperations#copy(java.util.Collection, de.mq.merchandise.order.Item)
	 */
	@Override
	public final void copy(final Collection<Condition> conditions, final Item item) {
	final Map<Condition.ConditionType,String> values = new HashMap<>();
	for(final Condition condition : conditions) {
		values.put(condition.conditionType(), condition.input());
	}
	
	copy(values, item);
	
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ConditionOperations#copy(java.util.Map, de.mq.merchandise.order.Item)
	 */
	@Override
	public final void copy(final Map<Condition.ConditionType, String> values, final Item item) {
		ReflectionUtils.doWithFields(item.getClass(), new FieldCallback() {
			
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if( ! field.isAnnotationPresent(Condition.Item.class)) {
					return; 
				}
				if(! values.containsKey(field.getAnnotation(Condition.Item.class).type())) {
				    return; 	
				}	
				
				field.setAccessible(true);
				@SuppressWarnings("unchecked")
				final Converter<Object, ?> converter = (Converter<Object, String>) EntityUtil.create(field.getAnnotation(Condition.Item.class).converter());
				ReflectionUtils.setField(field, item, converter.convert(values.get(field.getAnnotation(Condition.Item.class).type())));
			}

			
		});
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.order.support.ConditionOperations#copy(de.mq.merchandise.opportunity.support.Condition.ConditionType, java.lang.String,  de.mq.merchandise.order.Item)
	 */
	@Override
	public final void copy(final Condition.ConditionType conditionType, final String value, final Item item) {
		final Map<Condition.ConditionType,String> values = new HashMap<>();
		values.put(conditionType, value);
		copy(values, item);
	}


}
