package de.mq.merchandise.domain.support;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Optional;


import java.util.Set;

import javax.persistence.Id;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.util.ReflectionUtils;

/**
 * Alle Entitaeten implementieren dieses Interface.
 * Zugriff auf den PrimaerSchluessel 
 * @author Admin
 *
 */
public interface BasicEntity {
	
	/**
	 * Der Primaerschluessel der Entity
	 * @return
	 */
	default  Optional<Long> id() {
		final Set<Field> ids = new HashSet<>();
		ReflectionUtils.doWithFields(getClass(), f -> ids.add(f), f -> f.isAnnotationPresent(Id.class));
		
		DataAccessUtils.requiredSingleResult(ids);
		final Field field = ids.stream().findFirst().get();
		field.setAccessible(true);
		final Long id =  (Long) ReflectionUtils.getField(field, this);
		
		if( id == null){
			return Optional.empty();
		}
		return Optional.of(id);
	}

}
