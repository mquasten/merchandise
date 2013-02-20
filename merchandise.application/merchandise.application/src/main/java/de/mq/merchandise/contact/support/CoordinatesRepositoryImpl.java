package de.mq.merchandise.contact.support;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestOperations;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;

@Repository
class CoordinatesRepositoryImpl implements CoordinatesRepository {

	static final String ADDRESS_PARAM_KEY = "address";
	static final String GOOGLE_URL = "http://maps.google.com/maps/geo?q={"+ADDRESS_PARAM_KEY+"}&output=json";
	private final RestOperations restOperations;

	@Autowired
	public CoordinatesRepositoryImpl(final RestOperations restOperations) {
		this.restOperations = restOperations;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.application.contact.support.CoordinatesRepository#
	 * forAddress(de.mq.merchandise.domain.contact.CityAddress)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public final Coordinates forAddress(final CityAddress cityAddress, final double maxDeviation) {

		final Map<String, String> params = new HashMap<>();
		params.put(ADDRESS_PARAM_KEY, cityAddress.contact());

	
		final Map<String, ?> results = restOperations.getForObject(GOOGLE_URL,HashMap.class, params);
		

		final Number status = fromMap(Number.class, results, "Status", "code");
		if (status.intValue() != 200) {
			throw new IllegalStateException("Unable to reverse geocode the  address, status: " + status.intValue());
		}

		final List<?> placemarks = fromMap(List.class, results, "Placemark");

		DataAccessUtils.requiredSingleResult(placemarks);

		final Number[] coordinates = (Number[]) fromMap(List.class,placemarks.get(0), "Point", "coordinates").toArray(new Number[3]);

		final Map<String, ?> latLonBox = fromMap(Map.class, placemarks.get(0), "ExtendedData", "LatLonBox");

		final Coordinates northEast = new CoordinatesBuilderImpl().withLatitude(fromMap(Number.class, latLonBox, "north").doubleValue()).withLongitude(fromMap(Number.class, latLonBox, "east").doubleValue()).build();
		final Coordinates southWest = new CoordinatesBuilderImpl().withLatitude(fromMap(Number.class, latLonBox, "south").doubleValue()).withLongitude(fromMap(Number.class, latLonBox, "west").doubleValue()).build();

		final double deviation = northEast.distance(southWest);

		if (deviation > maxDeviation) {
			throw new IllegalArgumentException("Deviation is to large: " + deviation + "km");
		}

		return new CoordinatesBuilderImpl().withLongitude(coordinates[0].doubleValue()).withLatitude(coordinates[1].doubleValue()).build();
	}

	@SuppressWarnings("unchecked")
	<T> T fromMap(Class<T> clazz, final Object map,final String... path) {
		Object current = map;
		for (final String key : path) {
			if (!(current instanceof Map<?, ?>)) {
				throw new InvalidDataAccessApiUsageException("Node isn't a Map");
			}
			if (!((Map<?, ?>) current).containsKey(key)) {
				throw new InvalidDataAccessApiUsageException("Node doesn't exists: " + key);
			}
			current = ((Map<?, ?>) current).get(key);

		}
		return (T) current;

	}

}
