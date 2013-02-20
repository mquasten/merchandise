package de.mq.merchandise.model;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.model.UserImpl;

public class UserTest {
	
	@Test
	public final void languageDefault() {
		final UserImpl language = new UserImpl();
		Assert.assertEquals("en", language.getLanguage());
	}
	
	@Test
	public final void language() {
		final UserImpl language = new UserImpl();
		language.setLanguage("DE");
		Assert.assertEquals("DE", language.getLanguage());
		
	}

}
