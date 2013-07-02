package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.stereotype.Component;

import de.mq.merchandise.opportunity.support.ActivityClassificationImpl;

@Component("classifications")
public class ActivityControllerImpl {

	
	
	public final List<SelectItem> activities() {
		final List<SelectItem> results = new ArrayList<>();
		
		final SelectItem headline = new SelectItem();
		headline.setLabel("Entertainment");
		headline.setValue(new ActivityClassificationImpl());
		headline.setDisabled(true);
		results.add(headline);
		
		for(int i = 0 ; i < 100; i++){
		final SelectItem artist = new SelectItem();
		artist.setLabel("Artist " +i);
		artist.setValue(new ActivityClassificationImpl());
		results.add(artist);
		}
		return results;
	}
	
}
