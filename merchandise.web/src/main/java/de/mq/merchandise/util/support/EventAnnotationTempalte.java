package de.mq.merchandise.util.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.subject.support.SubjectEventQualifier;

@Component
class EventAnnotationTempalte implements EventAnnotationOperations {
	
	private Collection<Class<? extends Annotation>> qualifiers = new HashSet<>();
	
	private  final String valueMethodName = "value";
	
	EventAnnotationTempalte() {
		add(SubjectEventQualifier.class);
	}
	


	/* (non-Javadoc)
	 * @see de.mq.merchandise.util.support.EventAnnotationOperations#valueFromAnnotation(java.lang.reflect.Method)
	 */
	@Override
	public Object valueFromAnnotation(final Method method) {
		final Optional<Annotation> annotation = annotation(method);
		Assert.isTrue(annotation.isPresent(), "Method should be annotated, something is wrong.");

		final Method valueMethod = ReflectionUtils.findMethod(annotation.get().getClass(), valueMethodName);
		Assert.notNull(valueMethod, valueMethodName + " in Annotation not Found");
		final Object eventId = ReflectionUtils.invokeMethod(valueMethod, annotation.get());
		return eventId;
	}


	private  Optional<Annotation> annotation(final Method method) {
		return   Arrays.asList(method.getAnnotations()).stream().filter(a -> qualifiers.contains(a.annotationType()) && ReflectionUtils.findMethod(a.annotationType(), valueMethodName) != null).findFirst();
	 
	}
	
	private void add(Class<? extends Annotation> qualifier){
		qualifiers.add(qualifier);
	}



	/*
	 * (non-Javadoc)
	 * @see de.mq.merchandise.util.support.EventAnnotationOperations#isAnnotaionPresent(java.lang.reflect.Method)
	 */
	@Override
	public boolean isAnnotaionPresent(final Method method) {
		return annotation(method).isPresent();
	}

}
