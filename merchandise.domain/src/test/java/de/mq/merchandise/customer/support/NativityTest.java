package de.mq.merchandise.customer.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.support.NativityImpl;

import junit.framework.Assert;

public class NativityTest {
	
	private static final Date DATE = new GregorianCalendar(1968, 4, 28).getTime();
	private static final String PLACE = "Melborne";

	@Test
	public final void create(){
		final Nativity nativity = new NativityImpl(PLACE , DATE);
		Assert.assertEquals(PLACE, nativity.birthPlace());
		Assert.assertEquals(DATE, nativity.birthDate());
	}
	
	
	@Test
	public final void createInvalid() throws NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		final Nativity nativity = newInvalid();
		Assert.assertNull(nativity.birthPlace());
		Assert.assertNull(nativity.birthDate());
	}
	
	
	@Test
	public final void equals() {
		final Nativity nativity = newInvalid();
		Assert.assertTrue(nativity.equals(nativity));
		Assert.assertFalse(nativity.equals(newInvalid()));
		Assert.assertTrue(new NativityImpl(PLACE, DATE).equals(new NativityImpl(PLACE, DATE)));
		Assert.assertFalse(new NativityImpl(PLACE, DATE).equals(new NativityImpl("dontLetMeGetMe", DATE)));
		
		Assert.assertFalse(new NativityImpl(PLACE, new Date()).equals(new NativityImpl(PLACE, DATE)));
	}
	
	@Test
	public final void hash() {
		final Nativity nativity = newInvalid();
		Assert.assertEquals(System.identityHashCode(nativity), nativity.hashCode());
		Assert.assertEquals(PLACE.hashCode()+ DATE.hashCode(), new NativityImpl(PLACE, DATE).hashCode());
	}


	private Nativity newInvalid()  {
		try {
		final Constructor<? extends Nativity> constructor = NativityImpl.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		final Nativity nativity = constructor.newInstance();
		return nativity;
		} catch (final NoSuchMethodException|InstantiationException|IllegalAccessException|InvocationTargetException ex){
			throw new IllegalStateException(ex);
		}
	}

}
