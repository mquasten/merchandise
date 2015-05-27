package de.mq.merchandise.subject.support;

import java.util.Collection;

import org.springframework.util.CollectionUtils;

import de.mq.merchandise.util.TableContainerColumns;

public class TestConstants {
	
	
	
	public static  final Enum<? extends TableContainerColumns>  SUBJECT_COLS_ID = SubjectCols.Id; 
	public static  final Enum<? extends TableContainerColumns> SUBJECT_COLS_NAME = SubjectCols.Name; 
	public static  final  Enum<? extends TableContainerColumns> SUBJECT_COLS_DESC = SubjectCols.Description; 
	

	@SuppressWarnings("unchecked")
	public static  final Collection<TableContainerColumns> SUBJECT_COLS = CollectionUtils.arrayToList(SubjectCols.values());
	
	public static  final Class<? extends Enum<? extends TableContainerColumns>> SUBJECT_COLS_CLASS = SubjectCols.class;
				




	
	


}
