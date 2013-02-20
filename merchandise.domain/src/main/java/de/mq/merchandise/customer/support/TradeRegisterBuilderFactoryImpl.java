package de.mq.merchandise.customer.support;

import de.mq.merchandise.customer.TradeRegisterBuilder;
import de.mq.merchandise.customer.TradeRegisterBuilderFactory;
import de.mq.merchandise.util.EntityUtil;

public class TradeRegisterBuilderFactoryImpl implements TradeRegisterBuilderFactory {
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.person.support.TradeRegisterBuilderFactory#tradeRegisterBuilder()
	 */
	@Override
	public final TradeRegisterBuilder tradeRegisterBuilder() {
		return EntityUtil.create(TradeRegisterBuilderImpl.class);
	}

}
