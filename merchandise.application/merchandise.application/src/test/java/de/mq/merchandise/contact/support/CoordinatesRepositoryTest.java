package de.mq.merchandise.contact.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.web.client.RestOperations;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;

public class CoordinatesRepositoryTest {
	
	static final String ADDRESS = "Magadan RU";
	static final String KEY = "kylie";
	
	
	
	private static final Coordinates COORDINATES = new CoordinatesBuilderImpl().withLatitude(59.5683240).withLongitude(150.8089180).build();

	@Test
	public void fromMap() {
		final CoordinatesRepositoryImpl coordinatesRepository = new CoordinatesRepositoryImpl(Mockito.mock(RestOperations.class));
		final Map<String,Object> artists = new HashMap<>();
		@SuppressWarnings("unchecked")
		final Map<String,?> videos = Mockito.mock(Map.class);
		artists.put(KEY, videos);
		Assert.assertEquals(videos, coordinatesRepository.fromMap(Map.class, artists, KEY));
		
		
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void fromMapNoMap() {
		final CoordinatesRepositoryImpl coordinatesRepository = new CoordinatesRepositoryImpl(Mockito.mock(RestOperations.class));
		final Map<String,Object> artists = new HashMap<>();
		artists.put(KEY, "fever");
		coordinatesRepository.fromMap(Map.class, artists, KEY, "whatEver");
		
	}
	
	@Test(expected=InvalidDataAccessApiUsageException.class)
	public void fromMapWrongKey() {
		final CoordinatesRepositoryImpl coordinatesRepository = new CoordinatesRepositoryImpl(Mockito.mock(RestOperations.class));
		final Map<String,Object> artists = new HashMap<>();
		@SuppressWarnings("unchecked")
		final Map<String,?> videos = Mockito.mock(Map.class);
		artists.put(KEY, videos);
		coordinatesRepository.fromMap(Map.class, artists, "dontLetmeGetMe");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public final void forAddress() {
		
		final ArgumentCaptor<String> urlCaptor = ArgumentCaptor.forClass(String.class);
		final ArgumentCaptor<Class> classCaptor = ArgumentCaptor.forClass(Class.class);
		final ArgumentCaptor<Map> mapCaptor = ArgumentCaptor.forClass(Map.class);
		
		final Map<String,Object> params = new HashMap<>();
		params.put("address", ADDRESS);
		params.put(CoordinatesRepositoryImpl.SENSOR_PARAM_KEY, false);
		
		final CityAddress cityAddress = cityAddress();
		
		final Map<String, Object> jsonResult = createJsonMap("ok", COORDINATES, "street_address" );
		
		
		final RestOperations restOperations = Mockito.mock(RestOperations.class);
		Mockito.when(restOperations.getForObject(urlCaptor.capture(), classCaptor.capture(), mapCaptor.capture())).thenReturn(jsonResult);
		
		
		final CoordinatesRepository coordinatesRepository = new CoordinatesRepositoryImpl(restOperations);
		
		final Coordinates result = coordinatesRepository.forAddress(cityAddress);
		Assert.assertEquals(COORDINATES.longitude(), result.longitude());
		Assert.assertEquals(COORDINATES.latitude(), result.latitude());
		
		Assert.assertEquals(CoordinatesRepositoryImpl.GOOGLE_URL, urlCaptor.getValue());
		Assert.assertEquals(HashMap.class, classCaptor.getValue());
		
		Assert.assertEquals(2, mapCaptor.getValue().size());
		Assert.assertEquals(params, mapCaptor.getValue());
		
		
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateException.class)
	public final void forAddressWrongStatus() {
		final RestOperations restOperations = Mockito.mock(RestOperations.class);
		final CoordinatesRepository coordinatesRepository = new CoordinatesRepositoryImpl(restOperations);
		Mockito.when(restOperations.getForObject(Mockito.anyString(),Mockito.any(Class.class), Mockito.anyMap())).thenReturn(createJsonMap("error", COORDINATES, "street_address" ));
		
		coordinatesRepository.forAddress(cityAddress());
	}

	private CityAddress cityAddress() {
		final CityAddress cityAddress= Mockito.mock(CityAddress.class);
		Mockito.when(cityAddress.contact()).thenReturn(ADDRESS);
		return cityAddress;
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	@SuppressWarnings("unchecked")
	public final void forAddressWrongDeviation() {
		final RestOperations restOperations = Mockito.mock(RestOperations.class);
		
		final CoordinatesRepository coordinatesRepository = new CoordinatesRepositoryImpl(restOperations);
		Mockito.when(restOperations.getForObject(Mockito.anyString(),Mockito.any(Class.class), Mockito.anyMap())).thenReturn(createJsonMap("ok", COORDINATES, "country" ));
		coordinatesRepository.forAddress(cityAddress());
	}

	private Map<String, Object> createJsonMap(final String status, final Coordinates coordinates, final String type) {
		final Map<String,Object> jsonResult = new HashMap<>();
		//final Map<String,Number> code = new HashMap<>();
		//code.put("code", status);
		jsonResult.put("status", status);
		final List<Object> placemarks = new ArrayList<>();
		
		final Map<String,Object> placemark = new HashMap<>();
		
		final Map<String,Object> points = new HashMap<>();
		placemark.put("geometry", points);
		Map<String,Number> coordinatesList = new HashMap<>();
		coordinatesList.put("lng", coordinates.longitude());
		coordinatesList.put("lat", coordinates.latitude());
		
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
