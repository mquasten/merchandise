package de.mq.merchandise.controller;

import java.lang.reflect.Constructor;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

public class SerialisationControllerImpl {
	
	static final String DELIMITER = ""+(char) 30; /*Record Separator*/
	
	

	public  String serialize(final Object target){
		return serialize(target, properties(target));
	}
	
	
	public void deserialize(final Object target, final String values) {
		deserialize(target, values, properties(target));
	}
	
	String serialize(final Object target,final String[] properties )  {
		final Object[] values =  new Object[properties.length];
		int i=0;
		for(final String property : properties){
			
			values[i]=property(target, property);
		    i++;
		}
		return StringUtils.arrayToDelimitedString(values,DELIMITER);
	}


	


	private String[] properties(final Object target) {
		Class<?> clazz =target.getClass();
		while (clazz!= null){
			if(clazz.isAnnotationPresent(State.class)){
				return clazz.getAnnotation(State.class).value();
			}
			clazz=clazz.getSuperclass();
		}
		return new String[] { };
	}
	
	
	void deserialize(final Object target, final String values, final String[] properties ) {
		int i=0;
		for(final String value : StringUtils.delimitedListToStringArray(values, DELIMITER)) {
			assignProperty(target, properties[i], value);
			i++;
		}
	}


	
	
	private void assignProperty(final Object target, final String property, final String value)  {
		try {
		
		    Class<?> propertyType = PropertyUtils.getPropertyType(target, property);
		    if( propertyType==null){
		    	propertyType=String.class;
		    }
            
		   if (value.length() == 0 ){
			   PropertyUtils.setProperty(target,property, null);
			   return;
		   }

			PropertyUtils.setProperty(target, property,((Constructor<?>) ClassUtils.resolvePrimitiveIfNecessary(propertyType).getConstructor(String.class)).newInstance(value));	
		} catch (final Exception ex) {
			ReflectionUtils.handleReflectionException(ex);
		}
	}



	
	
	

	private Object property(final Object target, final String property)  {
		try {
		
			 final Object result =  PropertyUtils.getProperty(target, property);
			 if(result==null){
				 return "";
			 }
			 return result;
			
			
		} catch (Exception ex) {
			
			ReflectionUtils.handleReflectionException(ex);
			return null;
		}
	}
	
	

}



