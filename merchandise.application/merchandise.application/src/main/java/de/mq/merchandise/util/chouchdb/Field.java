package de.mq.merchandise.util.chouchdb;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.convert.converter.Converter;

import de.mq.mapping.util.proxy.NoConverter;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Field {
	String value() default "";
	Class<? extends Converter<?,?>>  converter() default NoConverter.class;
}
