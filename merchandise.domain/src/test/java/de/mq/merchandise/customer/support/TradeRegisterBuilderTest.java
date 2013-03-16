package de.mq.merchandise.customer.support;

import java.util.GregorianCalendar;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.customer.TradeRegisterBuilder;
import de.mq.merchandise.customer.support.TradeRegisterBuilderImpl;

public class TradeRegisterBuilderTest {
	
	private static final String ZIP_CODE = "41844";
	private static final String CITY = "Wegberg";
	private static final String REFERENCE = "0814";

	@Test
	public final void withReference() {
		final TradeRegisterBuilder builder = new TradeRegisterBuilderImpl();
		Assert.assertEquals(builder, builder.withReference(REFERENCE));
	}
	
	@Test
	public final void withCity() {
		final TradeRegisterBuilder builder = new TradeRegisterBuilderImpl();
		Assert.assertEquals(builder, builder.withCity(CITY));
	}
	
	@Test
	public final void withZip() {
		final TradeRegisterBuilder builder = new TradeRegisterBuilderImpl();
		Assert.assertEquals(builder, builder.withZipCode(ZIP_CODE));
	}
	
	
	
	
	@Test
	public final void build() {
		final TradeRegister tradeRegister = new TradeRegisterBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).withReference(REFERENCE).build();
		Assert.assertEquals(CITY, tradeRegister.city());
		Assert.assertEquals(ZIP_CODE, tradeRegister.zipCode());
		Assert.assertEquals(REFERENCE, tradeRegister.reference());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void missingCity(){
		new TradeRegisterBuilderImpl().withZipCode(ZIP_CODE).withReference(REFERENCE).build();
	}
	

	@Test(expected=IllegalArgumentException.class)
	public final void missingZip(){
		new TradeRegisterBuilderImpl().withCity(CITY).withReference(REFERENCE).build();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void missingReference(){
		new TradeRegisterBuilderImpl().withCity(CITY).withZipCode(ZIP_CODE).build();
	}
	
	
}
