package de.mq.merchandise.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.faces.model.SelectItem;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.controller.ReferenceController;
import de.mq.merchandise.reference.ReferenceService;

public class ReferenceControllerTest {
	
	@Test
	public final void languages() {
		
		final ReferenceService referenceService = Mockito.mock(ReferenceService.class);
		final List<Locale> locales = new ArrayList<>();
		locales.add(Locale.GERMAN);
		
		
		Mockito.when(referenceService.languages()).thenReturn(locales);
		
		final ReferenceController referenceController = new ReferenceController(referenceService);
		
		final List<SelectItem> results =  referenceController.languages("de");
		
		Assert.assertEquals(1, results.size());
		
		Assert.assertEquals("de", results.get(0).getValue());
		Assert.assertEquals("Deutsch", results.get(0).getLabel());
		
		
	}
	
	@Test
	public final void countries() {
		final ReferenceService referenceService = Mockito.mock(ReferenceService.class);
		final List<Locale> locales = new ArrayList<>();
		locales.add(Locale.GERMANY);
		Mockito.when(referenceService.countries()).thenReturn(locales);
		final ReferenceController referenceController = new ReferenceController(referenceService);
		
		final List<SelectItem> results =  referenceController.countries("de");
		Assert.assertEquals(1, results.size());
		Assert.assertEquals("DE", results.get(0).getValue());
		Assert.assertEquals("Deutschland", results.get(0).getLabel());
		
		
	}
	

}
