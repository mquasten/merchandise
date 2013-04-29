package de.mq.merchandise.util.support;

import junit.framework.Assert;

import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import de.mq.merchandise.customer.Person;
import de.mq.merchandise.util.support.HibernateProxyConverter;

public class HibernateProxyConverterTest {
	
	private final Converter<Object,Object> hibernateProxyConverter = new HibernateProxyConverter();
	
	@Test
	public final void nonProxy() {
		
		final Person person = Mockito.mock(Person.class);
		Assert.assertEquals(person, hibernateProxyConverter.convert(person));
	}
	
	@Test
	public final void proxy() {
		final Person person = Mockito.mock(Person.class);
		final HibernateProxy hibernateProxy = Mockito.mock(HibernateProxy.class);
		final LazyInitializer lazyInitializer = Mockito.mock(LazyInitializer.class);
		Mockito.when(lazyInitializer.getImplementation()).thenReturn(person);
		Mockito.when(hibernateProxy.getHibernateLazyInitializer()).thenReturn(lazyInitializer);
		Assert.assertEquals(person, hibernateProxyConverter.convert(hibernateProxy));
	}

}
