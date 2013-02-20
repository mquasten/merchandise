package de.mq.merchandise.contact.support;

import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.support.GeoLocationImpl;
import de.mq.merchandise.util.EntityUtil;

public class GeoLocationTest {
	
	private static final Long ID = 19680528L;
	private static final String COUNTRY = "DE";
	private static final String PLACE = "Rickelrath";
	private static final String ZIP_CODE = "41844";
	private static final String CITY = "Wegberg";
	private static final Coordinates COORDINATES=Mockito.mock(Coordinates.class);;

	@Test
	public final void geoLocation() {
		final CityAddress geoLocation = EntityUtil.create(GeoLocationImpl.class);
		ReflectionTestUtils.setField(geoLocation, "city", CITY);
		ReflectionTestUtils.setField(geoLocation, "zipCode", ZIP_CODE);
		ReflectionTestUtils.setField(geoLocation, "place", PLACE);
		ReflectionTestUtils.setField(geoLocation, "country", COUNTRY);
		ReflectionTestUtils.setField(geoLocation, "coordinates", COORDINATES);
		ReflectionTestUtils.setField(geoLocation, "id", ID);
		
		Assert.assertEquals((long) ID, geoLocation.id());
		Assert.assertEquals(CITY, geoLocation.city());
		Assert.assertEquals(ZIP_CODE, geoLocation.zipCode());
		Assert.assertEquals(Locale.GERMANY, geoLocation.country());
		
		Assert.assertEquals(PLACE, ((GeoLocationImpl) geoLocation).place());
		Assert.assertEquals(COORDINATES, ((GeoLocationImpl) geoLocation).coordinates());
		Assert.assertEquals(PLACE +" "+ZIP_CODE+" " + CITY + " " + COUNTRY , geoLocation.contact());
		
		
	}
	
	@Test
	public final void  equals() {
		final CityAddress cityAddress =  EntityUtil.create(GeoLocationImpl.class);
		Assert.assertTrue(cityAddress.equals(cityAddress));
		Assert.assertFalse(cityAddress.equals(EntityUtil.create(GeoLocationImpl.class)));
		Assert.assertTrue(newGeoLocation(ID).equals(newGeoLocation(ID)));
		Assert.assertFalse(newGeoLocation(ID).equals(newGeoLocation(ID+1)));
	}
	
	@Test
	public final void hash() {
		final CityAddress cityAddress =  EntityUtil.create(GeoLocationImpl.class);
		Assert.assertEquals(System.identityHashCode(cityAddress), cityAddress.hashCode());
		Assert.assertEquals(ID.hashCode(), newGeoLocation(ID).hashCode());
	}
	
	@Test
	public final void hasId() {
		Assert.assertTrue(newGeoLocation(ID).hasId());
		Assert.assertFalse( EntityUtil.create(GeoLocationImpl.class).hasId());
		
	}

	private CityAddress newGeoLocation(final long id) {
		CityAddress cityAddress = EntityUtil.create(GeoLocationImpl.class);
		ReflectionTestUtils.setField(cityAddress, "id", id);
		return cityAddress;
	}

}
