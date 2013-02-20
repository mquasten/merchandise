package de.mq.merchandise.contact.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.GeocodingService;

@Service
class GeocodingServiceImpl implements GeocodingService {
	
	private final CoordinatesRepository coordinatesRepository;
	
	@Autowired
	public GeocodingServiceImpl(final CoordinatesRepository coordinatesRepository){
		this.coordinatesRepository=coordinatesRepository;
	}
	
	public final Coordinates coordinates(final CityAddress cityAddress, final double maxDeviation) {
		return coordinatesRepository.forAddress(cityAddress, maxDeviation);
	}
	

}
