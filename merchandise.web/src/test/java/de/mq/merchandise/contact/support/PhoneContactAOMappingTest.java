package de.mq.merchandise.contact.support;

import junit.framework.Assert;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.PhoneContactImpl;
import de.mq.merchandise.model.ContactTestConstants;
import de.mq.merchandise.util.EntityUtil;

public class PhoneContactAOMappingTest {
	
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	private BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
	@Test
	public final void toDomain() {
		final LoginContact phoneContact = EntityUtil.create(PhoneContactImpl.class);
		final PhoneContactAO web = proxyFactory.createProxy(PhoneContactAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(phoneContact).build());
		
		web.setId(ContactTestConstants.CONTACT_ID);
		web.setInternationalAreaCode(ContactTestConstants.INTERNATION_AREA_CODE);
		web.setAreaCode(ContactTestConstants.AREA_CODE);
		web.setNumber(ContactTestConstants.PHONE_NUMBER);
		web.setLoginContact(ContactTestConstants.USED_AS_LOGIN);
		
		Assert.assertEquals(Long.valueOf(web.getId()).longValue(), phoneContact.id());
		Assert.assertEquals(web.getInternationalAreaCode() + web.getAreaCode() + web.getNumber(), phoneContact.contact());
		Assert.assertEquals(web.getLoginContact().booleanValue(), phoneContact.isLogin());
		Assert.assertEquals(phoneContact, web.getContact());
	}
	
	@Test
	public final void toWeb() {
		final LoginContact loginContact = new PhoneContactBuilderImpl().withCountryCode( ContactTestConstants.INTERNATION_AREA_CODE).withAreaCode( ContactTestConstants.AREA_CODE ).withSubscriberNumber(ContactTestConstants.PHONE_NUMBER).withLogin().build();
		
		ReflectionTestUtils.setField(loginContact, "id", Long.valueOf(ContactTestConstants.CONTACT_ID));
		final PhoneContactAO result = proxyFactory.createProxy(PhoneContactAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(loginContact).build());
		
		
		Assert.assertEquals( ContactTestConstants.INTERNATION_AREA_CODE , result.getInternationalAreaCode());
		Assert.assertEquals( ContactTestConstants.AREA_CODE , result.getAreaCode());
		Assert.assertEquals( ContactTestConstants.PHONE_NUMBER , result.getNumber());
		Assert.assertEquals( loginContact.isLogin() , result.getLoginContact().booleanValue());
		Assert.assertEquals(""+ loginContact.id(), result.getId());
		Assert.assertEquals(loginContact, result.getContact());
	}
	
	

}
