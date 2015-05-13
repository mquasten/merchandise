package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.mockito.Mockito;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Item;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.TableContainerColumns;

public class TestConstants {
	
	
	public static  Enum<? extends TableContainerColumns> SUBJECT_COLS_ID = SubjectCols.Id; 
	public static  Enum<? extends TableContainerColumns> SUBJECT_COLS_NAME = SubjectCols.Name; 
	
	public static final  Class<? extends Converter<?, Item>>  SUBJECT_COMVERTER_CLASS = SubjectConverterImpl.class;
	
	public static final  Converter<?, Item>  CONVERTER  = new SubjectConverterImpl();
	
	public static  final Class<?extends Object> CONTROLLER_TARGET =  SubjectModelControllerImpl.class;
	
	static final SubjectModelControllerImpl controller =  (SubjectModelControllerImpl) Mockito.mock(CONTROLLER_TARGET);
	
	public static final Object controller() {
		Mockito.when(controller.countSubjects(Mockito.any(SubjectModel.class))).thenReturn(new Long(1));
		Collection<Subject> results = new ArrayList<>();
		final Subject subject = Mockito.mock(Subject.class);
		Mockito.when(subject.id()).thenReturn(Optional.of(4711L));
		Mockito.when(subject.name()).thenReturn("Name");
		results.add(subject);
		
		
		Mockito.when(controller.subjects(Mockito.any(SubjectModel.class), Mockito.any(ResultNavigation.class))).thenReturn(results);
		return controller;
	}
				

	
	

}
