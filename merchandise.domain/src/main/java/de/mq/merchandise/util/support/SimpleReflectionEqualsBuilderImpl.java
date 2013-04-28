package de.mq.merchandise.util.support;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.persistence.Id;

import org.hibernate.proxy.HibernateProxy;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import de.mq.merchandise.util.Equals;
import de.mq.merchandise.util.EqualsBuilder;

public class SimpleReflectionEqualsBuilderImpl implements EqualsBuilder {
	
	
	static final  UUID OBJECT_UUID = UUID.randomUUID();
	static final  UUID ID_UUID = UUID.randomUUID();
	
	private final Map<UUID,Object>sourceFields=new HashMap<>();
	private final Map<UUID,Object>targetFields=new HashMap<>();
	private Class<?> clazz = Object.class;
	private boolean nulls=false;
	
	private boolean idNulls=false;
	
	SimpleReflectionEqualsBuilderImpl() {
		
	}
	
	
	@Override
	public final EqualsBuilder  withSource(final Object source) {
		sourceFields.clear();
		sourceFields.putAll(fieldValues( deProxymate(source)));
		return this;
	}


	private Object deProxymate(final Object source) {
		if (source instanceof HibernateProxy) {
			return  ((HibernateProxy) source).getHibernateLazyInitializer().getImplementation();
		}
		return source;
	}
	
	@Override
	public final EqualsBuilder  withTarget(final Object target) {
		targetFields.clear();
		targetFields.putAll(fieldValues(deProxymate(target)));
		return this;
		
	}
	
	@Override
	public final EqualsBuilder forInstance(Class<?> clazz) {
		this.clazz=clazz;
		return this;
	}
	
	@Override
	public final boolean isEquals() {
		
		idsExistsGuard(sourceFields, "source");
		idsExistsGuard(targetFields, "target");
		
		if(!clazz.isInstance(targetFields.get(OBJECT_UUID))){
			return false;
		}
		
		if( idAWare(sourceFields) && idAWare(targetFields)){
			return id(sourceFields)==id(targetFields);
		}
		
		
		if( sourceFields.size() <= 2){
			return equalsSuper();
		}
		
		if(nulls){
			return equalsSuper();
		}
		
		for(final Entry<UUID,Object> entry : sourceFields.entrySet()){
			if(entry.getKey() == OBJECT_UUID){
				continue;
			}
			
			if(entry.getKey() == ID_UUID){
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
		idsExistsGuard(sourceFields, "source");
	
		if(idAWare(sourceFields)) {
			return id(sourceFields);
		}
		
		if( sourceFields.size() <= 2){
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
			if(entry.getKey() == ID_UUID){
				continue;
			}
			result+=entry.getValue().hashCode();
		}
		return result;
		
	}


	private int id(final Map<UUID,Object> values) {
		return (int) values.get(ID_UUID);
	}


	private boolean idAWare(final Map<UUID,Object> values) {
		if( idNulls) {
			return false;
		}
		return ((int) values.get(ID_UUID) != 0 );
	}

	private void idsExistsGuard(final Map<UUID,Object> map , final String name ) {
		if( ! map.containsKey(OBJECT_UUID)){
			 throw new IllegalStateException("ObjectIdentity is missing on " + name );
		}
		if (! map.containsKey(ID_UUID)) {
			throw new IllegalStateException("Id Hash is missing on "+ name);
		}
	}

	

	

	private Map<UUID,Object>  fieldValues(final Object source) {
		final Map<UUID,Object> values = new HashMap<>();
		values.put(ID_UUID, 0);
		values.put(OBJECT_UUID,  deProxymate(source));
		ReflectionUtils.doWithFields(source.getClass(), new FieldCallback() {
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				field.setAccessible(true);
				handleField(source, values, field);
			}

			
		}, new FieldFilter() {
			
			@Override
			public boolean matches(final Field field) {
				return field.isAnnotationPresent(Equals.class)||field.isAnnotationPresent(Id.class);
			}
		});
		return values;
	}
	
	private void handleField(final Object source, final Map<UUID, Object> values, final Field field) throws IllegalAccessException {
		final Object value = field.get(source);
		
		if( field.isAnnotationPresent(Id.class) ){
			handleIdField(values, field, value);
		}
		
		if(field.isAnnotationPresent(Equals.class) ){
		    handleEqualsField(values, field, value);
		}
	}


	private void handleEqualsField(final Map<UUID, Object> values, final Field field, final Object value) {
		if(  value==null){
			nulls=true;
			return;
		}
		
	    values.put(new UUID(field.getDeclaringClass().hashCode(),field.hashCode()), value);
	}


	private void handleIdField(final Map<UUID, Object> values, final Field field, final Object value) {

		if(value==null){
			idNulls=true;
			return;
		}
		
		values.put(ID_UUID,(int) values.get(ID_UUID)+value.hashCode());
	}
	
	
	
	
	
	

	private boolean equalsSuper() {
		return sourceFields.get(OBJECT_UUID) == targetFields.get(OBJECT_UUID);
	}
	
	
}
