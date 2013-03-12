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
	
	static final String SENSOR_PARAM_KEY = "sensor";
	
	static final String GOOGLE_URL = "http://maps.googleapis.com/maps/api/geocode/json?address={"+  ADDRESS_PARAM_KEY +"}&sensor={" +SENSOR_PARAM_KEY+ "}";
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
	public final Coordinates forAddress(final CityAddress cityAddress) {

		final Map<String, Object> params = new HashMap<>();
		params.put(ADDRESS_PARAM_KEY, cityAddress.contact());
        params.put(SENSOR_PARAM_KEY  , false );
	
		final Map<String, ?> results = restOperations.getForObject(GOOGLE_URL,HashMap.class, params);
		final String status = fromMap(String.class, results, "status");
		
		if (! status.equalsIgnoreCase("ok") ) {
			throw new IllegalStateException("Unable to reverse geocode the  address, status: " + status);
		}
		
		
		
		final List<?>  placemarks = fromMap(List.class, results, "results");

		DataAccessUtils.requiredSingleResult(placemarks);
		
		final Map<String, ?> coordinates = fromMap(Map.class, placemarks.get(0), "geometry" , "location" );
		
		final String[] result  = fromMap(String.class, placemarks.get(0), "formatted_address" ).split(",");
		if ( result.length < 2){
			throw new IllegalStateException("Unable to get parse result, formatted address array size: "+  result.length);
		}
		
		if( !result[1].replaceAll(" ", "").equalsIgnoreCase(cityAddress.zipCode()+ cityAddress.city())) {
			throw new IllegalArgumentException("Coordinates doesn't belong to a street in the same city and or zipcode");
		}
		
		final Number lat = fromMap(Number.class, coordinates, "lat");
		final Number lng =  fromMap(Number.class, coordinates, "lng");
		final List<String> types = fromMap(List.class, placemarks.get(0), "types");
		
		if( ! types.contains("street_address")) {
			throw new IllegalArgumentException("Coordinates doesn't belong to a street address");
		}


		return new CoordinatesBuilderImpl().withLongitude(lng.doubleValue()).withLatitude(lat.doubleValue()).build();
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
