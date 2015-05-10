package de.mq.merchandise.subject.support;



import java.util.Arrays;
import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.util.Observer;

public class UserModelTest {
	
	private Customer customer = Mockito.mock(Customer.class);
	private UserModel userModel = new UserModelImpl(customer);
	
	@Test
	public final void customer() {
		Assert.assertEquals(customer, userModel.getCustomer());
	}
	
	@Test
	public final void locale() {
		@SuppressWarnings("unchecked")
		final Observer<UserModel.EventType> observer = Mockito.mock(Observer.class); 
		userModel.register(observer, UserModel.EventType.LocaleChanged);
		Assert.assertNull(userModel.getLocale());
		userModel.setLocale(Locale.GERMAN);
		Assert.assertEquals(Locale.GERMAN, userModel.getLocale());
		Mockito.verify(observer).process(UserModel.EventType.LocaleChanged);
		
		
	}
	@Test
	public final void setNullLocale() {
		@SuppressWarnings("unchecked")
		final Observer<UserModel.EventType> observer = Mockito.mock(Observer.class); 
		userModel.register(observer, UserModel.EventType.LocaleChanged);
		ReflectionTestUtils.setField(userModel, "locale", Locale.GERMAN);
		
		userModel.setLocale(null);
		
		Assert.assertEquals(Locale.GERMAN, userModel.getLocale());
		Mockito.verifyZeroInteractions(observer);
	}
	
	@Test
	public final void events () {
		Arrays.asList(UserModel.EventType.values()).forEach( event -> Assert.assertEquals(event, UserModel.EventType.valueOf(event.name())));
	    Assert.assertEquals(1, UserModel.EventType.values().length);
	    Assert.assertEquals(UserModel.EventType.LocaleChanged, 	Arrays.asList(UserModel.EventType.values()).stream().findAny().get());
	}

}
