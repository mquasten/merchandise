package de.mq.merchandise.controller;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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
	
	private final Comparator<Entry<Coordinates,String>> longitudeComparator =  new Comparator<Entry<Coordinates,String>>() {

		@Override
		public int compare(final Entry<Coordinates,String> c1, final Entry<Coordinates,String> c2) {
			return (int) Math.signum(c2.getKey().longitude() - c1.getKey().longitude());	
		} 
	};
		
	private final Comparator<Entry<Coordinates,String>> latitudeComparator = new  Comparator<Entry<Coordinates,String>>() {

		@Override
		public int compare(final Entry<Coordinates,String> c1, final Entry<Coordinates,String> c2) {
			return (int) Math.signum(c2.getKey().latitude() -  c1.getKey().latitude());
			
		} 
	};
	
	public final MapModel  model(final Person person) {
		final MapModel mapModel = new DefaultMapModel();
		
		for(final Entry<Coordinates, String> entry :  filter(person.contacts()) ){
			mapModel.addOverlay((new Marker( new LatLng(entry.getKey().latitude(), entry.getKey().longitude()), entry.getValue() )));
		}
		
	    return mapModel;
		
	}
	
	
	public final String  center(final Person person ) {
		
		final List<Entry<Coordinates,String>>  coordinates = new ArrayList<>(filter(person.contacts()));
		if( coordinates.size() < 1){
			throw new IllegalArgumentException("At least one address, wirth geo coordinates should be given");
		}
		
		final Coordinates result =  coordinatesBuilder.withLongitude((Collections.max(coordinates, longitudeComparator).getKey().longitude() +  Collections.min(coordinates, longitudeComparator).getKey().longitude()) /2).withLatitude((Collections.max(coordinates, latitudeComparator).getKey().latitude() +  Collections.min(coordinates, latitudeComparator).getKey().latitude()) /2).build();
		return  result.latitude()+ ", "+ result.longitude();
	}


	private Set<Entry<Coordinates, String>> filter(final Collection<? extends Contact> contacts) {
		final Set<Entry<Coordinates, String>> coordinates = new HashSet<>();
		for(final Contact contact :contacts){
			if (!(contact instanceof Address)) {
				continue;
			}
			coordinates.add(new AbstractMap.SimpleEntry<>(((Address)contact).coordinates(), contact.contact()));
		}
		return coordinates;
	}
	
	
}
	
	
