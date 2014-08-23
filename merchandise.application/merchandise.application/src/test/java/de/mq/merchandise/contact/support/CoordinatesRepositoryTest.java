package de.mq.merchandise.contact.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestOperations;

import de.mq.mapping.util.json.MapBasedResponseClassFactory;
import de.mq.mapping.util.json.MapBasedResultBuilder;
import de.mq.mapping.util.json.support.MapBasedResponse;
import de.mq.mapping.util.json.support.MappingTestConstants;
import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;

public class CoordinatesRepositoryTest {
	
	
	private static final String ZIP_CODE = "12345";
	private static final String CITY = "Magadan";
	private static final String FORMATTED_ADDRESS = "Eine Straße,"+ ZIP_CODE + " " + CITY +", Russland";
	static final String ADDRESS = "Magadan RU";
	static final String KEY = "kylie";
	final CityAddress cityAddress = Mockito.mock(CityAddress.class);
	
	private final RestOperations restOperations = Mockito.mock(RestOperations.class);
	private final MapBasedResponseClassFactory mapBasedResponseClassFactory = Mockito.mock(MapBasedResponseClassFactory.class);
	private CoordinatesRepository coordinatesRepository;
	private final Map<String,Object> params = new HashMap<>();
	
	private Class<MapBasedResponse> clazz = MapBasedResponse.class;
	
	private  Collection<?>  mappings;
	
	private MapBasedResultBuilder mappBasedResultBuilder = MappingTestConstants.newMappingBuilder();
	@Before
	public final void setup() {
		params.clear();
		params.put(CoordinatesRepositoryImpl.SENSOR_PARAM_KEY, false);
		
		Mockito.when(mapBasedResponseClassFactory.mappingBuilder()).thenReturn(mappBasedResultBuilder);
		
		coordinatesRepository = new CoordinatesRepositoryImpl(restOperations, mapBasedResponseClassFactory);
		mappings = mappBasedResultBuilder.build();
		Mockito.when(mapBasedResponseClassFactory.createClass(mappings)).thenReturn(clazz);
		
		Mockito.when(cityAddress.contact()).thenReturn(FORMATTED_ADDRESS);
		Mockito.when(cityAddress.city()).thenReturn(CITY);
		Mockito.when(cityAddress.city()).thenReturn(ZIP_CODE);
		params.put(CoordinatesRepositoryImpl.ADDRESS_PARAM_KEY, cityAddress.contact());
		final MapBasedResponse mapBasedResponse = MappingTestConstants.newEnhancedMapBasedResponse(mappings, createJsonMap( CoordinatesRepositoryImpl.STATUS_OK, COORDINATES, CoordinatesRepositoryImpl.STREET_ADDRESS_TYPE, cityAddress.contact()));
		
		Mockito.when(restOperations.getForObject(CoordinatesRepositoryImpl.GOOGLE_URL, clazz, params)).thenReturn(mapBasedResponse);
		
	}
	
	private static final Coordinates COORDINATES = new CoordinatesBuilderImpl().withLatitude(59.5683240).withLongitude(150.8089180).build();

	
	@Test
	public final void forAddress() {
		Assert.assertEquals(COORDINATES, coordinatesRepository.forAddress(cityAddress));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void  forAddressWrongAddressTyp() {
		final MapBasedResponse mapBasedResponse = MappingTestConstants.newEnhancedMapBasedResponse(mappings, createJsonMap( CoordinatesRepositoryImpl.STATUS_OK, COORDINATES, "political" , cityAddress.contact()));
		Mockito.when(restOperations.getForObject(CoordinatesRepositoryImpl.GOOGLE_URL, clazz, params)).thenReturn(mapBasedResponse);
		coordinatesRepository.forAddress(cityAddress);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public final void forAddressCityAndZipCodeNotMatching() {
		Mockito.when(cityAddress.city()).thenReturn("Stalingrad");
		Mockito.when(cityAddress.zipCode()).thenReturn("TIGER2");
		coordinatesRepository.forAddress(cityAddress);
	}
	@Test
	public final void forAddressdZipCodeMatching() {
		Mockito.when(cityAddress.city()).thenReturn("Stalingrad");
		Mockito.when(cityAddress.zipCode()).thenReturn(ZIP_CODE);
		Assert.assertEquals(COORDINATES, coordinatesRepository.forAddress(cityAddress));
	}
	
	@Test
	public final void forAddressdCityMatching() {
		Mockito.when(cityAddress.city()).thenReturn(CITY);
		Mockito.when(cityAddress.zipCode()).thenReturn("TIGER2");
		Assert.assertEquals(COORDINATES, coordinatesRepository.forAddress(cityAddress));
	}
	
	
	
	private Map<String, Object> createJsonMap(final String status, final Coordinates coordinates, final String type, final String formatted_address) {
		final Map<String,Object> jsonResult = new HashMap<>();
		
		jsonResult.put("status", status);
		final List<Object> placemarks = new ArrayList<>();
		
		final Map<String,Object> placemark = new HashMap<>();
		
		final Map<String,Object> points = new HashMap<>();
		placemark.put("geometry", points);
		Map<String,Number> coordinatesList = new HashMap<>();
		coordinatesList.put("lng", coordinates.longitude());
		coordinatesList.put("lat", coordinates.latitude());
		
		placemark.put("formatted_address", formatted_address);
		
		points.put("location", coordinatesList);
		
		placemarks.add(placemark);
		
		final List<String> types = new ArrayList<>();
		types.add(type);
		
		placemark.put("types", types);
		
		
		final Map<String,Object> extendedData = new HashMap<>();
		placemark.put("ExtendedData", extendedData);
		
		jsonResult.put("results", placemarks);
		return jsonResult;
	}

}
