package de.mq.merchandise.subject.support;

import de.mq.merchandise.customer.Customer;

class UserModelImpl implements UserModel {
	
	private final Customer customer;

	UserModelImpl(Customer customer) {
		this.customer = customer;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.UserModel#getCustomer()
	 */
	@Override
	public final Customer getCustomer() {
		return customer;
	}

}
