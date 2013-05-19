package de.mq.merchandise.opportunity.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Paging;

public class CommercialSubjectMemoryRepositoryMock implements CommercialSubjectRepository {

	private final Map<Long, CommercialSubject> commercialSubjects = new HashMap<>();
	

	@Override
	public Collection<CommercialSubject> forNamePattern(final String namePattern, final Paging paging) {
		final String patternFoMatch = namePattern.replaceAll("[%]", ".*");
	
		final List<CommercialSubject> allResults = new ArrayList<>();
		for(final CommercialSubject commercialSubject : commercialSubjects.values()){
			
			if( ! commercialSubject.name().matches(patternFoMatch)) {
				continue;
			}
			allResults.add(commercialSubject);
		}
		paging.assignRowCounter(allResults.size());
	    sort(allResults);
	    return Collections.unmodifiableList(allResults.subList(paging.firstRow(), Math.min(allResults.size(), paging.firstRow()+paging.pageSize())));
	   
	}

	

	private void sort(final List<CommercialSubject> allResults) {
		Collections.sort(allResults, new Comparator<CommercialSubject>(){

			@Override
			public int compare(final CommercialSubject c1, final CommercialSubject c2) {
				return c1.name().compareTo(c2.name());
			}});
	}

	@Override
	public CommercialSubject save(final CommercialSubject commercialSubject) {
		if ( ! commercialSubject.hasId()){
			EntityUtil.setId(commercialSubject, (long) (Math.random() * 1e12));
		}
		commercialSubjects.put(commercialSubject.id(), commercialSubject);
		return commercialSubject;
	}



	@Override
	public void delete(final CommercialSubject commercialSubject) {
        idExistsGuard(commercialSubject);
		commercialSubjects.remove(commercialSubject.id());
		
	}



	private void idExistsGuard(final CommercialSubject commercialSubject) {
		if( ! commercialSubject.hasId()) {
        	throw new IllegalArgumentException("Id should not be null, commercialSubject isn't persistent");
        }
	}

}
