package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;

import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.rule.support.RuleImpl;
import de.mq.merchandise.rule.support.RuleRepository;

@Repository
@Profile("mock")
public class DocumentEntityRepositoryMock  implements DocumentEntityRepository{
	
	
	
	
	
	private final Map<Class<? extends DocumentsAware>, BasicRepository<? extends DocumentsAware, Long> > repositories = new HashMap<>();
	
	DocumentEntityRepositoryMock() {
		
	}
	
	
	
	@Autowired
	DocumentEntityRepositoryMock(final OpportunityRepository opportunityRepository, final CommercialSubjectRepository commercialSubjectRepository, RuleRepository ruleRepository) {
		repositories.put(OpportunityImpl.class, opportunityRepository);
		repositories.put(CommercialSubjectImpl.class, commercialSubjectRepository);
		repositories.put(RuleImpl.class, ruleRepository);
		
	}

	
	

	@Override
	public DocumentsAware forId(final Long id, final Class<? extends DocumentsAware> clazz) {
		entityTypeGuard(clazz);
		return (DocumentsAware) repositories.get(clazz).forId(id);
	}




	private void entityTypeGuard(final Class<? extends DocumentsAware> clazz) {
		if( ! repositories.containsKey(clazz)){
			throw new InvalidDataAccessApiUsageException("Wrong type " + clazz + "not spported"  );
		}
	}

	

	

	@SuppressWarnings("unchecked")
	@Override
	public DocumentsAware save(final DocumentsAware entity) {
		entityTypeGuard(entity.getClass());
		return ((BasicRepository<DocumentsAware, Long>) repositories.get(entity.getClass())).save(entity);
	}



	@Override
	public void delete(final Long id) {
		final DocumentsAware whereTheWildRosesGrow = forId(id); 
		entityTypeGuard(whereTheWildRosesGrow.getClass());
		repositories.get(whereTheWildRosesGrow.getClass()).delete(id);
	}



	@Override
	public DocumentsAware forId(final Long id) {
		for(Class<? extends DocumentsAware> clazz : repositories.keySet()){
			
			
			final DocumentsAware  result = forId(id, clazz);
			if( result != null){
				return result;
			}
			
		}
		throw new InvalidDataAccessApiUsageException("Document not found in repositories, id " + id  );
	}
	
	
}
