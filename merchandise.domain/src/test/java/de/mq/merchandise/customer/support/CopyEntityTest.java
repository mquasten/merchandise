package de.mq.merchandise.customer.support;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import junit.framework.Assert;

import org.hamcrest.core.IsAnything;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.SystemPropertyUtils;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.util.EntityUtil;

public class CopyEntityTest {
	
	@Test
	public final void copy() {
	
		Customer kylie = newCustomer();
		
		
		final Customer kyliesCopy = EntityUtil.copy(kylie);
	
		modify(kyliesCopy);
		
		for(final Contact contact : kylie.person().contacts()) {
			Assert.assertFalse(contact.hasId());
		}

		Assert.assertFalse(kylie.person().hasId());
		Assert.assertFalse(kylie.hasId());
	    Assert.assertNotNull(((NaturalPerson)kylie.person()).nativity().birthPlace());
	
	}
	
	@Test
	public final void shallowCopy() {
		final Customer kylie = newCustomer();
		final Customer kyliesCopy = EntityUtil.create(CustomerImpl.class);
		ReflectionUtils.shallowCopyFieldState(kylie, kyliesCopy);
		
		modify(kyliesCopy);
		
		
		
		
		
		for(final Contact contact : kylie.person().contacts()) {
			Assert.assertTrue(contact.hasId());
		}

		Assert.assertTrue(kylie.person().hasId());
		Assert.assertFalse(kylie.hasId());
	    Assert.assertNull(((NaturalPerson)kylie.person()).nativity().birthPlace());
	
	}

	private void modify(final Customer kyliesCopy) {
		ReflectionTestUtils.setField(kyliesCopy.person(), "id", (long) (Math.random()* 1e12));
		ReflectionTestUtils.setField(kyliesCopy, "id", (long) (Math.random()* 1e12));
		ReflectionTestUtils.setField(((NaturalPerson)kyliesCopy.person()).nativity(), "birthPlace", null);
		for(final Contact contact : kyliesCopy.person().contacts()) {
			
			ReflectionTestUtils.setField(contact, "id", (long) (Math.random()* 1e12));
			
		}
	}
	
	
	private Customer newCustomer() {
		final Nativity nativity = new NativityImpl("Melborne", new GregorianCalendar(1968,5,28).getTime());
		final NaturalPerson person = new NaturalPersonImpl("Kylie Ann", "Minogue",nativity, Locale.UK);
		
	    final Coordinates coordinates = new ContactBuilderFactoryImpl().coordinatesBuilder().withLongitude(145).withLatitude(38).build();
		final Address address = new ContactBuilderFactoryImpl().addressBuilder().withCity("Melborne").withCountry(Locale.UK).withStreet("Strasse").withZipCode("12345").withCoordinates(coordinates).withHouseNumber("xy").build();
		person.assign(address);
		LoginContact login = new ContactBuilderFactoryImpl().eMailContactBuilder().withAccount("kinkyKylie@fever.net").build();
		person.assign(login);
		
		return  new CustomerImpl(person);
		
	}
	
	@Test
	public final void test () throws IOException, ClassNotFoundException {
    final ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	final MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resourcePatternResolver);

  
    final String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                               resolveBasePackage("de.mq.merchandise") + "/" + "**/*.class";
    final Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);
    for (Resource resource : resources) {
        if (!resource.isReadable()) {
        	continue;
        }
        final MetadataReader metadataReader = metadataReaderFactory.getMetadataReader(resource);
           
        final Class<?> clazz = Class.forName(metadataReader.getClassMetadata().getClassName());
       
        /*if( clazz.isInterface() && ! clazz.isAnnotation()) {
        	System.out.println(">>>" + clazz);
        } */
        if( ! clazz.isAnnotationPresent(Entity.class)&&! clazz.isAnnotationPresent(Embeddable.class) ){
        	continue;
        }
      
        if( Modifier.isAbstract( clazz.getModifiers() )){
        	printInterfaces(clazz);
        	continue;
        }
        System.out.println(clazz);
        printInterfaces( clazz);
      
    }
   
	}
	
	private void printInterfaces(Class<?>clazz) {
		for(Class x : clazz.getInterfaces()) {
        	System.out.println("\t" + x);
        	printInterfaces(x);
        }
	}
	
	private String resolveBasePackage(String basePackage) {
	    return ClassUtils.convertClassNameToResourcePath(SystemPropertyUtils.resolvePlaceholders(basePackage));
	}
}

