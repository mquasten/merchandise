package de.mq.merchandise.customer.support;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.annotations.Target;

import de.mq.merchandise.customer.Nativity;
import de.mq.merchandise.customer.NaturalPerson;
import de.mq.merchandise.util.Equals;

@Entity(name="NaturalPerson")
class NaturalPersonImpl extends AbstractPerson implements NaturalPerson{

	private static final long serialVersionUID = 1L;


	@Column(length=50)
	@Equals
	private final String firstname;
	
	
	@Equals
	@Embedded()
	@Target(NativityImpl.class)
	private final Nativity nativity;
	
	public NaturalPersonImpl(final String firstname, final String lastname, final Nativity nativity, final Locale locale) {
		super(lastname, locale);
		this.firstname=firstname;
		this.nativity= nativity;
	}
	
	public NaturalPersonImpl(final String firstname, final String lastname, final Nativity nativity) {
		this(firstname, lastname, nativity, DEFAULT_LOCALE);
	}
	
	@SuppressWarnings("unused")
	private NaturalPersonImpl() {
		this(null,null,null,DEFAULT_LOCALE);
	}
	
	
	public final String firstname() {
		return this.firstname;
	}

	
	public final Nativity nativity(){
		return this.nativity;
	}

	
	

}
