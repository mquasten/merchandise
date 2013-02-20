package de.mq.merchandise.contact.support;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import junit.framework.Assert;
import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.mapping.util.proxy.support.SimpleReflectionBeanResolverImpl;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.EMailContactImpl;
import de.mq.merchandise.model.ContactTestConstants;
import de.mq.merchandise.util.EntityUtil;

public class EMailContactAOMappingTest {
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	private final BeanResolver beanResolver = new SimpleReflectionBeanResolverImpl();
	
	@Test
	public final void toDomain() {
		final LoginContact loginContact = EntityUtil.create(EMailContactImpl.class);
		// new ModelRepositoryImpl(loginContact)
		final EMailContactAO web = proxyFactory.createProxy(EMailContactAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(loginContact).build());
		web.setId(ContactTestConstants.CONTACT_ID);
		web.setLoginContact(ContactTestConstants.USED_AS_LOGIN);
		web.setAccount(ContactTestConstants.EMAIL);
		
		Assert.assertEquals(Long.valueOf(web.getId()).longValue(), loginContact.id());
		Assert.assertEquals(web.getLoginContact().booleanValue(), loginContact.isLogin());
		Assert.assertEquals(web.getAccount(), loginContact.contact());
		Assert.assertEquals(loginContact, web.getContact());
	}
	
	@Test
	public final void toWeb() {
		final LoginContact loginContact = new EMailContactBuilderImpl().withAccount(ContactTestConstants.EMAIL).withLogin().build();
		//ReflectionTestUtils.setField(loginContact, "isLogin", ContactTestConstants.USED_AS_LOGIN);
		ReflectionTestUtils.setField(loginContact, "id", Long.valueOf(ContactTestConstants.CONTACT_ID));
		//ReflectionTestUtils.setField(loginContact, "account", ContactTestConstants.EMAIL);
		
		final EMailContactAO web = proxyFactory.createProxy(EMailContactAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(beanResolver).withDomain(loginContact).build());
		
		Assert.assertEquals(loginContact.isLogin(), web.getLoginContact().booleanValue());
		
		Assert.assertEquals(loginContact.id(), Long.valueOf(web.getId()).longValue());
		Assert.assertEquals(loginContact.contact(), web.getAccount());
		Assert.assertEquals(loginContact, web.getContact());
		
	}

}
