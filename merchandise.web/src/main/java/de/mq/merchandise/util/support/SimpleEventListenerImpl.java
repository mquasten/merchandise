package de.mq.merchandise.util.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.subject.support.SubjectEventQualifier;
import de.mq.merchandise.util.Event;

@Controller()
class SimpleEventListenerImpl implements ApplicationContextAware {

	private static final String ANNOTATION_DEFAULT_VALUE_METHOD = "value";
	private final Map<Object, Method> methods = new HashMap<>();
	private final Map<Object, Object> targets = new HashMap<>();

	private Set<Class<? extends Annotation>> qualifiers = new HashSet<>();
	
	private ApplicationContext applicationContext;

	SimpleEventListenerImpl() {
		qualifiers.add(SubjectEventQualifier.class);
	}

	@SuppressWarnings("unchecked")
	@EventListener
	<T,R> void processEvent(final Event<T,R> event) {
			final Method method = methods.get(event.id());
			final Object target = targets.get(event.id());
			Assert.notNull(method, "Method not found.");
			Assert.notNull(target, "Target not found.");
			method.setAccessible(true);
			final List<Object> arguments = Arrays.asList(method.getParameterTypes()).stream().map(type -> getBean(type, event.parameter())).collect(Collectors.toList());
			
			final Object result = ReflectionUtils.invokeMethod(method, target, arguments.toArray(new Object[arguments.size()]));
	      if( result == null) {
	      	return;
	      }
	      event.assign((R) result);
	      
	   
	}
	
	private Object getBean(Class<?> clazz, Map<Class<?>, Object> beans){
		if(beans.containsKey(clazz)){
			return beans.get(clazz);
		}
		return applicationContext.getBean(clazz);
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext)  {
		this.applicationContext=applicationContext;
		final Set<Object> controllers = applicationContext.getBeansWithAnnotation(Controller.class).values().stream().collect(Collectors.toSet());
		
	
		controllers.forEach(obj -> Arrays.asList(obj.getClass().getDeclaredMethods()).stream().forEach(m -> Arrays.asList(m.getAnnotations()).stream().filter(a -> qualifiers.contains(a.annotationType()) && ReflectionUtils.findMethod(a.annotationType(), ANNOTATION_DEFAULT_VALUE_METHOD) != null).forEach(a -> {
			final Object event = ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(a.annotationType(), ANNOTATION_DEFAULT_VALUE_METHOD), a);
			methods.put(event, m);
			targets.put(event, obj);
		})));
	}

}


