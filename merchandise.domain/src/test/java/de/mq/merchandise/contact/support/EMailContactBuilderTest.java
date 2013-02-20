package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.EMailContactBuilder;
import de.mq.merchandise.contact.support.EMailContactBuilderImpl;

public class EMailContactBuilderTest {
	
	private static final String MAIL_ACCOUNT = "kylie.minogue.com";

	@Test
	public final void createMailContact() {
		final EMailContactBuilder builder = new EMailContactBuilderImpl();
		Assert.assertEquals(builder, builder.withAccount(MAIL_ACCOUNT));
		Assert.assertEquals(MAIL_ACCOUNT, builder.build().contact());
	}
	
	@Test
	public final void login() {
		final EMailContactBuilder builder = new EMailContactBuilderImpl();
		Assert.assertFalse((boolean) ReflectionTestUtils.getField(builder, "login"));
		Assert.assertEquals(builder, builder.withLogin());
		Assert.assertTrue((boolean) ReflectionTestUtils.getField(builder, "login"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void createInvalidMailContact() {
		new EMailContactBuilderImpl().build();
	}

}
