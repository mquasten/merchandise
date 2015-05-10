package de.mq.merchandise.util.support;

import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Window;

public interface VaadinOperations {

	Navigator newNavigator();


	void addWindow(final Window window);


	void showErrror(final String message);

}