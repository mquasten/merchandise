package de.mq.merchandise.contact.support;

import java.util.Locale;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.annotations.Target;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.util.Equals;


@Entity(name="Address")
public
class AddressImpl extends AbstractCityAddress implements Address {

	private static final long serialVersionUID = 1L;

	@Column(length=50)
	@Equals
	private final String street;
	
	@Column(length=10, name="house_number")
	@Equals
	private final String houseNumber;
	
	@Embedded
	@Target(CoordinatesImpl.class)
	private Coordinates coordinates;
	
	@SuppressWarnings("unused")
	private AddressImpl() {
		super(new Locale("",""), null,null);
		this.street=null;
		this.houseNumber=null;
		this.coordinates=null;
	}
	
	AddressImpl(final Locale country, final String zipCode, final String city, final String street, final String houseNumber, final Coordinates coordinates){
		super(country, zipCode,city);
		this.street=street;
		this.houseNumber=houseNumber;
		this.coordinates=coordinates;
	}
	
	@Override
	public String houseNumber() {
		return houseNumber;
	}

	@Override
	public String street() {
		return street;
	}
	
	

	@Override
	public Coordinates coordinates() {
		return coordinates;
	}

	@Override
	protected  final String contactInfo() {
		return street +" "+houseNumber+ ", "+  zipCode() + " " + city() + ", " +country().getCountry() ;
	}

	

	@Override
	public final void assign(final Coordinates coordinates) {
		this.coordinates=coordinates;
	}

	

}
