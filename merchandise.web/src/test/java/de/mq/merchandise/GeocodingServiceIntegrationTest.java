package de.mq.merchandise;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.GeocodingService;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:/geocoding.xml"})
public class GeocodingServiceIntegrationTest {
	
	@Autowired
	private GeocodingService geocodingService;
	
	@Test
	public final void geocoding() {
		Assert.assertNotNull(geocodingService);
		final CityAddress cityAddress = new ContactBuilderFactoryImpl().addressBuilder().withCity("Volgograd").withZipCode("400001").withStreet("ulitsa kim").withHouseNumber("13").withCoordinates(Mockito.mock(Coordinates.class)).withCountry(new Locale("ru", "RU")).build();
		final Coordinates result = geocodingService.coordinates(cityAddress);
		Assert.assertEquals(44, Math.round(result.longitude()));
		Assert.assertEquals(49, Math.round(result.latitude()));
	}
	
	
}
