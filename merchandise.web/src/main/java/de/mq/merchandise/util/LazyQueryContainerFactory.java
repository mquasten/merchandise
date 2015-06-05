package de.mq.merchandise.util;


import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.util.support.RefreshableContainer;


public interface LazyQueryContainerFactory {


	<E, T> RefreshableContainer create(final Enum<? extends TableContainerColumns> idPropertyId, final Number batchSize, final Converter<?, Item> converter, T fascade,  E countEvent,  E pageEvent);

	<E,T> RefreshableContainer create(final Enum<? extends TableContainerColumns> idPropertyId, Converter<?, Item> converter, T fascade,  E countEvent,  E pageEvent);

}