package de.mq.merchandise.subject.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface MapperQualifier {
	
	public enum MapperType{
		Subject2Subject, Customer2Subject, Condition2Subject,
		
		CommercialSubject2CommercialSubject,
		CommercialSubject2QueryMap,
		CommercialSubjectItemIntoCommercialSubject
	}

	
	
	MapperType value() default MapperType.Subject2Subject ;
}


