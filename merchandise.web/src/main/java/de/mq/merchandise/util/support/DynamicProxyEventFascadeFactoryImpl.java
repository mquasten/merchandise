package de.mq.merchandise.util.support;

import java.lang.reflect.Proxy;






import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.util.EventFascadeProxyFactory;

@Component
@EventFascadeProxyFactory.EventFascadeProxyFactoryQualifier(EventFascadeProxyFactory.FactoryType.DynamicProxy)
class DynamicProxyEventFascadeFactoryImpl extends AbstractEventFascadeProxyFactory {

	@Autowired
	DynamicProxyEventFascadeFactoryImpl(final ApplicationEventPublisher applicationEventPublisher, final EventAnnotationOperations eventAnnotationOperations) {
		super(applicationEventPublisher, eventAnnotationOperations);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T createProxy(Class<? extends T> targetClass) {
		
	
		return  (T) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[] { targetClass }, (object, method, args) -> {
			
			if( ! super.isAnnotated(method) ) {
				return ReflectionUtils.invokeMethod(method, object, args);
			}
			
			return invoke(method, args);
			
		});
	}

}
