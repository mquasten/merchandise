package de.mq.merchandise.contact.support;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="CityAddress")
@Table(name="address")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="address_type",discriminatorType=DiscriminatorType.STRING , length=25)
public abstract class AbstractCityAddress implements CityAddress {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@GeneratedValue()
	@Id()
	protected Long id;
	@Column(length = 50)
	@Equals
	protected String city;
	@Column(length = 50, name="zip_code")
	protected String zipCode;
	
	@Column(length = 5)
	@Equals
	protected String country;

	AbstractCityAddress() {
		super();
	}
	
	AbstractCityAddress(final Locale locale, final String zipCode, final String city) {
		this.zipCode=zipCode;
		this.city=city;
		this.country=locale.toString();
	}
	
	protected abstract String  contactInfo();
	
	
	
	@Override
	public final Locale country() {
		return EntityUtil.locale(this.country);
	}

	@Override
	public String zipCode() {
		
		return this.zipCode;
	}

	@Override
	public String city() {
		
		return this.city;
	}
	
	

	

	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}
	
	
	@Override
	public boolean hasId() {
		return (id != null);
	}

	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	
	@Override
	public final String contact() {
		return contactInfo();
	}
}