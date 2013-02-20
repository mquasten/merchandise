package de.mq.merchandise.reference.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.reference.Reference;
import de.mq.merchandise.reference.Reference.Kind;

@Repository
@Profile("mock")
public class ReferenceMemoryRepositoryMock implements ReferenceRepository {

	@Override
	public final List<Reference> forType(final Kind referenceType) {
		final List<Reference> results = new ArrayList<>();
		if(referenceType==Kind.Country){
			results.add(new ReferenceImpl(Locale.GERMANY.getCountry(), referenceType));
			results.add(new ReferenceImpl(Locale.UK.getCountry(), referenceType));
			results.add(new ReferenceImpl(Locale.US.getCountry(), referenceType));
		    return results;
		}
		
		results.add(new ReferenceImpl(Locale.GERMAN.getLanguage(), referenceType));
		results.add(new ReferenceImpl(Locale.ENGLISH.getLanguage(), referenceType));
		
		return results;
	}

}
