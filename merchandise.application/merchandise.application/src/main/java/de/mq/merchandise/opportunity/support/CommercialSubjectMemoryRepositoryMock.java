package de.mq.merchandise.opportunity.support;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerMemoryReposioryMock;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Paging;
@Repository
@Profile("mock")
public class CommercialSubjectMemoryRepositoryMock implements CommercialSubjectRepository {

	private final Map<Long, CommercialSubject> commercialSubjects = new HashMap<>();
	
	static final CommercialSubject[] DEFAULTS = new CommercialSubject[] {new CommercialSubjectImpl(null, "EscortService", "Nicole's special services"),  new CommercialSubjectImpl(null, "Music-Downloads", "Flatrate für Musik")};
	
	private CustomerMemoryReposioryMock customerMemoryReposioryMock;
	
	@Autowired
	public CommercialSubjectMemoryRepositoryMock(final CustomerMemoryReposioryMock customerMemoryReposioryMock) {
		this.customerMemoryReposioryMock=customerMemoryReposioryMock;
	}
	
	
	
	
	

	@PostConstruct
	void init() {
		for(final CommercialSubject commercialSubject : DEFAULTS){
			EntityUtil.setId(commercialSubject, randomId());
			final Field field = ReflectionUtils.findField(commercialSubject.getClass(), "customer");
			field.setAccessible(true);
			ReflectionUtils.setField(field, commercialSubject, customerMemoryReposioryMock.forId(CustomerMemoryReposioryMock.DEFAULT_CUSTOMER_ID));
			commercialSubjects.put(commercialSubject.id(), commercialSubject);
		}
	}
	
	@Override
	public Collection<CommercialSubject> forNamePattern(final Customer customer, final String namePattern, final Paging paging) {
		final String patternFoMatch = namePattern.replaceAll("[%]", ".*");
	
		final List<CommercialSubject> allResults = new ArrayList<>();
		for(final CommercialSubject commercialSubject : commercialSubjects.values()){
			
			if( commercialSubject.customer().id() != customer.id()){
				continue;
			}
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
			EntityUtil.setId(commercialSubject, randomId());
		}
		commercialSubjects.put(commercialSubject.id(), commercialSubject);
		return commercialSubject;
	}

	private long randomId() {
		return (long) (Math.random() * 1e12);
	}



	@Override
	public void delete(final Long id) {
		commercialSubjects.remove(id);
	}






	@Override
	public CommercialSubject forId(final Long id) {
		return commercialSubjects.get(id);
	}



	

}
