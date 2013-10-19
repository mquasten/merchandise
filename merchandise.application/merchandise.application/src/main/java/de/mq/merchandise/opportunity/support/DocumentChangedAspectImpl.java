package de.mq.merchandise.opportunity.support;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.BasicEntity;
import de.mq.merchandise.BasicRepository;
import de.mq.merchandise.util.AbstractRepository;

@Component
@Aspect
public class DocumentChangedAspectImpl {
	
	private final EntityContextRepository entityContextRepository;
	

	private Map<Class<?>,Resource> resources = new HashMap<>();
	
	@Autowired
	public DocumentChangedAspectImpl(final EntityContextRepository entityContextRepository) {
		this.entityContextRepository=entityContextRepository;
		for(final Resource resource : Resource.values()){
			resources.put(resource.entityClass(), resource);
		}
	}
	
	@AfterReturning(value="execution(* de.mq.merchandise.util.AbstractRepository.save(..))",  returning="result")
	public void entityContextCreateOrUpdate(final  BasicEntity result) {
		if( ! resources.containsKey(result.getClass())){
			return;
		}
		entityContextRepository.save(new EntityContextImpl(result.id(), resources.get(result.getClass()) ));
	}
	
	@AfterReturning(value="execution(* de.mq.merchandise.util.AbstractRepository.delete(..)) && target(bean) && args(id) ",  argNames="id,bean")
	public void entityContextDelete(final Long id, final BasicRepository<DocumentsAware, Long> bean ) {
	         
				final Method method = ReflectionUtils.findMethod(bean.getClass(), "entityImplementationClass");
				methodExistsGuard(bean, method);
				method.setAccessible(true);
				
				final Resource  resource = resources.get(ReflectionUtils.invokeMethod(method, bean));
				if( resource == null){
					return;
				}
				entityContextRepository.save(new EntityContextImpl(id , resource ,true));
	         
	}

	private void methodExistsGuard(final BasicRepository<DocumentsAware, Long> bean, final Method method) {
		if( method == null){
			throw new IllegalArgumentException("Method  entityImplementationClass() not fond on repository implementation: " + bean.getClass() );
		}
	}

}
