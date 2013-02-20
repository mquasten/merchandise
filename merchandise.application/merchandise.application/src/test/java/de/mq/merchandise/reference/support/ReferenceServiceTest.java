package de.mq.merchandise.reference.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.ReferenceService;
import de.mq.merchandise.reference.Reference.Kind;
import de.mq.merchandise.reference.support.ReferenceRepository;
import de.mq.merchandise.reference.support.ReferenceServiceImpl;

public class ReferenceServiceTest {
	
	
	
	
	@Test
	public final void language() {
		final ReferenceRepository referenceRepository = Mockito.mock(ReferenceRepository.class);
		final Reference reference = Mockito.mock(Reference.class);
		Mockito.when(reference.key()).thenReturn("de");
		final List<Reference> references = new ArrayList<>();
		references.add(reference);
		Mockito.when(referenceRepository.forType(Kind.Language)).thenReturn(references);
		final ReferenceService referenceService = new ReferenceServiceImpl(referenceRepository);
		
		final List<Locale> results = referenceService.languages();
		Assert.assertEquals(1, results.size());
		Assert.assertEquals(Locale.GERMAN, results.get(0));
		
	}
	
	@Test
	public final void country() {
		final ReferenceRepository referenceRepository = Mockito.mock(ReferenceRepository.class);
		
		final Reference reference = Mockito.mock(Reference.class);
		Mockito.when(reference.key()).thenReturn("DE");
		final List<Reference> references = new ArrayList<>();
		references.add(reference);
		Mockito.when(referenceRepository.forType(Kind.Country)).thenReturn(references);
		
		final ReferenceService referenceService = new ReferenceServiceImpl(referenceRepository);
		
		final List<Locale> results = referenceService.countries();
		
		Assert.assertEquals(1, results.size());
		
		Assert.assertEquals(new Locale("","DE"), results.get(0));
		
	}
	

}
