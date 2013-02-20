package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;

import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.PostBox;
import de.mq.merchandise.contact.support.PostBoxImpl;
import de.mq.merchandise.util.EntityUtil;

public class PostBoxAOMappingTest {
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	private final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
	
	@Test
	public final void toDomain() {
		final PostBoxImpl postBox = EntityUtil.create(PostBoxImpl.class);
		final PostBoxAO postBoxAO = proxyFactory.createProxy(PostBoxAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(postBox).build());
		postBoxAO.setCity(AddressTestConstants.CITY);
		postBoxAO.setZipCode(AddressTestConstants.ZIP_CODE);
		postBoxAO.setCountry(AddressTestConstants.COUNTRY.toString());
		postBoxAO.setId(AddressTestConstants.ADDRESS_ID);
		postBoxAO.setPostBox(AddressTestConstants.POSTBOX);
		
		
		Assert.assertEquals(Long.valueOf(postBoxAO.getId()).longValue(),postBox.id());
		Assert.assertEquals(postBoxAO.getCity(),postBox.city());
		Assert.assertEquals(postBoxAO.getZipCode(),postBox.zipCode());
		Assert.assertEquals(postBoxAO.getCountry(),postBox.country().toString());
		
		Assert.assertEquals(postBoxAO.getPostBox(),postBox.box());
		Assert.assertEquals(postBox, postBoxAO.getAddress());
	}
	
	@Test
	public final void toWeb() {
		
		final PostBox postBox =  new PostBoxAddressBuilderImpl().withBox(AddressTestConstants.POSTBOX).withCountry(AddressTestConstants.COUNTRY).withZipCode(AddressTestConstants.ZIP_CODE).withCity(AddressTestConstants.CITY).build();
		ReflectionTestUtils.setField(postBox, "id", Long.valueOf(AddressTestConstants.ADDRESS_ID));
		//ReflectionTestUtils.setField(postBox, "city", AddressTestConstants.CITY);
		//ReflectionTestUtils.setField(postBox, "zipCode", AddressTestConstants.ZIP_CODE);
		//ReflectionTestUtils.setField(postBox, "country", AddressTestConstants.COUNTRY);
		//ReflectionTestUtils.setField(postBox, "postBox", AddressTestConstants.POSTBOX);
		
		final PostBoxAO result = proxyFactory.createProxy(PostBoxAO.class,new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(postBox).build());
		
		Assert.assertEquals(postBox.id(), Long.valueOf(result.getId()).longValue());
		Assert.assertEquals(postBox.city(), result.getCity());
		Assert.assertEquals(postBox.zipCode(), result.getZipCode());
		Assert.assertEquals(postBox.country().toString(),result.getCountry());
		
		Assert.assertEquals(postBox.box(), result.getPostBox());
		
		
		
	}

}
