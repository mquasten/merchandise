package de.mq.merchandise.subject.support;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectMapper.SubjectMapperType;
import de.mq.merchandise.support.Mapper;



@Component
@SubjectMapper(SubjectMapperType.CommercialSubject2QueryMap)
class CommercialSubjectToQueryMapper implements Mapper<CommercialSubject,Map<String,Object>> {

	final static String WILDCARD_PATTERN = "%";
	
	final static private String serachPattern = "%s%"+WILDCARD_PATTERN;

	@Override
	public Map<String, Object> mapInto(final CommercialSubject source, final Map<String, Object> target) {
		target.put(CommercialSubjectRepository.NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.ITEM_NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_DESCRIPTION_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_ID_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.CUSTOMER_NAME_PARAM, WILDCARD_PATTERN);
		target.put(CommercialSubjectRepository.SUBJECT_ID_PARAM, "-1");
		target.put(CommercialSubjectRepository.CUSTOMER_ID_PARAM, "-1");
		
		if( StringUtils.hasText(source.name())) {
			target.put(CommercialSubjectRepository.NAME_PARAM, String.format(serachPattern, source.name()));
		}
		if( source.customer() != null) {
			mapCustomer(source, target );
		}
		source.commercialSubjectItems().stream().findFirst().ifPresent(item -> mapSubjectItem(target, item));
		
		return target;
	}

	private void mapSubjectItem(final Map<String, Object> results, final CommercialSubjectItem item) {
		if( StringUtils.hasText(item.name())) {
			results.put(CommercialSubjectRepository.ITEM_NAME_PARAM, String.format(serachPattern,item.name()));
		}
		if( item.subject() != null){
			mapSubject(results, item.subject());
		}
	}

	private void mapSubject(final Map<String, Object> results, final Subject subject) {
		if( StringUtils.hasText(subject.name())){
			results.put(CommercialSubjectRepository.SUBJECT_NAME_PARAM,  String.format(serachPattern,subject.name()));
		}
		if( StringUtils.hasText(subject.description())){
			results.put(CommercialSubjectRepository.SUBJECT_DESCRIPTION_PARAM,  String.format(serachPattern, subject.description()));
		}
		if(subject.id().isPresent()){
			results.put(CommercialSubjectRepository.SUBJECT_ID_PARAM,  subject.id().get());
		}
	}

	private void mapCustomer(final CommercialSubject source, final Map<String, Object> results) {
		if( StringUtils.hasText(source.customer().name())){
			results.put(CommercialSubjectRepository.CUSTOMER_NAME_PARAM, String.format(serachPattern,source.customer().name()));
		}
		if( source.customer().id().isPresent() ) {
			results.put(CommercialSubjectRepository.CUSTOMER_ID_PARAM,  source.customer().id().get());
		}
	}
	
}
