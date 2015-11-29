package de.mq.merchandise.subject.support;

import java.util.Locale;

import de.mq.merchandise.customer.Customer;
import de.mq.util.event.support.ObservableImpl;


class UserModelImpl extends ObservableImpl<UserModel.EventType> implements UserModel {
	
	private final Customer customer;
	
	private Locale locale ; 

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
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.UserModel#getLocale()
	 */
	@Override
	public final Locale getLocale() {
		return this.locale;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.UserModel#setLocale(java.util.Locale)
	 */
	@Override
	public final void setLocale(final Locale locale) {
		if(locale == null){
			return;
		}
		this.locale=locale;
	
		notifyObservers(EventType.LocaleChanged);
		
	}
}
