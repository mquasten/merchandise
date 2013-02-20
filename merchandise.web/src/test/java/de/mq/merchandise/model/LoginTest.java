package de.mq.merchandise.model;

import org.junit.Test;

import de.mq.merchandise.model.Login;
import de.mq.merchandise.model.LoginImpl;

import junit.framework.Assert;

public class LoginTest {
	
	private static final String PASSWORD = "fever";
	private static final String USER = "kminogue";

	@Test
	public final void login() {
		final Login login = new LoginImpl(USER, PASSWORD) ;
		Assert.assertEquals(USER, login.getUser());
		Assert.assertEquals(PASSWORD, login.getPassword());
	}
	
	
	@Test
	public final void loginSetter() {
		final LoginImpl login = new LoginImpl() ;
		Assert.assertNull(login.getUser());
		Assert.assertNull(login.getPassword());
		login.setUser(USER);
		login.setPassword(PASSWORD);
		
		Assert.assertEquals(USER, login.getUser());
		Assert.assertEquals(PASSWORD, login.getPassword());
	}

}
