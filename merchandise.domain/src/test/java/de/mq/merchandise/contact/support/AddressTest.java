package de.mq.merchandise.contact.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import junit.framework.Assert;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.PostBox;
import de.mq.merchandise.contact.support.AbstractCityAddress;
import de.mq.merchandise.contact.support.AddressImpl;
import de.mq.merchandise.contact.support.PostBoxImpl;

public class AddressTest {
	
	private static final String HOUSE_NUMBER = "4";
	private static final String STREET = "Am Telt";
	private static final String POST_BOX = "4711";
	private static final long ID = 19680528L;
	private static final String CITY = "Wegberg";
	private static final String ZIP_CODE = "41844";
	private static final Coordinates COORDINATES = Mockito.mock(Coordinates.class);



	@Test
	public final void cityAddress() {
		final CityAddress cityAddress= new AddressMock(ZIP_CODE, CITY, ID);
		Assert.assertEquals(CITY, cityAddress.city());
		Assert.assertEquals(ZIP_CODE, cityAddress.zipCode());
		Assert.assertEquals(ID, cityAddress.id());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void cityAddressNotPersostent() {
		new AddressMock().id();
	}
	
	@Test
	public final void postBox() {
		final PostBox postBox = new PostBoxImpl(Locale.GERMANY, ZIP_CODE, CITY, POST_BOX );
		Assert.assertEquals(ZIP_CODE, postBox.zipCode());
		Assert.assertEquals(CITY, postBox.city());
		Assert.assertEquals(POST_BOX, postBox.box());
		Assert.assertEquals(POST_BOX+", " + ZIP_CODE + " " +CITY + " DE", postBox.contact());
		Assert.assertEquals(Locale.GERMANY, postBox.country());
	}
	
	@Test
	public final void postBoxDefaultConstructor() throws NoSuchMethodException, SecurityException {
		final PostBox postBox= newInvalidPostBox();
		Assert.assertNull(postBox.city());
		Assert.assertNull(postBox.zipCode());
		Assert.assertNull(postBox.box());
		Assert.assertNull(postBox.country());
	}
	
	@Test
	public final void address() {
		final Address address = new AddressImpl(Locale.GERMANY, ZIP_CODE, CITY, STREET, HOUSE_NUMBER,COORDINATES);
		Assert.assertEquals(ZIP_CODE, address.zipCode());
		Assert.assertEquals(CITY, address.city());
		Assert.assertEquals(STREET, address.street());
		Assert.assertEquals(HOUSE_NUMBER, address.houseNumber());
		Assert.assertEquals(STREET+ " " + HOUSE_NUMBER + ", "+ ZIP_CODE + " " + CITY + ", DE", address.contact());
		Assert.assertEquals(Locale.GERMANY, address.country());
		Assert.assertEquals(COORDINATES, address.coordinates());
	}
	
	@Test
	public final void invalidAddress() {
		final Address address = newInvalidAddress();
		Assert.assertNull(address.city());
		Assert.assertNull(address.street());
		Assert.assertNull(address.zipCode());
		Assert.assertNull(address.houseNumber());
	}
	
	@Test
	public final void equalsPostBox() {
        final PostBox bostBox= newInvalidPostBox();
		Assert.assertFalse(bostBox.equals(newInvalidPostBox()));
		Assert.assertTrue(bostBox.equals(bostBox));
		Assert.assertTrue(new PostBoxImpl(Locale.GERMANY, ZIP_CODE, CITY, POST_BOX).equals(new PostBoxImpl(Locale.GERMANY,ZIP_CODE, CITY, POST_BOX)));
		Assert.assertFalse(new PostBoxImpl(Locale.GERMANY, ZIP_CODE, "dontLetMeGetMe", POST_BOX).equals(new PostBoxImpl(Locale.GERMANY, ZIP_CODE, CITY, POST_BOX)));
		
	}
	
	@Test
	public final void hashPostBox() {
		
		Assert.assertEquals(Locale.GERMANY.toString().hashCode()+CITY.hashCode()+POST_BOX.hashCode(), new PostBoxImpl(Locale.GERMANY, ZIP_CODE, CITY, POST_BOX).hashCode());
	}
	
	@Test
	public final void assignCoordinates() {
		final Address address = new AddressImpl(Locale.GERMANY, ZIP_CODE, CITY, STREET, HOUSE_NUMBER, null);
		Assert.assertNull(address.coordinates());
		final Coordinates coordinates  = Mockito.mock(Coordinates.class);
		address.assign(coordinates);
		Assert.assertEquals(coordinates, address.coordinates());
	}
	
	@Test
	public final void equalsAddress() {
		final Address address= newInvalidAddress();
		Assert.assertFalse(address.equals(newInvalidAddress()));
		Assert.assertTrue(address.equals(address));
		Assert.assertTrue(new AddressImpl(Locale.GERMANY, ZIP_CODE, CITY, STREET, HOUSE_NUMBER,COORDINATES).equals(new AddressImpl(Locale.GERMANY, ZIP_CODE, CITY, STREET, HOUSE_NUMBER,COORDINATES)));
		Assert.assertFalse(new AddressImpl(Locale.GERMANY, ZIP_CODE, CITY, "dontLetMeGetMe", HOUSE_NUMBER, COORDINATES).equals(new AddressImpl(Locale.GERMANY, ZIP_CODE, CITY, STREET, HOUSE_NUMBER,COORDINATES)));
	}
	
	@Test
	public final void hasId() {
		final AddressMock address = new AddressMock(ZIP_CODE, CITY, ID);
		Assert.assertTrue(address.hasId());
		
		ReflectionTestUtils.setField(address, "id", null);
		Assert.assertFalse(address.hasId());
		
	}

	private PostBox newInvalidPostBox()  {
		
		try {
			Constructor<? extends PostBox> constructor = PostBoxImpl.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (final NoSuchMethodException | SecurityException| InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException ex) {
			// TODO Auto-generated catch block
			throw new IllegalStateException(ex);
		} 
		
	}
	
	private Address newInvalidAddress()  {
		
		try {
			Constructor<? extends Address> constructor = AddressImpl.class.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (final NoSuchMethodException | SecurityException| InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException ex) {
			// TODO Auto-generated catch block
			throw new IllegalStateException(ex);
		} 
		
	}
	
	class AddressMock extends AbstractCityAddress {

		private static final long serialVersionUID = 5076616552868133560L;
		AddressMock(final String zipCode, final String city, final long id) {
			super(Locale.GERMANY, zipCode, city);
			this.id=id;
		}
		AddressMock(){
			super();
		}
		@Override
		protected String contactInfo() {
			return zipCode +" "+ city;
		}
		
		
		
		
	}

}
