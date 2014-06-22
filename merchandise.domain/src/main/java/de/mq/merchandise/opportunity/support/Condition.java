package de.mq.merchandise.opportunity.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.springframework.core.convert.converter.Converter;

import de.mq.mapping.util.proxy.NoConverter;
import de.mq.merchandise.BasicEntity;

public interface Condition extends BasicEntity, RuleOperations {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	@interface Item {
		ConditionType type();
		Class<? extends Converter<?,?>> converter() default NoConverter.class;
	}
	
	enum ConditionType {
		Product,
		Quantity,
		Unit,
		PricePerUnit,
		Quality,
		Currency, 
		Detail;
	}
	
	enum InputType {
		User,
		Calculated
	}

	List<String> values();
	
	CommercialRelation commercialRelation();
	
	ConditionType conditionType();
	
	InputType inputTyp();
	
	void assignValue(final String value);
	
	void removeValue(final String value);

	void assignInput(final String input);

	boolean hasInput();

	String input();
	
}
