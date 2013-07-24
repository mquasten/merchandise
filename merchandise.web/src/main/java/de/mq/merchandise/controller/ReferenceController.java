package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.mq.merchandise.reference.ReferenceService;

@Component("referenceController")
public class ReferenceController {
	
	
	private final ReferenceService referenceService;

	
	@Autowired
	public ReferenceController(final  ReferenceService referenceService) {
		this.referenceService=referenceService;
		
	}
	
	public final List<SelectItem> languages(final String language) {
		final List<SelectItem> items = new ArrayList<>();
		for(final Locale locale : referenceService.languages() ) {
			items.add(new SelectItem(locale.getLanguage(), locale.getDisplayLanguage(new Locale(language))));
		}
		return items;
	} 
	
	
	public final List<SelectItem> countries(final String language) {
		final List<SelectItem> items = new ArrayList<>();
		
		for(final Locale locale : referenceService.countries()) {
			items.add(new SelectItem(locale.getCountry(), locale.getDisplayCountry(new Locale(language))));
		}
		return items;
	}
	

}
