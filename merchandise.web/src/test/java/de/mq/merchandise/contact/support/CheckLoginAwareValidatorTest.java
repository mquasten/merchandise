package de.mq.merchandise.contact.support;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidatorContext;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.BeanResolver;
import de.mq.mapping.util.proxy.support.BeanConventionCGLIBProxyFactory;
import de.mq.mapping.util.proxy.support.ModelRepositoryBuilderImpl;
import de.mq.merchandise.model.ContactTestConstants;

public class CheckLoginAwareValidatorTest {
	
	private final AOProxyFactory proxyFactory = new  BeanConventionCGLIBProxyFactory();
	
	@Test
	public final void validateIsLogin() {
		final List<EMailContactAO> contacts = new ArrayList<>();
		contacts.add(proxyFactory.createProxy(EMailContactAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(Mockito.mock(BeanResolver.class)).withDomain(new EMailContactBuilderImpl().withAccount(ContactTestConstants.EMAIL).withLogin().build()).build()));
		Assert.assertTrue(new CheckLoginAwareValidator().isValid(contacts, Mockito.mock(ConstraintValidatorContext.class)));
	
	}
	
	@Test
	public final void validateIsNotLogin() {
		final List<EMailContactAO> contacts = new ArrayList<>();
		contacts.add(proxyFactory.createProxy(EMailContactAO.class, new ModelRepositoryBuilderImpl().withBeanResolver(Mockito.mock(BeanResolver.class)).withDomain(new EMailContactBuilderImpl().withAccount(ContactTestConstants.EMAIL).build()).build()));
		Assert.assertFalse(new CheckLoginAwareValidator().isValid(contacts, Mockito.mock(ConstraintValidatorContext.class)));
	
	}
	
	@Test
	public final void initialize() {
		new CheckLoginAwareValidator().initialize(Mockito.mock(CheckLoginAware.class));
	}

}
