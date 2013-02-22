package de.mq.merchandise.controller;

import java.util.Collection;

import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.springframework.stereotype.Component;

import de.mq.merchandise.contact.CityAddress;

@Component("mapsController")
public class MapsControllerImpl {

	
	public final MapModel  model(final Collection<CityAddress> addresses) {
		
		final MapModel mapModel = new DefaultMapModel();
		mapModel.addOverlay(new Marker(new LatLng(36.879466, 30.667648), "Melmack")); 
		return mapModel;
		
	}
	
}
