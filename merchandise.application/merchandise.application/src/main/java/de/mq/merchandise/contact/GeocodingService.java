package de.mq.merchandise.contact;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;

/**
 * Get the geoCoordinates (longitude and latitude for a given address
 * @author MQuasten
 *
 */
public interface GeocodingService {
	
	/**
	 * Calculates the coordinates for the given address.
	 * @param cityAddress the address for which the geoCoding should be done
	 * @return the coordinates for the given address
	 */
	Coordinates coordinates(final CityAddress cityAddress);
	

}