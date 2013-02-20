package de.mq.merchandise.contact.support;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.PhoneContactBuilder;
import de.mq.merchandise.contact.support.PhoneContactBuilderImpl;

import junit.framework.Assert;


public class PhoneContactBuilderTest {
	
	private static final String COUNTRY_CODE = "1";
	private static final String AREA_CODE = "211";
	private static final String SUBCRIBER_NUMBER = "123456";

	@Test
	public final void phoneContact() {
		final PhoneContactBuilder builder = new PhoneContactBuilderImpl();
		Assert.assertEquals(builder, builder.withCountryCode(COUNTRY_CODE));
		Assert.assertEquals(builder, builder.withAreaCode(AREA_CODE));
		Assert.assertEquals(builder, builder.withSubscriberNumber(SUBCRIBER_NUMBER));
		Assert.assertEquals(COUNTRY_CODE + AREA_CODE + SUBCRIBER_NUMBER, builder.build().contact());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void phoneContactWithoutCountryCode() {
		final PhoneContactBuilder builder = new PhoneContactBuilderImpl();
		builder.withSubscriberNumber(SUBCRIBER_NUMBER);
		builder.withCountryCode(null);
		builder.build();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void phoneContactWithoutSubscriberNumber() {
		final PhoneContactBuilder builder = new PhoneContactBuilderImpl();
		
		builder.build();
	}
	
	@Test
	public final void phoneContactWithoutAreaCode() {
		final PhoneContactBuilder builder = new PhoneContactBuilderImpl().withSubscriberNumber(SUBCRIBER_NUMBER);
		Assert.assertEquals("49123456", builder.build().contact());
	}
	
	@Test
	public final void login() {
		final PhoneContactBuilder builder = new PhoneContactBuilderImpl();
		Assert.assertFalse((boolean) ReflectionTestUtils.getField(builder, "login"));
		Assert.assertEquals(builder, builder.withLogin());
		Assert.assertTrue((boolean) ReflectionTestUtils.getField(builder, "login"));
	}

}
