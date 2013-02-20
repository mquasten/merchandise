package de.mq.merchandise.contact;

/**
 * A real address, with street and houseNumber
 * @author ManfredQuasten
 *
 */
public interface Address extends CityAddress {
	
	/**
	 * The houseNumber of the address
	 * @return the houseNumber
	 */
	String houseNumber();
	
	/**
	 * The street of the address.
	 * @return the street 
	 */
	String street();
	
	/**
	 * The related coordinate of the address.
	 * If not available null
	 * @return the coordinates of the address.
	 */
	Coordinates coordinates();

	/**
	 * Assign new Coordinate to a given address
	 * @param coordinates the new Coordinate for the address;
	 */
	void assign(Coordinates coordinates);
	
	

}
