package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.GeocodingService;

public class GeocodingServiceTest {
	
	@Test
	public final void coordinates() {
		final CoordinatesRepository coordinatesRepository = Mockito.mock(CoordinatesRepository.class);
		
		final GeocodingService geocodingService = new GeocodingServiceImpl(coordinatesRepository);
		final CityAddress cityAddress = Mockito.mock(CityAddress.class);
		final Coordinates coordinates = Mockito.mock(Coordinates.class);
		Mockito.when(coordinatesRepository.forAddress(cityAddress)).thenReturn(coordinates);
		
		
		Assert.assertEquals(coordinates, geocodingService.coordinates(cityAddress));
	}

}
