package de.mq.merchandise.util.support;



import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.util.ReflectionTestUtils;








import com.vaadin.data.Item;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectImpl;
import de.mq.merchandise.subject.support.TestConstants;


public class DomainToItemConverterTest {
	
	
	private static final String EMPTY_STRING = "";
	private static final String ID_FIELD = "id";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String NAME_FIELD = "name";
	private static final String SUBJECT_DESC = "subjectDesc";
	private static final String SUBJECT_NAME = "subjectName";
	private static final Long ID = 19680528L;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	final Converter<Subject, Item> converter = new  DomainToItemConverterImpl(TestConstants.SUBJECT_COLS_CLASS);
	
	
	@Test
	public final void toWeb() {
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		ReflectionTestUtils.setField(subject, DESCRIPTION_FIELD, SUBJECT_DESC);
		ReflectionTestUtils.setField(subject, ID_FIELD, ID);
		Assert.assertNotNull(subject.created());
		
		final Item item = converter.convert(subject);
		Assert.assertEquals(4, item.getItemPropertyIds().size());
		Assert.assertEquals(ID, item.getItemProperty(TestConstants.SUBJECT_COLS_ID).getValue());
		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());
		Assert.assertEquals(SUBJECT_DESC, item.getItemProperty(TestConstants.SUBJECT_COLS_DESC).getValue());
		Assert.assertEquals(subject.created(),  item.getItemProperty(TestConstants.SUBJECT_COLS_DATE).getValue());
		
	}
	@Test
	public final void toNulls() {
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		Assert.assertNotNull(subject.created());
		final Item item = converter.convert(subject);
		Assert.assertEquals(-1L, item.getItemProperty(TestConstants.SUBJECT_COLS_ID).getValue());
		Assert.assertEquals(EMPTY_STRING, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());
		Assert.assertEquals(EMPTY_STRING, item.getItemProperty(TestConstants.SUBJECT_COLS_DESC).getValue());
		Assert.assertEquals(subject.created(),  item.getItemProperty(TestConstants.SUBJECT_COLS_DATE).getValue());
	}
	
	@Test
	public final void toWebCols() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Converter<Subject, Item> converter = new  DomainToItemConverterImpl(new Enum[] { TestConstants.SUBJECT_COLS_NAME});
		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);
		ReflectionTestUtils.setField(subject, NAME_FIELD, SUBJECT_NAME);
		ReflectionTestUtils.setField(subject, DESCRIPTION_FIELD, SUBJECT_DESC);
		ReflectionTestUtils.setField(subject, ID_FIELD, ID);
		Assert.assertNotNull(subject.created());
		
		final Item item = converter.convert(subject);
		
		Assert.assertEquals(SUBJECT_NAME, item.getItemProperty(TestConstants.SUBJECT_COLS_NAME).getValue());
		
		Assert.assertEquals(1, item.getItemPropertyIds().size());
	}

}
