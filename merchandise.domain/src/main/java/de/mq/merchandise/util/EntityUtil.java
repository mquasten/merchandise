package de.mq.merchandise.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Locale;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;

import de.mq.merchandise.util.support.SimpleReflectionEqualsBuilderImpl;

public  abstract   class EntityUtil {

	final static  Class<? extends EqualsBuilder> EQUALSBUILDER_CLASS = SimpleReflectionEqualsBuilderImpl.class;


	public static final void idAware(final Long id) {
		if (id == null) {
			throw new IllegalStateException("Id not set, object not persistent now");
		}
	}
	
	
	public static final void mandatoryGuard(final String value , String name){
		notNullGuard(value, name);
		
		if( value.trim().length() == 0){
			throw new IllegalArgumentException("Field " + name +" is mandatory");
		}
	}


	public static void notNullGuard(final Object value, String name) {
		if( value == null){
			throw new IllegalArgumentException("Field " + name +" is mandatory");
		}
	}
	
	
	public static final EqualsBuilder equalsBuilder() {
		try {
			final Constructor<? extends EqualsBuilder> constructor =  EQUALSBUILDER_CLASS.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch (final InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException ex) {
			throw new IllegalStateException(ex);
		}
	}
	
	public static Locale locale(final String locale){
		if( locale == null){
			return null;
		}
		
		if( locale.trim().isEmpty()){
			return null;
		}
		final String[] values = locale.trim().split("_");
		
		if(values.length==1){
			return new Locale(values[0].trim());
		}
		
		if(values.length==3){
			return new Locale(values[0].trim(), values[1].trim(), values[2].trim());
		}
		
		return new Locale(values[0].trim(), values[1].trim());
	}
	
	public static <T> T create (final Class<? extends T> clazz) {
		try {
			final Constructor<? extends T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			return constructor.newInstance();
		} catch  (final NoSuchMethodException | SecurityException| InstantiationException | IllegalArgumentException | InvocationTargetException | IllegalAccessException ex) {
			throw new IllegalStateException(ex);
		} 
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T  copy(final T source)  {
		
		try {
			return  (T) doCopy(source);
		} catch (final ClassNotFoundException | IOException ex) {
			  throw new IllegalStateException(ex);
		}
	    
		
	}


	private static Object  doCopy(final Object source) throws IOException, ClassNotFoundException {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		 try(final ObjectOutputStream out = new ObjectOutputStream(baos))  {
				out.writeObject(source);
		    	return stream2Object(baos);
		}
	}


	private static Object stream2Object(final ByteArrayOutputStream baos) throws IOException, ClassNotFoundException  {
		try(final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()))) {
			return  in.readObject();
		} 
	}
	
	
	
	
	public static void clearFields(final Object source) {
		ReflectionUtils.doWithFields(source.getClass(), new FieldCallback() {
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if( field.isAnnotationPresent(Transient.class)){
					return;
				}
				
				field.setAccessible(true);
				final Object value = field.get(source);
				if (value instanceof Collection) {
					final Collection<?> collection = (Collection<?>) value;
					collection.clear();
					return;
				}
				
				
				setField(source, field, null);
				
			}

			
		});
	}
	
	private static void  setField(final Object object, final Field field, Object value) {
		try {
				field.set(object, value);
		} catch (final Exception ex ){
	
		}
	}
	
	public static void setId(final Object object, final Object id) {

		ReflectionUtils.doWithFields(object.getClass(), new FieldCallback() {
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if (!field.isAnnotationPresent(Id.class)) {
					return;
				}
				field.setAccessible(true);
				field.set(object, id);
			}
		});

	}
	
	
	public static void setDependency(final Object parent, final Class<?> type, final Object child ) {
           ReflectionUtils.doWithFields(parent.getClass(), new FieldCallback() {
			
			@Override
			public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {
				if( field.getType().equals(type)){
					field.setAccessible(true);
					field.set(parent , child);
				}
				
			}
		});
	}
	

}
