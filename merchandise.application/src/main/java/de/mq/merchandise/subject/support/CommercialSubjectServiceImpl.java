package de.mq.merchandise.subject.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import de.mq.merchandise.support.Mapper;

public class CommercialSubjectServiceImpl {
	
	private final Mapper<CommercialSubject,Map<String,Object>>  mapper;
	
	private final CommercialSubjectRepository commercialSubjectRepository;
	
	public CommercialSubjectServiceImpl(final CommercialSubjectRepository commercialSubjectRepository, final Mapper<CommercialSubject, Map<String, Object>> mapper) {
		this.commercialSubjectRepository=commercialSubjectRepository;
		this.mapper = mapper;
	}

	@Transactional(readOnly=true)
	public final Collection<CommercialSubject>commercialSubjects(final CommercialSubject commercialSubject  ) {
		final Map<String,Object> queryMap = new HashMap<>();
		mapper.mapInto(commercialSubject, queryMap);
		return Collections.unmodifiableCollection(commercialSubjectRepository.forCriteria(queryMap));
		
	}

}
