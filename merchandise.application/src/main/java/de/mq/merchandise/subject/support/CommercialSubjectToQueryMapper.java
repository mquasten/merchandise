package de.mq.merchandise.subject.support;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.MapperQualifier.MapperType;
import de.mq.merchandise.support.Mapper;



@Component
@MapperQualifier(MapperType.CommercialSubject2QueryMap)
class CommercialSubjectToQueryMapper implements Mapper<CommercialSubject,Map<String,Object>> {

	static final long NOT_EXISTING_ID = -1L;

	final static String WILDCARD_PATTERN = "%";
	
	final static  String SEARCH_PATTERN = "%s%"+WILDCARD_PATTERN;

	@Override
	public Map<String, Object> mapInto(final CommercialSubject source, final Map<String, Object> target) {
		target.put(CommercialSubjectRepository.NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.ITEM_NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_DESCRIPTION_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_ID_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.CUSTOMER_NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_ID_PARAM, NOT_EXISTING_ID);
		target.put(CommercialSubjectRepository.CUSTOMER_ID_PARAM, NOT_EXISTING_ID);
		
		if( StringUtils.hasText(source.name())) {
			target.put(CommercialSubjectRepository.NAME_PARAM, String.format(SEARCH_PATTERN, source.name()));
		}
		if( source.customer() != null) {
			mapCustomer(source, target );
		}
		source.commercialSubjectItems().stream().findFirst().ifPresent(item -> mapSubjectItem(target, item));
		
		return target;
	}

	private void mapSubjectItem(final Map<String, Object> results, final CommercialSubjectItem item) {
		if( StringUtils.hasText(item.name())) {
			results.put(CommercialSubjectRepository.ITEM_NAME_PARAM, String.format(SEARCH_PATTERN,item.name()));
		}
		if( item.subject() != null){
			mapSubject(results, item.subject());
		}
	}

	private void mapSubject(final Map<String, Object> results, final Subject subject) {
		if( StringUtils.hasText(subject.name())){
			results.put(CommercialSubjectRepository.SUBJECT_NAME_PARAM,  String.format(SEARCH_PATTERN,subject.name()));
		}
		if( StringUtils.hasText(subject.description())){
			results.put(CommercialSubjectRepository.SUBJECT_DESCRIPTION_PARAM,  String.format(SEARCH_PATTERN, subject.description()));
		}
		if(subject.id().isPresent()){
			results.put(CommercialSubjectRepository.SUBJECT_ID_PARAM,  subject.id().get());
		}
	}

	private void mapCustomer(final CommercialSubject source, final Map<String, Object> results) {
		if( StringUtils.hasText(source.customer().name())){
			results.put(CommercialSubjectRepository.CUSTOMER_NAME_PARAM, String.format(SEARCH_PATTERN,source.customer().name()));
		}
		if( source.customer().id().isPresent() ) {
			results.put(CommercialSubjectRepository.CUSTOMER_ID_PARAM,  source.customer().id().get());
		}
	}
	
}
