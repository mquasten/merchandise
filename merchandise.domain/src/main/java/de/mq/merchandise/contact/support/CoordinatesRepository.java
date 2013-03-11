package de.mq.merchandise.contact.support;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;

/**
 * Repository for GeoCoding. The known anti-corruption-layer pattern from DDD.
 * The implementation for geoCoding should be exchangeable, google maps , own solr index, navTec or what ever
 * @author MQuasten
 *
 */
interface CoordinatesRepository {

	/**
	 * Gets the Coordinates from the given address
	 * @param cityAddress the address for witch the coordinates should be given
	 * @return the longitude and latitude for the given address
	 */
	public abstract Coordinates forAddress(final CityAddress cityAddress);

}