package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;

import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.support.AddressImpl;
import de.mq.merchandise.util.EntityUtil;


public class AddressMappingTest {
	
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	private BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
	@Test
	public final void toDomain() {
		final Address address = EntityUtil.create(AddressImpl.class);
		// new ModelRepositoryImpl(address)
		final AddressAO addressAO = proxyFactory.createProxy(AddressAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(address).build());
		addressAO.setCity(AddressTestConstants.CITY);
		addressAO.setCountry(AddressTestConstants.COUNTRY.toString());
		addressAO.setHouseNumber(AddressTestConstants.HOUSE_NUMBER);
		addressAO.setStreet(AddressTestConstants.STREET);
		addressAO.setId(AddressTestConstants.ADDRESS_ID);
		
		
		
		Assert.assertEquals(AddressTestConstants.ADDRESS_ID, addressAO.getId());
		Assert.assertEquals(AddressTestConstants.COUNTRY.toString(), addressAO.getCountry());
		Assert.assertEquals(AddressTestConstants.CITY, addressAO.getCity());
		Assert.assertEquals(AddressTestConstants.STREET, addressAO.getStreet());
		Assert.assertEquals(AddressTestConstants.HOUSE_NUMBER, addressAO.getHouseNumber());
	}
	
	@Test
	public final void toWeb() {
		final Address address = new AddressBuilderImpl().withCity( AddressTestConstants.CITY).withStreet(AddressTestConstants.STREET).withZipCode(AddressTestConstants.ZIP_CODE).withHouseNumber(AddressTestConstants.HOUSE_NUMBER).withCoordinates(Mockito.mock(Coordinates.class)).build();
		ReflectionTestUtils.setField(address, "id", Long.parseLong(AddressTestConstants.ADDRESS_ID));	
		final AddressAO addressAO = proxyFactory.createProxy(AddressAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(address).build());
		
		Assert.assertEquals(AddressTestConstants.ADDRESS_ID, addressAO.getId());
		Assert.assertEquals(AddressTestConstants.CITY, addressAO.getCity());
		Assert.assertEquals(AddressTestConstants.STREET, addressAO.getStreet());
		Assert.assertEquals(AddressTestConstants.HOUSE_NUMBER, addressAO.getHouseNumber());
		Assert.assertEquals(AddressTestConstants.COUNTRY.toString(), addressAO.getCountry());
		
		Assert.assertEquals(address, addressAO.getAddress());
	}
	

}
