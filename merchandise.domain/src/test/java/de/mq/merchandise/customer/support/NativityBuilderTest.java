package de.mq.merchandise.customer.support;

import java.util.Date;
import java.util.GregorianCalendar;

import org.junit.Test;

import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NativityBuilder;
import de.mq.merchandise.customer.support.NativityBuilderImpl;

import junit.framework.Assert;

public class NativityBuilderTest {
	
	private static final String PLACE = "Melborne";
	private static final Date DATE = new GregorianCalendar(1968, 4 , 28).getTime();

	@Test
	public final void birthDate() {
		 final NativityBuilder builder = new NativityBuilderImpl();
		 Assert.assertEquals(builder, builder.withBirthDate(DATE));
	}
	
	@Test
	public final void birthPlace() {
		 final NativityBuilder builder = new NativityBuilderImpl();
		 Assert.assertEquals(builder, builder.withBirthPlace(PLACE));
	}
	
	@Test
	public final void build() {
		final Nativity nativity = new NativityBuilderImpl().withBirthDate(DATE).withBirthPlace(PLACE).build();
		Assert.assertEquals(DATE, nativity.birthDate());
		Assert.assertEquals(PLACE, nativity.birthPlace());
	}
	
	
	
	

}
