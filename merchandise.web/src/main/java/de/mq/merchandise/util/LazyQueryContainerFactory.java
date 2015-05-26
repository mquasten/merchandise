package de.mq.merchandise.util;


import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.util.support.RefreshableContainer;


public interface LazyQueryContainerFactory {


	<T> RefreshableContainer create(final Enum<? extends TableContainerColumns> idPropertyId, final Number batchSize, final Converter<?, Item> converter, final T countEvent, final T pageEvent);

	<T> RefreshableContainer  create(final Enum<? extends TableContainerColumns> idPropertyId, final Converter<?, Item> converter, final T countEvent, final T pageEvent);

}