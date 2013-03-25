package de.mq.merchandise.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.EqualsBuilder;
import de.mq.merchandise.util.support.SimpleReflectionEqualsBuilderImpl;


public class EntityUtilTest {
	
	@Before
	@After
	public final void setup() {
		setEqualsBuilderClass(SimpleReflectionEqualsBuilderImpl.class);
	}
	
	@Test
	public final void idAware() {
		EntityUtil.idAware(4711L);
	}
	
	@Test(expected=IllegalStateException.class)
	public final void idAwareNull() {
		EntityUtil.idAware(null);
	}
	
	@Test
	public final void mandatoryGuard(){
		EntityUtil.mandatoryGuard("kylie", "artist");
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public final void mandatoryGuardNull(){
		EntityUtil.mandatoryGuard(null, "artist");
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public final void mandatoryGuardEmpty(){
		EntityUtil.mandatoryGuard(" ", "artist");
	}
	
	@Test
	public final void notNullGuard() {
		EntityUtil.notNullGuard(new Date(), "date");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void notNullGuardNotOk() {
		EntityUtil.notNullGuard(null, "date");
	}
	
	@Test
	public final void equalsBuilder() {
		Assert.assertEquals(SimpleReflectionEqualsBuilderImpl.class, EntityUtil.equalsBuilder().getClass());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void equalsBilderWrongClass()  {
		setEqualsBuilderClass(EqualsBuilder.class);
		EntityUtil.equalsBuilder();
	}
	
	@Test
	public final void locale(){
		Assert.assertNull(EntityUtil.locale(null));
		Assert.assertNull(EntityUtil.locale(" "));
		Assert.assertEquals(Locale.GERMANY, EntityUtil.locale("de_DE"));
		Assert.assertEquals(Locale.GERMAN, EntityUtil.locale("de"));
		Assert.assertEquals(new Locale("de", "DE", "xxx"), EntityUtil.locale("de_DE_xxx"));
		
		
	}
	
	@Test
	public final void  coverage() {
		class EntityUtilMock extends EntityUtil {
			
		}
		
		Assert.assertNotNull(new EntityUtilMock());
	}

	private void setEqualsBuilderClass(final Class<? extends EqualsBuilder> clazz) {
		final Field field = ReflectionUtils.findField(EntityUtil.class, "EQUALSBUILDER_CLASS");
		field.setAccessible(true);
		/* dirrty, nasty, ugly ... */
		final Field modifiersField =  ReflectionUtils.findField(Field.class, "modifiers");
	    modifiersField.setAccessible(true);
	    ReflectionUtils.setField(modifiersField, field, field.getModifiers() & ~Modifier.FINAL);
		ReflectionUtils.setField(field, null, clazz);
	}
	
	@Test
	public final void builder() {
		Assert.assertEquals(Date.class, EntityUtil.create(Date.class).getClass());
	}
	
	@Test(expected=IllegalStateException.class)
	public final void buildeCantCreater() {
		Assert.assertEquals(Date.class, EntityUtil.create(Runnable.class).getClass());
	}
	
	@Test
	public final void  copy() {
		final Address address = newAddressWithValues();
		
		
		final Address target = EntityUtil.copy(address);
		Assert.assertEquals(address.id(), target.id());
		Assert.assertEquals(address.city(),target.city());
		Assert.assertEquals(address.street(), target.street());
		Assert.assertEquals(address.houseNumber(), target.houseNumber());
		Assert.assertEquals(address.country(), target.country());
	}
	
	
	@Test(expected=IllegalStateException.class)
	public final void copyNotSerializeable() {
		final Object source =   new Date() {
			private void writeObject( ObjectOutputStream oos ) throws IOException {
				throw new IOException();
			}
			private static final long serialVersionUID = 1L;
			
		};
		EntityUtil.copy(source);
		
	}
	
	@Test(expected=IllegalStateException.class)
	public final void copyErrorReadObjectBack() {
		final Object source =   new Date() {
			private static final long serialVersionUID = 1L;
			private void readObject( ObjectInputStream ois ) throws IOException {
				throw new IOException();
			}
		};
			
		
		
		EntityUtil.copy(source);
	
	
	
		
	
	}
	
	
	
	

	private Address newAddressWithValues() {
		final Address address = new ContactBuilderFactoryImpl().addressBuilder().withCity("Wegberg").withStreet("Am Telt").withHouseNumber("4").withZipCode("41844").withCountry(Locale.GERMANY).withCoordinates(Mockito.mock(Coordinates.class)).build();
		ReflectionTestUtils.setField(address, "id", 19680528L);
		//ReflectionTestUtils.setField(address, "city", "Wegberg");
		//ReflectionTestUtils.setField(address, "street", "Am Telt");
		//ReflectionTestUtils.setField(address, "houseNumber", "4");
		//ReflectionTestUtils.setField(address, "country", "de_DE");
		return address;
	}
	
	@Test
	public final void  setFieldsToNull() {
		final Address address = newAddressWithValues();
		Assert.assertNotNull(address.id());
		Assert.assertNotNull(address.city());
		Assert.assertNotNull(address.street());
		Assert.assertNotNull(address.houseNumber());
		Assert.assertNotNull(address.country());
		EntityUtil.setFieldsToNull(address);
		
		Assert.assertNull(address.city());
		Assert.assertNull(address.street());
		Assert.assertNull(address.houseNumber());
		Assert.assertNull(address.country());
		
		try {
			address.id();
			Assert.fail("IllegalStateException expected, id nor set");
		} catch ( IllegalStateException ex ){
			
		}
	}
	
	@Test
	public final void setId() {
		final Contact contact = new ContactBuilderFactoryImpl().eMailContactBuilder().withAccount("kylie.minogue@hotArtists.net").build();
		
		final Long id = 19680528L;
		Assert.assertNull(ReflectionTestUtils.getField(contact, "id"));
		EntityUtil.setId(contact, id);
		
		Assert.assertEquals(id, ReflectionTestUtils.getField(contact, "id"));
		
	}
	

}
