package de.mq.merchandise.subject.support;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import org.springframework.beans.factory.annotation.Qualifier;



@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
public @interface SubjectModelQualifier {
	Type value();
	
	
	public  enum Type {
		LazyQueryContainer,
		SubjectSearchItem,
		SubjectEditItem,
		SubjectModel,
		ItemToSubjectConverter
	}
}
