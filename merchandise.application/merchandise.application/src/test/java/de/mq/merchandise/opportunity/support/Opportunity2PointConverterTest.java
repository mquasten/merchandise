package de.mq.merchandise.opportunity.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Coordinates;

public class Opportunity2PointConverterTest {
	
	
	
	private final Converter<Opportunity, Map<Address,String>>  converter = new Opportunity2PointConverterImpl();
	
	// Gulag Magadan 
	private double  LATTITUDE = 59+  (double)34/60 ;
	private double LONGITUDE = 150+  (double) 48/60;
	
	@Test
	public final void convert() {
		final Opportunity opportunity = Mockito.mock(Opportunity.class);
		final Address address = Mockito.mock(Address.class);
		final Coordinates coordinates = Mockito.mock(Coordinates.class);
		Mockito.when(coordinates.latitude()).thenReturn(LATTITUDE);
		Mockito.when(coordinates.longitude()).thenReturn(LONGITUDE);
		Mockito.when(address.coordinates()).thenReturn(coordinates);
		Set<Address> addresses = new HashSet<>();
		addresses.add(address);
		Mockito.when(opportunity.addresses()).thenReturn(addresses);
		
		
		Map<Address,String> result = converter.convert(opportunity);
		Assert.assertEquals(1, result.size());
		Assert.assertEquals(address, result.keySet().iterator().next());
		Assert.assertEquals(String.format("POINT(%s %s)", LONGITUDE, LATTITUDE), result.values().iterator().next());
	}

}
