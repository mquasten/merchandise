package de.mq.merchandise.subject.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.beans.factory.annotation.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface SubjectMapper {
	
	public enum SubjectMapperType{
		Subject2Subject, Customer2Subject
	}

	
	
	SubjectMapperType value() default SubjectMapperType.Subject2Subject ;
}


