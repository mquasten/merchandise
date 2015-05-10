package de.mq.merchandise.util.support;


import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import de.mq.merchandise.util.support.VaadinOperations;
import de.mq.merchandise.util.support.VaadinTemplate;


public class VaadinOpeationsTest {
	
	private static final String MESSAGE = "Message";

	private final UI ui = Mockito.mock(UI.class);
	
	private VaadinOperations vaadinOperations = new VaadinTemplate(ui);
	
	@Test
	public final void newNavigator() {
		final Page page = Mockito.mock(Page.class);
		Mockito.when(ui.getPage()).thenReturn(page);
	
		Assert.assertEquals(ui, vaadinOperations.newNavigator().getUI());
		
	}
	
	
	@Test
	public final void  addWindow() {
		final Window window = Mockito.mock(Window.class);
		vaadinOperations.addWindow(window);
		Mockito.verify(ui).addWindow(window);
	}
	
	@Test
	public final void showErrror() {
		VaadinTemplate vaadinOperations = Mockito.mock(VaadinTemplate.class);
		ReflectionTestUtils.setField(vaadinOperations, "ui", ui);
		Notification notification = Mockito.mock(Notification.class);
		Mockito.when(vaadinOperations.newNotification(MESSAGE)).thenReturn(notification);
		final Page page = Mockito.mock(Page.class);
		Mockito.when(ui.getPage()).thenReturn(page);
		
		vaadinOperations.showErrror(MESSAGE);
		
		Mockito.verify(vaadinOperations).newNotification(MESSAGE);
		Mockito.verify(notification).show(page);
	}
	
	@Test
	public final void newNotification() {
		Notification result = new VaadinTemplate(ui).newNotification(MESSAGE);
		Assert.assertEquals(MESSAGE, result.getCaption());
	}

}
