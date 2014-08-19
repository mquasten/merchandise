package de.mq.merchandise.contact.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.client.RestOperations;

import de.mq.mapping.util.json.MapBasedResponseClassFactory;
import de.mq.mapping.util.json.support.MapBasedResponse;
import de.mq.mapping.util.json.support.MapBasedResponse.InfoField;
import de.mq.mapping.util.json.support.MapBasedResultRow;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;

@Repository
class CoordinatesRepositoryImpl implements CoordinatesRepository {

	private static final String STREET_ADDRESS_TYPE = "street_address";

	private static final String STATUS_OK = "OK";

	static final String ADDRESS_PARAM_KEY = "address";
	
	static final String SENSOR_PARAM_KEY = "sensor";
	
	static final String GOOGLE_URL = "http://maps.googleapis.com/maps/api/geocode/json?address={"+  ADDRESS_PARAM_KEY +"}&sensor={" +SENSOR_PARAM_KEY+ "}";
	private final RestOperations restOperations;
	

	
	
	private final Class<MapBasedResponse> clazz; 
	

	@Autowired
	public CoordinatesRepositoryImpl(final RestOperations restOperations, final MapBasedResponseClassFactory mapBasedResponseClassFactory) {
		this.restOperations = restOperations;
		clazz=mapBasedResponseClassFactory.createClass( mapBasedResponseClassFactory.mappingBuilder().withParentMapping("results").withChildMapping( MapBasedResponse.ChildField.Value, "geometry", "location" ).withChildMapping( MapBasedResponse.ChildField.Key, "types").withChildMapping(MapBasedResponse.ChildField.Id, "formatted_address").withFieldMapping("status",  MapBasedResponse.InfoField.Status).build());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.mq.merchandise.application.contact.support.CoordinatesRepository#
	 * forAddress(de.mq.merchandise.domain.contact.CityAddress)
	 */
	
	@Override
	public final Coordinates forAddress(final CityAddress cityAddress) {
		final Map<String, Object> params = new HashMap<>();
		params.put(ADDRESS_PARAM_KEY, cityAddress.contact());
        params.put(SENSOR_PARAM_KEY  , false );
	
        
        final MapBasedResponse response = restOperations.getForObject(GOOGLE_URL,clazz, params);
       
        final String status = response.field(InfoField.Status, String.class);
        Assert.isTrue(status.equalsIgnoreCase(STATUS_OK), "Bad Respose Status: " + status);
        
      
        for( final MapBasedResultRow row : response.rows()) {
        	if ( ! row.collectionKey(String.class).contains(STREET_ADDRESS_TYPE)) {
        		continue;
        	}
        	final String formatted = row.id();
        	if(! (formatted.toLowerCase().contains(cityAddress.city().toLowerCase()) || formatted.toLowerCase().contains(cityAddress.zipCode().toLowerCase()))) {
        		continue;
        	}
        	return  row.composedValue(CoordinatesImpl.class);
        	
        }
		throw new IllegalArgumentException("Coordinates for address not found: " + cityAddress.contact() );
		
		
	}

}
