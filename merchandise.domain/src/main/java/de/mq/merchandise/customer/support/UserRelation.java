package de.mq.merchandise.customer.support;

import java.util.Set;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.State;

interface UserRelation extends BasicEntity {

	void grant(CustomerRole... roles);

	void revoke(CustomerRole... roles);

	Set<CustomerRole> roles();

	State state();

	long id();

	boolean isOwner(Person person);

	boolean hasRole(CustomerRole role);

	Person person();

	Customer customer();

}