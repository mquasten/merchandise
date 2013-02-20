package de.mq.merchandise.customer.support;

import de.mq.merchandise.customer.NativityBuilder;
import de.mq.merchandise.customer.NativityBuilderFactory;
import de.mq.merchandise.util.EntityUtil;

public class NativityBuilderFactoryImpl implements NativityBuilderFactory {
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.person.support.NativityBuilderFactory#nativityBuilder()
	 */
	@Override
	public final NativityBuilder nativityBuilder() {
		return EntityUtil.create(NativityBuilderImpl.class);
	}

}
