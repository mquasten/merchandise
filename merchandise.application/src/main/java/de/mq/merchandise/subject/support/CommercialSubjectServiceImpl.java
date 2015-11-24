package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.support.Mapper;

@Transactional(readOnly=true)
@Service
class CommercialSubjectServiceImpl implements CommercialSubjectService {
	
	private final Mapper<CommercialSubject,Map<String,Object>>  mapper;
	
	private final CommercialSubjectRepository commercialSubjectRepository;
	
	@Autowired
	public CommercialSubjectServiceImpl(final CommercialSubjectRepository commercialSubjectRepository, final Mapper<CommercialSubject, Map<String, Object>> mapper) {
		this.commercialSubjectRepository=commercialSubjectRepository;
		this.mapper = mapper;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectService#commercialSubjects(de.mq.merchandise.subject.support.CommercialSubject, de.mq.merchandise.ResultNavigation)
	 */
	@Override
	@Transactional(readOnly=true)
	public final Collection<CommercialSubject>commercialSubjects(final CommercialSubject commercialSubject, final ResultNavigation resultNavigation  ) {
		return Collections.unmodifiableCollection(commercialSubjectRepository.commercialSubjectsForCustomer(queryMap(commercialSubject),resultNavigation));
		
	}

	private Map<String, Object> queryMap(final CommercialSubject commercialSubject) {
		final Map<String,Object> queryMap = new HashMap<>();
		return mapper.mapInto(commercialSubject, queryMap);
		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectService#commercialSubjects(de.mq.merchandise.subject.support.CommercialSubject)
	 */
	@Override
	@Transactional(readOnly=true)
	public final Number commercialSubjects(final CommercialSubject commercialSubject) {
		final Number counter = commercialSubjectRepository.countCommercialSubjectsForCustomer(queryMap(commercialSubject));
		Assert.notNull(counter);
		return counter;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectService#save(de.mq.merchandise.subject.support.CommercialSubject)
	 */
	@Override
	@Transactional(readOnly=false , propagation=Propagation.REQUIRED)
	public final void save(final CommercialSubject commercialSubject) {
		commercialSubjectRepository.save(commercialSubject);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectService#commercialSubject(java.lang.Long)
	 */
	@Override
	@Transactional(readOnly=true)
	public final CommercialSubject  commercialSubject(final Long id){
		final CommercialSubject result = commercialSubjectRepository.commercialSubject(id);
		
		result.commercialSubjectItems().forEach(i -> {Hibernate.initialize(i); Hibernate.initialize(i.subject());Hibernate.initialize(i.subject().customer());Hibernate.initialize((i.subject().conditions()));  i.conditionValues().stream().map(e -> e.getKey()).forEach(c -> Hibernate.initialize(c));} );
		
		Assert.notNull(result , "CommercialSubject not found");
		return result;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectService#remove(de.mq.merchandise.subject.support.CommercialSubject)
	 */
	@Override
	@Transactional(readOnly=false , propagation=Propagation.REQUIRED)
	public final void remove (final CommercialSubject commercialSubject){
		commercialSubjectRepository.remove(commercialSubject);
	}

}
