package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.support.CoordinatesImpl;
import de.mq.merchandise.util.EntityUtil;

public class CoordinatesTest {
	
	private static final Double LONGITUDE = 6d;
	private static final Double LATITUDE = 54d;

	@Test
	public final void create() {
		final Coordinates coordinates = new CoordinatesImpl(LATITUDE, LONGITUDE);
		Assert.assertEquals(LATITUDE, coordinates.latitude());
		Assert.assertEquals(LONGITUDE, coordinates.longitude());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void invalidLongitude() {
		EntityUtil.create(CoordinatesImpl.class).longitude();
	}
	@Test(expected=IllegalArgumentException.class)
	public final void invalidLatitude() {
		
		EntityUtil.create(CoordinatesImpl.class).latitude();
	}
	
	@Test
	public final void equals() {
		final Coordinates coordinates = EntityUtil.create(CoordinatesImpl.class);
		Assert.assertTrue(coordinates.equals(coordinates));
		Assert.assertFalse(coordinates.equals(EntityUtil.create(CoordinatesImpl.class)));
		Assert.assertTrue(new CoordinatesImpl(LATITUDE, LONGITUDE).equals(new CoordinatesImpl(LATITUDE, LONGITUDE)));
		Assert.assertFalse(new CoordinatesImpl(-1, LONGITUDE).equals(new CoordinatesImpl(LATITUDE, LONGITUDE)));
		Assert.assertFalse(new CoordinatesImpl(LATITUDE, LONGITUDE).equals(new CoordinatesImpl(LATITUDE, -1)));
	}
	
	@Test
	public final void hash() {
		final CoordinatesImpl coordinates = EntityUtil.create(CoordinatesImpl.class);
		Assert.assertEquals(System.identityHashCode(coordinates), coordinates.hashCode());
		Assert.assertEquals(LONGITUDE.hashCode()+ LATITUDE.hashCode(), new CoordinatesImpl(LATITUDE, LONGITUDE).hashCode());
	}
	
	@Test
	public final void distance() {
		final Coordinates coordinates = Mockito.mock(Coordinates.class);
		Mockito.when(coordinates.latitude()).thenReturn(toDecimal(51,22,30));
		Mockito.when(coordinates.longitude()).thenReturn(toDecimal(6,24,05));
	    Assert.assertEquals(13, Math.round(new CoordinatesImpl(toDecimal(51, 19,2), toDecimal(6,34,16)).distance(coordinates)));
	}
	
	private double toDecimal(double ... degrees){
		double result=0;
		int i=0;
		for(double x : degrees){
			result+=x*Math.pow(60, (-1) * i);
			i++;
		}
		
		return result;
	}

}
