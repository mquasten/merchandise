package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

@Repository
@Profile("mock")
public class DocumentEntityRepositoryMock  implements DocumentEntityRepository{
	
	
	private final Map<UUID,DocumentsAware>  storedDocuments = new HashMap<>(); 

	private final OpportunityMemoryRepositoryMock opportunityRepository;
	
	private final CommercialSubjectMemoryRepositoryMock commercialSubjectRepository;
	
	@Autowired
	DocumentEntityRepositoryMock(OpportunityMemoryRepositoryMock opportunityRepository, CommercialSubjectMemoryRepositoryMock commercialSubjectRepository) {
		
		this.opportunityRepository = opportunityRepository;
		this.commercialSubjectRepository = commercialSubjectRepository;
	}

	
	
	@PostConstruct
	void init() {
		System.out.println("contruct DocumentEntityRepositoryMock" );
		for(final Opportunity opportunity :opportunityRepository.entities()) {
			System.out.println("add opportunity:" + opportunity.id());
			storedDocuments.put(uuid(opportunity.id(), opportunity.getClass()), opportunity);
		}
		for(final CommercialSubject commercialSubject: commercialSubjectRepository.entities()){
			System.out.println("add supject:" + commercialSubject.id());
			storedDocuments.put(uuid(commercialSubject.id(), commercialSubject.getClass()), commercialSubject);
		}
	}
	

	@Override
	public DocumentsAware forId(final Long id, final Class<? extends DocumentsAware> clazz) {
		return  storedDocuments.get(entityExistsGuard(id, uuid(id, clazz)));
	}

	private UUID uuid(final Long id, final Class<? extends DocumentsAware> clazz) {
		return new UUID(id, clazz.hashCode());
	}

	private UUID entityExistsGuard(final Long id, final UUID uuid) {
		if ( ! storedDocuments.containsKey(uuid) ) {
			throw new InvalidDataAccessApiUsageException("Entity not found: " + id);
		}
		return uuid;
	}

	@Override
	public void save(final DocumentsAware entity) {
		
		storedDocuments.put(uuid(entity.id(), entity.getClass()), entity);
	}

}
