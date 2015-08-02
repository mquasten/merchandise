package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.support.Mapper;

@Transactional(readOnly=true)
public class CommercialSubjectServiceImpl implements CommercialSubjectService {
	
	private final Mapper<CommercialSubject,Map<String,Object>>  mapper;
	
	private final CommercialSubjectRepository commercialSubjectRepository;
	
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
		mapper.mapInto(commercialSubject, queryMap);
		return queryMap;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.subject.support.CommercialSubjectService#commercialSubjects(de.mq.merchandise.subject.support.CommercialSubject)
	 */
	@Override
	@Transactional(readOnly=true)
	public final Number commercialSubjects(final CommercialSubject commercialSubject) {
		return commercialSubjectRepository.countCommercialSubjectsForCustomer(queryMap(commercialSubject));
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
