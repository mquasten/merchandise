package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.CoordinatesBuilder;
import de.mq.merchandise.contact.support.CoordinatesBuilderImpl;

public class CoordinatesBuilderTest {
	
	private static final double LONGITUDE = 6D;
	private static final double LATITUDE = 54D;

	@Test
	public final void longitude() {
		final CoordinatesBuilder coordinatesBuilder = new CoordinatesBuilderImpl();
		Assert.assertEquals(coordinatesBuilder, coordinatesBuilder.withLongitude(LONGITUDE));
	}
	@Test
	public final void latitude() {
		final CoordinatesBuilder coordinatesBuilder = new CoordinatesBuilderImpl();
		Assert.assertEquals(coordinatesBuilder, coordinatesBuilder.withLongitude(LATITUDE));
	}
	
	@Test
	public final void build() {
		final Coordinates coordinates = new CoordinatesBuilderImpl().withLatitude(LATITUDE).withLongitude(LONGITUDE).build();
		Assert.assertEquals(LATITUDE, coordinates.latitude());
		Assert.assertEquals(LONGITUDE, coordinates.longitude());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void missingLongitude() {
		 new CoordinatesBuilderImpl().withLatitude(LATITUDE).build();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void missingLatitude() {
		 new CoordinatesBuilderImpl().withLatitude(LONGITUDE).build();
	}

}
