package de.mq.merchandise.util;

import com.vaadin.data.fieldgroup.FieldGroup;

public interface ItemContainerFactory {

	public abstract void assign(FieldGroup parent, Class<? extends Enum<? extends TableContainerColumns>> colsClass);

}