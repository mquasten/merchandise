package de.mq.merchandise.util.support;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import de.mq.merchandise.util.Equals;
import de.mq.merchandise.util.EqualsBuilder;

public class SimpleReflectionEqualsBuilderImpl implements EqualsBuilder {
	
	
	static final  UUID OBJECT_UUID = UUID.randomUUID();
	
	
	private final Map<UUID,Object>sourceFields=new HashMap<>();
	private final Map<UUID,Object>targetFields=new HashMap<>();
	private Class<?> clazz = Object.class;
	private boolean nulls=false;
	
	SimpleReflectionEqualsBuilderImpl() {
		
	}
	
	
	@Override
	public final EqualsBuilder  withSource(final Object source) {
		sourceFields.clear();
		sourceFields.putAll(fieldValues(source));
		sourceFields.put(OBJECT_UUID, source);
		return this;
	}
	
	@Override
	public final EqualsBuilder  withTarget(final Object target) {
		targetFields.clear();
		targetFields.putAll(fieldValues(target));
		targetFields.put(OBJECT_UUID, target);
		return this;
		
	}
	
	@Override
	public final EqualsBuilder forInstance(Class<?> clazz) {
		this.clazz=clazz;
		return this;
	}
	
	@Override
	public final boolean isEquals() {
		
		sourceObjectExistsGuard();
		targetObjectExistsGuard();
		
		if(!clazz.isInstance(targetFields.get(OBJECT_UUID))){
			return false;
		}
		
		if( sourceFields.size() <= 1){
			return equalsSuper();
		}
		
		if(nulls){
			return equalsSuper();
		}
		
		for(final Entry<UUID,Object> entry : sourceFields.entrySet()){
			if(entry.getKey() == OBJECT_UUID){
				continue;
			}
			
			if( ! targetFields.containsKey(entry.getKey())){
				return equalsSuper();
			}
			
			if( ! entry.getValue().equals(targetFields.get(entry.getKey()))) {
				return equalsSuper();
			}
		}
		return true;
		
	}
	
	public final int buildHashCode() {
		sourceObjectExistsGuard();
		if( sourceFields.size() <= 1){
			return System.identityHashCode(sourceFields.get(OBJECT_UUID));
		}
		if( nulls){
			return System.identityHashCode(sourceFields.get(OBJECT_UUID));
		}
		
		int result=0;
		for(final Entry<UUID,Object> entry : sourceFields.entrySet()){
			if(entry.getKey() == OBJECT_UUID){
				continue;
			}
			result+=entry.getValue().hashCode();
		}
		return result;
		
	}

	private void targetObjectExistsGuard() {
		if( ! targetFields.containsKey(OBJECT_UUID)){
			 throw new IllegalStateException("Target is missing");
		}
	}

	private void sourceObjectExistsGuard() {
		if (! sourceFields.containsKey(OBJECT_UUID)) {
			throw new IllegalStateException("Source is missing");
		}
	}

	

	private Map<UUID,Object>  fieldValues(final Object source) {
		final Map<UUID,Object> values = new HashMap<>();
		ReflectionUtils.doWithFields(source.getClass(), new FieldCallback() {
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				field.setAccessible(true);
				handleField(source, values, field);
			}

			
		}, new FieldFilter() {
			
			@Override
			public boolean matches(final Field field) {
				return field.isAnnotationPresent(Equals.class);
			}
		});
		return values;
	}
	
	private void handleField(final Object source, final Map<UUID, Object> values, final Field field) throws IllegalAccessException {
		final Object value = field.get(source);
		if(  value==null){
			nulls=true;
			return;
		}
		
	    values.put(new UUID(field.getDeclaringClass().hashCode(),field.hashCode()), value);
	}
	
	
	
	
	
	

	private boolean equalsSuper() {
		return sourceFields.get(OBJECT_UUID) == targetFields.get(OBJECT_UUID);
	}
	
	
}
