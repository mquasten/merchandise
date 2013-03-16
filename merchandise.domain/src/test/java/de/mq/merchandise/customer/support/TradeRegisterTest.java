package de.mq.merchandise.customer.support;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.customer.support.TradeRegisterImpl;

public class TradeRegisterTest {
	
	private static final Date REGISTRATION_DATE = new GregorianCalendar(1968, 4, 28).getTime();
	private static final String REFERENCE = "Aktenzeichen";
	private static final String CITY = "Wegberg";
	private static final String ZIPCODE = "41844";

	@Test
	public final void create() {
		final TradeRegister tradeRegister = new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE);
		Assert.assertEquals(ZIPCODE, tradeRegister.zipCode());
		Assert.assertEquals(CITY, tradeRegister.city());
		Assert.assertEquals(REFERENCE, tradeRegister.reference());
	}
	
	@Test
	public final void createInvalid() {
		final TradeRegister tradeRegister = newInvalidTradeRegister();
		Assert.assertNull(tradeRegister.zipCode());
		Assert.assertNull(tradeRegister.city());
		Assert.assertNull(tradeRegister.reference());
	}

	private TradeRegister newInvalidTradeRegister()  {
		try {
			final Constructor<TradeRegisterImpl> constructor = TradeRegisterImpl.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return  constructor.newInstance();
		} catch ( final Exception ex){
			throw new IllegalStateException(ex);
		}
		
	}
	
	@Test
	public final void hash() {
		Assert.assertEquals(ZIPCODE.hashCode() + REFERENCE.hashCode(), new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE).hashCode());
		Assert.assertFalse(new TradeRegisterImpl(ZIPCODE, CITY, null).hashCode() == new TradeRegisterImpl(ZIPCODE, CITY, null).hashCode());
		Assert.assertFalse(new TradeRegisterImpl(null, CITY, REFERENCE).hashCode() == new TradeRegisterImpl(null, CITY, REFERENCE).hashCode());
	}
	@Test
	public final void equals() {
		Assert.assertTrue(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE).equals(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE)));
		Assert.assertFalse(new TradeRegisterImpl(ZIPCODE, CITY, "DontLetMeGetMe").equals(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE)));
		Assert.assertFalse(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE).equals(new TradeRegisterImpl(ZIPCODE, CITY, "DontLetMeGetMe")));
		Assert.assertFalse(new TradeRegisterImpl("DontLetMeGetMe", CITY, REFERENCE).equals(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE)));
		Assert.assertFalse(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE).equals(new TradeRegisterImpl("DontLetMeGetMe", CITY, REFERENCE)));
		Assert.assertFalse(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE).equals(new TradeRegisterImpl(null, CITY, REFERENCE)));
		Assert.assertFalse(new TradeRegisterImpl(ZIPCODE, CITY, null).equals(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE)));
		Assert.assertFalse(new TradeRegisterImpl(ZIPCODE, CITY, REFERENCE).equals("Kylie is nice"));
	}
	
	

}
