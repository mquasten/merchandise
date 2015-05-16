package de.mq.merchandise.util.support;

import java.util.Collection;

import com.vaadin.navigator.View;

public interface ViewNav {

	void create(View root, Collection<View> views, final VaadinOperations vaadinOperations);

	void navigateTo(Class<? extends View> clazz, final String ... params );

}