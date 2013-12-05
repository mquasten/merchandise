package de.mq.merchandise.rule;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.State;

/**
 * A Rule, a groovy program stored in a document based data store
 * It is referenced only by its id and can be accessed  with a rest client 
 * A Rule can be a validator, a converter or a price table. It depends of the implemented interface
 * @author mquasten
 *
 */
public interface Rule extends BasicEntity{

	/**
	 * The name of the rule
	 * @return the rule name
	 */
	String name();

	/**
	 * The state. It can be active or inactive
	 * @return the state
	 */
	State state();

	/**
	 * The related customer, that is owner of the rule
	 * @return the owner, the related customer
	 */
	Customer customer();
}