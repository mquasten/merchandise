package de.mq.merchandise.contact;

import java.util.Locale;

/**
 * The address of a city
 * @author ManfredQuasten
 *
 */
public interface CityAddress extends Contact{

	/**
	 * The zipCode of the City
	 * @return the zipCode
	 */
	public abstract String zipCode();

	/**
	 * The city related to the address
	 * @return the city of the address
	 */
	public abstract String city();
	
	/**
	 * The locale for the country
	 * The language is ignored
	 * @return the county of the address
	 */
	Locale country();

}