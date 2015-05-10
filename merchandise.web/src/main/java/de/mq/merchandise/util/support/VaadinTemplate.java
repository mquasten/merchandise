package de.mq.merchandise.util.support;



import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;


public class VaadinTemplate implements VaadinOperations {
	
	private final UI ui; 
	
	public VaadinTemplate(final UI ui) {
		this.ui=ui;
	}
	
	@Override
	public final Navigator newNavigator() {
		
		
		return new Navigator(ui, ui);
	}
	
	@Override
	public void  addWindow(final Window window) {
		ui.addWindow(window);;
	}
	
	@Override
	public final void showErrror(final String message) {
		Notification.show(message);
	}

}
