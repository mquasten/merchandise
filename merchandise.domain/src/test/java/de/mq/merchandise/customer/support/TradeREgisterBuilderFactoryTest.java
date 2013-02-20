package de.mq.merchandise.customer.support;

import junit.framework.Assert;

import org.junit.Test;

import de.mq.merchandise.customer.TradeRegisterBuilderFactory;
import de.mq.merchandise.customer.support.TradeRegisterBuilderFactoryImpl;
import de.mq.merchandise.customer.support.TradeRegisterBuilderImpl;

public class TradeREgisterBuilderFactoryTest {
	
	private TradeRegisterBuilderFactory tradeRegisterBuilderFactory = new TradeRegisterBuilderFactoryImpl();
	
	@Test
	public final void create() {
		Assert.assertEquals(TradeRegisterBuilderImpl.class, tradeRegisterBuilderFactory.tradeRegisterBuilder().getClass());
	}

}
