package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;


import org.mockito.Mockito;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.CollectionUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.PropertysetItem;

import de.mq.merchandise.ResultNavigation;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.util.TableContainerColumns;

public class TestConstants {
	
	
	public  static final String SUBJECT_DESCRIPTION = "Pets for you";
	public static final String SUBJECT_NAME = "PetStore";
	private static final Subject SUBJECT = Mockito.mock(Subject.class);
	public static final Long ID = 19680528L;
	public static  Enum<? extends TableContainerColumns> SUBJECT_COLS_ID = SubjectCols.Id; 
	public static  Enum<? extends TableContainerColumns> SUBJECT_COLS_NAME = SubjectCols.Name; 
	public static  Enum<? extends TableContainerColumns> SUBJECT_COLS_DESC = SubjectCols.Description; 
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Class<Enum<? extends TableContainerColumns>> SUBJECT_COLS_CLASS = (Class )SubjectCols.class;
	
	
	
	public static final Object subjectControllerMock() {
		final SubjectModelControllerImpl controller =  (SubjectModelControllerImpl) Mockito.mock( SubjectModelControllerImpl.class);
		Mockito.when(controller.countSubjects(Mockito.any(SubjectModel.class))).thenReturn(new Long(1));
		final Collection<Subject> results = new ArrayList<>();
		results.add(SUBJECT);
		Mockito.when(controller.subjects(Mockito.any(SubjectModel.class), Mockito.any(ResultNavigation.class))).thenReturn(results);
		return controller;
	}
				

	public static final Converter<Subject, Item> subjectConverterMock() {
		 @SuppressWarnings("unchecked")
		 final Converter<Subject, Item> converter  = Mockito.mock(Converter.class);
		 Mockito.when(converter.convert(SUBJECT)).thenReturn(itemMock());
		
		return converter;
	}


	public  static Item itemMock() {
		final Item item = new PropertysetItem();			
		 item.addItemProperty(SubjectCols.Id,  new ObjectProperty<>(ID));
		 item.addItemProperty(SubjectCols.Name,  new ObjectProperty<>(SUBJECT_NAME));
		 item.addItemProperty(SubjectCols.Description,  new ObjectProperty<>(SUBJECT_DESCRIPTION));
		return item;
	}
	

	@SuppressWarnings("unchecked")
	public static final Collection<TableContainerColumns> subjectColumns () {
		return CollectionUtils.arrayToList(SubjectCols.values());
	}

}
