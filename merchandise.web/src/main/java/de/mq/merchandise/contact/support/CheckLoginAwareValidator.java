package de.mq.merchandise.contact.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.MethodCallback;

import de.mq.mapping.util.proxy.GetterDomain;
import de.mq.mapping.util.proxy.ProxyUtil;
import de.mq.merchandise.contact.LoginContact;

public class CheckLoginAwareValidator implements ConstraintValidator<CheckLoginAware, Collection<?>>  {

	
	@Override
	public void initialize(final CheckLoginAware constraintAnnotation) {
		
		
	}

	@Override
	public boolean isValid(final Collection<?> contacts, final ConstraintValidatorContext context) {
		for(final Object contact: contacts){
			
			if( isLoginAware(contact) ) {
				return true;
			}
			
			
		
		}
		return false;
	}

	private boolean  isLoginAware(final Object contact) {
		for(final LoginContact loginContact : ProxyUtil.collectDomains(LoginContact.class, contact)){
		    if( loginContact.isLogin()){
		    	return true;
		    }
		}
		return false;
	}
	
   

	

	
	
	
}
