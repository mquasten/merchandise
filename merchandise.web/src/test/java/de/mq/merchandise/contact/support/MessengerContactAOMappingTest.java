package de.mq.merchandise.contact.support;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import junit.framework.Assert;
import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.InstantMessenger;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.InstantMessengerContactImpl;
import de.mq.merchandise.model.ContactTestConstants;
import de.mq.merchandise.util.EntityUtil;

public class MessengerContactAOMappingTest {
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	private BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
	
	@Test
	public final void toDomain() {
		final LoginContact loginContact = EntityUtil.create(InstantMessengerContactImpl.class);
		final MessengerContactAO web = proxyFactory.createProxy(MessengerContactAO.class, new  ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(loginContact).build() );
		
		web.setId(ContactTestConstants.CONTACT_ID);
		web.setLoginContact(ContactTestConstants.USED_AS_LOGIN);
		web.setInstantMessenger(InstantMessenger.Skype.name());
		web.setAccount(ContactTestConstants.MESSENGER);
		
		Assert.assertEquals( web.getInstantMessenger()+ ": " + web.getAccount(), loginContact.contact());
		Assert.assertEquals(Long.valueOf(web.getId()).longValue(), loginContact.id());
		Assert.assertEquals(web.getLoginContact().booleanValue(), loginContact.isLogin());
		Assert.assertEquals(loginContact, web.getContact());
		
	}
	
	@Test
	public final void testtoWeb() {
		final LoginContact loginContact = new InstantMessengerContactBuilderImpl().withAccount(ContactTestConstants.MESSENGER).withProvider(InstantMessenger.Skype).withLogin().build();
		ReflectionTestUtils.setField(loginContact, "id", Long.valueOf(ContactTestConstants.CONTACT_ID));
		//ReflectionTestUtils.setField(loginContact, "isLogin", ContactTestConstants.USED_AS_LOGIN);
		//ReflectionTestUtils.setField(loginContact, "instantMessenger", InstantMessenger.Skype);
		//ReflectionTestUtils.setField(loginContact, "account", ContactTestConstants.MESSENGER);
		
		final MessengerContactAO result = proxyFactory.createProxy(MessengerContactAO.class,  new  ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(loginContact).build());
		
		Assert.assertEquals(""+ loginContact.id(), result.getId());
		Assert.assertEquals(loginContact.isLogin(), result.getLoginContact().booleanValue());
		Assert.assertEquals(ContactTestConstants.MESSENGER, result.getAccount());
		Assert.assertEquals(InstantMessenger.Skype.name(), result.getInstantMessenger());
		Assert.assertEquals(loginContact, result.getContact());
	}

}
