package de.mq.merchandise.subject.support;

import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.ReflectionBasedFieldMapperImpl;

@Component
@CommercialSubjectModelQualifier(CommercialSubjectModelQualifier.Type.ItemToCommercialSubjectConverter)
public class ItemToCommercialSubjectConverterImpl extends ReflectionBasedFieldMapperImpl implements Converter<Item, CommercialSubject> {

	static final String ITEM_NAME_FIELD = "name";
	static final String SUBJECT_ID_FIELD = "id";
	static final String SUBJECT_DESC_FIELD = "description";
	static final String SUBJECT_NAME_FIELD = ITEM_NAME_FIELD;
	static final String COMMERCIAL_SUBJECT_NAME_FIELD = SUBJECT_NAME_FIELD;
	static final String COMMERCIAL_SUBJECT_ID_FIELD = SUBJECT_ID_FIELD;

	@Override
	public CommercialSubject convert(final Item source) {

		final CommercialSubject target = BeanUtils.instantiateClass(CommercialSubjectImpl.class);

		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);

		assign(SUBJECT_NAME_FIELD, subject, value(source, CommercialSubjectCols.SubjectName));
		assign(SUBJECT_DESC_FIELD, subject, value(source, CommercialSubjectCols.SubjectDesc));
		assign(SUBJECT_ID_FIELD, subject, -1L);

		target.assign(subject, ITEM_NAME_FIELD, false);

		final CommercialSubjectItem item = target.commercialSubjectItem(subject).get();
		assign(ITEM_NAME_FIELD, item, value(source, CommercialSubjectCols.ItemName));

		assign(SUBJECT_ID_FIELD, subject, null);

		assign(COMMERCIAL_SUBJECT_ID_FIELD, target, value(source, CommercialSubjectCols.Id));
		assign(COMMERCIAL_SUBJECT_NAME_FIELD, target, value(source, CommercialSubjectCols.Name));

		return target;
	}

	private <T> T value(final Item source, Object id) {
		@SuppressWarnings("unchecked")
		final Property<T> property = source.getItemProperty(id);
		if (property == null) {
			return null;
		}
		return property.getValue();
	}

}
