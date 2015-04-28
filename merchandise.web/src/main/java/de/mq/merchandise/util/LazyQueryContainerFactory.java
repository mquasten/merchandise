package de.mq.merchandise.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;
import com.vaadin.ui.Table;


public interface LazyQueryContainerFactory {
	


	public enum PagingMethods {
		Count,
		Read;
	}

	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface PagingMethod {
		PagingMethods value();
	}
	

	void assign(final Table parent, final Enum<? extends TableContainerColumns> idPropertyId, final Number batchSize, final Class<? extends Converter<?, Item>> converterClass, final Class<?> controllerTarget);

	void assign(final Table parent,final Enum<? extends TableContainerColumns> idPropertyId, final Class<? extends Converter<?, Item>> converterClass, final Class<?> controllerTarget);

}