package de.mq.merchandise.util;

import com.vaadin.data.Item;

public interface ItemContainerFactory {

	Item create(Class<? extends Enum<? extends TableContainerColumns>> colsClass);

}