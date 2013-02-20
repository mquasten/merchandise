package de.mq.merchandise.customer.support;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerBuilder;
import de.mq.merchandise.customer.Person;

class CustomerBuilderImpl implements CustomerBuilder {
	
	private Long id;
	
	private Person person;
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.person.support.CustomerBuilder#withId(long)
	 */
	@Override
	public final CustomerBuilder withId(long id) {
		this.id=id;
		return this;
		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.person.support.CustomerBuilder#withPerson(de.mq.merchandise.person.Person)
	 */
	@Override
	public final CustomerBuilder withPerson(final Person person) {
		this.person=person;
		return this;
		
	}


	/* (non-Javadoc)
	 * @see de.mq.merchandise.person.support.CustomerBuilder#build()
	 */
	@Override
	public final Customer build() {
		final Customer customer = new CustomerImpl(person);
		ReflectionUtils.doWithFields(customer.getClass(), new FieldCallback() {
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				field.setAccessible(true);
				if( ! field.isAnnotationPresent(Id.class)) {
					return;
				}
				field.set(customer, id);
			}
		});
		return customer;
	}
}
