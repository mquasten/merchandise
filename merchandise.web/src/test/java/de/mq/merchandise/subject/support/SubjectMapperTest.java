package de.mq.merchandise.subject.support;

import java.util.Date;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.subject.Subject;

public class SubjectMapperTest {
	
	
	private static final Long ID = 19680528L;
	private static final String NAME = "Crime Bank";
	private static final String DESCRIPTION = "Vito Corleone,s Banking Service";
	private Converter<Subject, Item> converter = new SubjectConverterImpl();
	private Date date = new Date();
	
	@Test
	public final void convert() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(subject.id()).thenReturn(Optional.of(ID));
		Mockito.when(subject.name()).thenReturn(NAME);
		Mockito.when(subject.description()).thenReturn(DESCRIPTION);
		Mockito.when(subject.created()).thenReturn(date);
		final Item item = converter.convert(subject);
		Assert.assertEquals(ID, item.getItemProperty(SubjectCols.Id).getValue());
		Assert.assertEquals(NAME, item.getItemProperty(SubjectCols.Name).getValue());
		Assert.assertEquals(DESCRIPTION, item.getItemProperty(SubjectCols.Description).getValue());
	}
	
	@Test
	public final void convertNulls() {
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(subject.id()).thenReturn(Optional.empty());
		final Item item = converter.convert(subject);
		Assert.assertEquals(0, item.getItemPropertyIds().size());
	}
	
	
	
	

}
