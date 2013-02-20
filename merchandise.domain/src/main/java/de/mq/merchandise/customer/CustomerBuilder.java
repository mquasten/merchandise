package de.mq.merchandise.customer;


public interface CustomerBuilder {

	public abstract CustomerBuilder withId(long id);

	public abstract CustomerBuilder withPerson(Person person);

	public abstract Customer build();

}