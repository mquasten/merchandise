package de.mq.merchandise.util.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SimpleSpringBeanResolver implements BeanResolver {
	
	@Autowired
	private ApplicationContext ctx ; 
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.support.BeanResolver#resolve(java.util.Map, java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final <T> T resolve(final Map<Class<?>, Object> beans , final Class<? extends T> clazz){
		if( beans.containsKey(clazz)){
			return  (T) beans.get(clazz);
		}
		
		return ctx.getBean(clazz);
		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.support.BeanResolver#resolve(java.lang.Class)
	 */
	@Override
	public final <T> T resolve(final Class<? extends T> clazz){
		return ctx.getBean(clazz);
		
	}
	
	@Override
	public final <T> Collection<T> resolveAll(final Class<? extends T> clazz, @SuppressWarnings("unchecked") final T ... beans){
		final Collection<T> results =  new HashSet<>();
	    results.addAll(Arrays.asList(beans));
		results.addAll(ctx.getBeansOfType(clazz).values());
		return Collections.unmodifiableCollection(results);
	}

}
