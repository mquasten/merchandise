package de.mq.merchandise.util;

import java.util.Locale;

import com.vaadin.data.fieldgroup.FieldGroup;

public interface ValidationUtil {

	<T> boolean validate(final T source, final FieldGroup fieldGroup, final Locale locale);

	void reset(FieldGroup fieldGroup);

}