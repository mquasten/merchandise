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

	static final String CUSTOMER_ID_NAME = "customerId";
	static final String CUSTOMER_NAME_FIELD = "customerName";
	static final String SUBJECT_ID_FIELD = "subjectId";
	static final String SUBJECT_DESCRIPTION_FIELD = "subjectDescription";
	static final String SUBJECT_NAME_FIELD = "subjectName";
	static final String ITEM_NAME_FIELD = "itemName";
	static final String NAME_FIELD = "name";

	@Override
	public Map<String, Object> mapInto(final CommercialSubject source, final Map<String, Object> target) {
		if( StringUtils.hasText(source.name())) {
			target.put(NAME_FIELD, source.name());
		}
		if( source.customer() != null) {
			mapCustomer(source, target);
		}
		source.commercialSubjectItems().stream().findFirst().ifPresent(item -> mapSubjectItem(target, item));
		
		return target;
	}

	private void mapSubjectItem(final Map<String, Object> results, final CommercialSubjectItem item) {
		if( StringUtils.hasText(item.name())) {
			results.put(ITEM_NAME_FIELD, item.name());
		}
		if( item.subject() != null){
			mapSubject(results, item.subject());
		}
	}

	private void mapSubject(final Map<String, Object> results, final Subject subject) {
		if( StringUtils.hasText(subject.name())){
			results.put(SUBJECT_NAME_FIELD, subject.name());
		}
		if( StringUtils.hasText(subject.description())){
			results.put(SUBJECT_DESCRIPTION_FIELD, subject.description());
		}
		if(subject.id().isPresent()){
			results.put(SUBJECT_ID_FIELD, subject.id().get());
		}
	}

	private void mapCustomer(final CommercialSubject source, final Map<String, Object> results) {
		if( StringUtils.hasText(source.customer().name())){
			results.put(CUSTOMER_NAME_FIELD, (source.customer().name()));
		}
		if( source.customer().id().isPresent() ) {
			results.put(CUSTOMER_ID_NAME, (source.customer().id().get()));
		}
	}
	
}
