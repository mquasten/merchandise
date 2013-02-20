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
	
	static final double MAX_DEVIATION = 1.0D;
	
	
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
		
		final Map<String,String> params = new HashMap<>();
		params.put("address", ADDRESS);
		params.put("format", "json");
		
		final CityAddress cityAddress = cityAddress();
		
		final Map<String, Object> jsonResult = createJsonMap(200, COORDINATES, 0,0 );
		
		
		final RestOperations restOperations = Mockito.mock(RestOperations.class);
		Mockito.when(restOperations.getForObject(urlCaptor.capture(), classCaptor.capture(), mapCaptor.capture())).thenReturn(jsonResult);
		
		
		final CoordinatesRepository coordinatesRepository = new CoordinatesRepositoryImpl(restOperations);
		
		final Coordinates result = coordinatesRepository.forAddress(cityAddress, MAX_DEVIATION);
		Assert.assertEquals(COORDINATES.longitude(), result.longitude());
		Assert.assertEquals(COORDINATES.latitude(), result.latitude());
		
		Assert.assertEquals(CoordinatesRepositoryImpl.GOOGLE_URL, urlCaptor.getValue());
		Assert.assertEquals(HashMap.class, classCaptor.getValue());
		Assert.assertEquals(1, mapCaptor.getValue().size());
		Assert.assertEquals(CoordinatesRepositoryImpl.ADDRESS_PARAM_KEY, mapCaptor.getValue().keySet().iterator().next());
		Assert.assertEquals(cityAddress.contact(), mapCaptor.getValue().values().iterator().next());
	}
	
	@SuppressWarnings("unchecked")
	@Test(expected=IllegalStateException.class)
	public final void forAddressWrongStatus() {
		final RestOperations restOperations = Mockito.mock(RestOperations.class);
		final CoordinatesRepository coordinatesRepository = new CoordinatesRepositoryImpl(restOperations);
		Mockito.when(restOperations.getForObject(Mockito.anyString(),Mockito.any(Class.class), Mockito.anyMap())).thenReturn(createJsonMap(500, COORDINATES, 0,0 ));
		
		coordinatesRepository.forAddress(cityAddress(), MAX_DEVIATION);
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
		Mockito.when(restOperations.getForObject(Mockito.anyString(),Mockito.any(Class.class), Mockito.anyMap())).thenReturn(createJsonMap(200, COORDINATES, 1.0,1.0 ));
		coordinatesRepository.forAddress(cityAddress(), MAX_DEVIATION);
	}

	private Map<String, Object> createJsonMap(final Number status, final Coordinates coordinates, final double deltaLongitude, final double deltaLatitude) {
		final Map<String,Object> jsonResult = new HashMap<>();
		final Map<String,Number> code = new HashMap<>();
		code.put("code", status);
		jsonResult.put("Status", code);
		final List<Object> placemarks = new ArrayList<>();
		
		final Map<String,Object> placemark = new HashMap<>();
		
		final Map<String,Object> points = new HashMap<>();
		placemark.put("Point", points);
		List<Number> coordinatesList = new ArrayList<>();
		coordinatesList.add(coordinates.longitude());
		coordinatesList.add(coordinates.latitude());
		coordinatesList.add(0D);;
		points.put("coordinates", coordinatesList);
		
		placemarks.add(placemark);
		
		final Map<String,Object> extendedData = new HashMap<>();
		placemark.put("ExtendedData", extendedData);
		
		
		
		final Map<String,Number>box = new HashMap<>();
		box.put("north", coordinates.latitude() + deltaLatitude/2);
		box.put("east", coordinates.longitude() + deltaLongitude/2);
		box.put("south", coordinates.latitude() - deltaLatitude/2);
		box.put("west", coordinates.longitude() - deltaLongitude/2);
		extendedData.put("LatLonBox", box);
		
		jsonResult.put("Placemark", placemarks);
		return jsonResult;
	}

}
