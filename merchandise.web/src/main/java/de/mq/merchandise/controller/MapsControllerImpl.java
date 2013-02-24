package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.stereotype.Component;

import de.mq.merchandise.contact.Address;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.Coordinates;
import de.mq.merchandise.contact.CoordinatesBuilder;
import de.mq.merchandise.contact.support.ContactBuilderFactoryImpl;
import de.mq.merchandise.customer.Person;

@Component("mapsController")
public class MapsControllerImpl {

	private final CoordinatesBuilder  coordinatesBuilder = new ContactBuilderFactoryImpl().coordinatesBuilder(); 
	
	private final Comparator<Coordinates> longitudeComparator =  new Comparator<Coordinates>() {

		@Override
		public int compare(Coordinates c1, Coordinates c2) {
			return (int) Math.signum(c2.longitude() - c1.longitude());	
		} 
	};
		
	private final Comparator<Coordinates> latitudeComparator = new  Comparator<Coordinates>() {

		@Override
		public int compare(Coordinates c1, Coordinates c2) {
			return (int) Math.signum(c2.latitude() -  c1.latitude());
			
		} 
	};
	
	public final MapModel  model(final Person person) {
		final MapModel mapModel = new DefaultMapModel();
		
		for(final Entry<Coordinates, String> entry :  filter(person.contacts()).entrySet() ){
			mapModel.addOverlay((new Marker( new LatLng(entry.getKey().latitude(), entry.getKey().longitude()), entry.getValue() )));
		}
		
	    return mapModel;
		
	}
	
	
	public final String  center(final Person person ) {
		final List<Coordinates>  coordinates = new ArrayList<>(filter(person.contacts()).keySet());
		if( coordinates.size() < 1){
			throw new IllegalArgumentException("At least one address, wirth geo coordinates should be given");
		}
		
		final Coordinates result =  coordinatesBuilder.withLongitude((Collections.max(coordinates, longitudeComparator).longitude() +  Collections.min(coordinates, longitudeComparator).longitude()) /2).withLatitude((Collections.max(coordinates, latitudeComparator).latitude() +  Collections.min(coordinates, latitudeComparator).latitude()) /2).build();
		return  result.latitude()+ ", "+ result.longitude();
	}


	private Map<Coordinates, String> filter(final Collection<? extends Contact> contacts) {
		final HashMap<Coordinates, String> coordinates = new HashMap<>();
		for(final Contact contact :contacts){
			if (!(contact instanceof Address)) {
				continue;
			}
			coordinates.put(((Address)contact).coordinates(), contact.contact());
			
		}
		return coordinates;
	}
	
	
	
	
}
	
	
