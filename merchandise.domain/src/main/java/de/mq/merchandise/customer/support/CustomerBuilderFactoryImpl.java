package de.mq.merchandise.customer.support;

import de.mq.merchandise.customer.CustomerBuilder;
import de.mq.merchandise.customer.CustomerBuilderFactory;

public class CustomerBuilderFactoryImpl implements CustomerBuilderFactory {
	
	
	@Override
	public final CustomerBuilder customerBuilder() {
		return new CustomerBuilderImpl();
		
	}

}
