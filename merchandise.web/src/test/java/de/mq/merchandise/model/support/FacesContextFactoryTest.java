package de.mq.merchandise.model.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.model.support.FacesContextFactoryImpl;

/*
 * This didn't make sense, it is only to become test coverage
 * FacesContextFactory decouple the sun shit, to have only one line of code that can not be tested
 */
public class FacesContextFactoryTest {
	
	@Test
	public final void facesContext() {
		Assert.assertNull(new FacesContextFactoryImpl().facesContext());
	}

}
