package de.mq.merchandise.contact.support;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mq.merchandise.contact.Coordinates;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/geocodingRepository.xml"})
public class CoordinatesRepositoryIntegrationTest {
	
	@Autowired
	private CoordinatesRepository coordinatesRepository;
	
	@Test
	public final void  forCityAddress() throws InterruptedException {
		final Coordinates result = coordinatesRepository.forAddress(new AddressBuilderImpl().withCity("Wegberg").withZipCode("41844").withCountry(Locale.GERMANY).withHouseNumber("4").withStreet("Am Telt").withCoordinates(Mockito.mock(Coordinates.class)).build());
		
		
		Assert.assertTrue(Math.abs(6.2829833D - result.longitude()) < 1e-14 );
		Assert.assertTrue(Math.abs(51.166913D - result.latitude()) < 1e-14);
		
	}
	
	

}
