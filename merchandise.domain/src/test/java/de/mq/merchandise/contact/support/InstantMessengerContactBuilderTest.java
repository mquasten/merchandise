package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.contact.InstantMessengerContactBuilder;
import de.mq.merchandise.contact.support.InstantMessengerContactBuilderImpl;

public class InstantMessengerContactBuilderTest {
	
	private static final String ACCOUNT = "hotKylie";

	@Test
	public final void instantMessengerContact() {
		final InstantMessengerContactBuilder builder = new InstantMessengerContactBuilderImpl();
		Assert.assertEquals(builder, builder.withAccount(ACCOUNT));
		Assert.assertEquals(builder,builder.withProvider(InstantMessenger.Skype));
		Assert.assertEquals(InstantMessenger.Skype +": " +ACCOUNT, builder.build().contact());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void instandMessengerContactWithoutAccount() {
		final InstantMessengerContactBuilder builder = new InstantMessengerContactBuilderImpl();
		builder.withProvider(InstantMessenger.Skype);
		builder.build();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void instandMessengerContactWithoutProvider() {
		final InstantMessengerContactBuilder builder = new InstantMessengerContactBuilderImpl();
		builder.withAccount(ACCOUNT);
		builder.build();
	}
	
	@Test
	public final void login(){
		final InstantMessengerContactBuilder builder = new InstantMessengerContactBuilderImpl();
		Assert.assertFalse((boolean) ReflectionTestUtils.getField(builder, "login"));
		Assert.assertEquals(builder, builder.withLogin());
		Assert.assertTrue((boolean) ReflectionTestUtils.getField(builder, "login"));
	}

}
