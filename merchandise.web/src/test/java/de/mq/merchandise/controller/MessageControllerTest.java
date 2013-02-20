package de.mq.merchandise.controller;

import java.util.Locale;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;

import de.mq.merchandise.controller.MessageControllerImpl;
import de.mq.merchandise.model.User;

public class MessageControllerTest {
	
	private static final String LANGUAGE = "DE";
	private static final String VALUE = "VALUE";
	private static final String KEY = "WHAT_EVER";

	@Test
	public final void keyFound() {
		final User user = Mockito.mock(User.class);
		Mockito.when(user.getLanguage()).thenReturn(LANGUAGE);
		final MessageSource messageSource = Mockito.mock(MessageSource.class);
		
		final ApplicationContext ctx = Mockito.mock(ApplicationContext.class);
		Mockito.when(ctx.getBean(User.class)).thenReturn(user);
		final Map<String,String> map = new MessageControllerImpl(messageSource, ctx);
		Mockito.when(messageSource.getMessage(KEY, new Object[] { },((MessageControllerImpl)map).defaultValue(KEY)   ,  new Locale(LANGUAGE))).thenReturn(VALUE);
		Assert.assertEquals(VALUE, map.get(KEY)); 
		
	}
	
	@Test
	public final void defaultValue() {
		
		final ApplicationContext ctx = Mockito.mock(ApplicationContext.class);
		
		Assert.assertEquals(KEY+  "?", new MessageControllerImpl(Mockito.mock(MessageSource.class), ctx).defaultValue(KEY));
		
		
		
	}

}
