package de.mq.merchandise.customer.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import de.mq.merchandise.customer.Customer;

@Component
class CustomerAO2DomainConverter implements Converter<CustomerAO, Customer>{

	@Override
	public Customer convert(final CustomerAO customerModel) {
		if(customerModel == null){
			return null;
		}
		return customerModel.getCustomer();
	}

}
