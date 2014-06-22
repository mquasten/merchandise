package de.mq.merchandise.order.support;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import de.mq.mapping.util.proxy.support.String2IntegerConverter;
import de.mq.merchandise.opportunity.support.CommercialSubject;
import de.mq.merchandise.opportunity.support.Condition;
import de.mq.merchandise.order.Item;
import de.mq.merchandise.order.ItemSet;
import de.mq.merchandise.order.Money;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

class ItemImpl implements Item {
	@Equals()
	private final ItemSet itemSet;
	@Equals()
	private final String itemId;

	private final CommercialSubject subject;

	@Condition.Item(type = Condition.ConditionType.Product)
	private String productId;

	@Condition.Item(type = Condition.ConditionType.Quality)
	private String quality;

	@Condition.Item(type = Condition.ConditionType.Unit)
	private String unit;
	
	@Condition.Item(type = Condition.ConditionType.Detail)
	private String detail;
	@Condition.Item(type = Condition.ConditionType.Quantity, converter=String2IntegerConverter.class)
	private Number quantity;
	@Condition.Item(type = Condition.ConditionType.PricePerUnit, converter=String2MoneyConverter.class)
	private Money pricePerUnit;
	
	public ItemImpl(final ItemSet itemSet, final CommercialSubject subject) {
		this.itemSet = itemSet;
		this.subject = subject;
		this.itemId = new UUID(new Date().getTime(), Double.valueOf(1e18 * Math.random()).longValue()).toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.subject#subject()
	 */
	@Override
	public CommercialSubject subject() {
		return subject;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#itemId()
	 */
	@Override
	public UUID itemId() {
		return UUID.fromString(itemId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#itemSet()
	 */
	@Override
	public ItemSet itemSet() {
		EntityUtil.notNullGuard(itemSet, "ItemSet");
		return this.itemSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#productId()
	 */
	@Override
	public String productId() {
		return this.productId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#quantity()
	 */
	@Override
	public Number quantity() {
		return quantity;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#quality()
	 */
	@Override
	public String quality() {
		return quality;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#unit()
	 */
	@Override
	public String unit() {
		return unit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#pricePerUnit()
	 */
	@Override
	public Money pricePerUnit() {
		return pricePerUnit;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#detail()
	 */
	@Override
	public String detail() {
		return detail;
	}

	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(ItemImpl.class).isEquals();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#amount()
	 */
	@Override
	public final Money amount() {
		EntityUtil.notNullGuard(pricePerUnit, "PricePerUnit");
		EntityUtil.notNullGuard(quantity, "Quantity");
		return pricePerUnit.multiply(quantity.doubleValue());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.order.support.Item#currency()
	 */
	@Override
	public Currency currency() {
		EntityUtil.notNullGuard(pricePerUnit, "PricePerUnit");
		return pricePerUnit.currency();
	}
	
	@Override
	public void assign(final Collection<Condition> conditions) {
		final Map<Condition.ConditionType,String> values = new HashMap<>();
		for(final Condition condition : conditions) {
			if(! condition.hasInput()){
				continue;
			}
			values.put(condition.conditionType(), condition.input());
		}
		
		ReflectionUtils.doWithFields(this.getClass(), new FieldCallback() {
			
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if( ! field.isAnnotationPresent(Condition.Item.class)) {
					return; 
				}
				field.setAccessible(true);
				@SuppressWarnings("unchecked")
				final Converter<Object, ?> converter = (Converter<Object, String>) EntityUtil.create(field.getAnnotation(Condition.Item.class).converter());
				ReflectionUtils.setField(field, ItemImpl.this, converter.convert(values.get(field.getAnnotation(Condition.Item.class).type())));
			}

			
		});
	}
	
	

}
