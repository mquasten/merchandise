package de.mq.merchandise.util.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.subject.support.SubjectEventQualifier;
import de.mq.merchandise.util.Event;


@Controller()
class SimpleEventListenerImpl  implements ApplicationContextAware{
	
	final Map<Object,Method> methods = new HashMap<>();
	
	private Set<Class<? extends Annotation>> qualifiers = new HashSet<>() ; 
	
	SimpleEventListenerImpl() {
		qualifiers.add(SubjectEventQualifier.class);
	}
	
	@EventListener
	<T> void test(final Event<T> event) {
		System.out.println(methods);
		System.out.println("****");
		System.out.println(event.id() );
		//event.assign(  "hottest artist ever");
		
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
		final Set<Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class).values().stream().collect(Collectors.toSet());
	    controllers.forEach(obj -> Arrays.asList(obj.getClass().getDeclaredMethods()).stream().forEach(m -> Arrays.asList(m.getAnnotations()).stream().filter( a ->  qualifiers.contains(a.annotationType()) && ReflectionUtils.findMethod(a.annotationType(), "value") != null && ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(a.annotationType(), "value"), a) != null).forEach(a ->methods.put( ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(a.annotationType(), "value"), a) , m))));
	}

	

	

	

	

	

}
