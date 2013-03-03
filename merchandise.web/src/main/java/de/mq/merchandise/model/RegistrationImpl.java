package de.mq.merchandise.model;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerAO;
import de.mq.merchandise.customer.support.LegalPersonAO;
import de.mq.merchandise.customer.support.NaturalPersonAO;

@Component("registration")
@Scope( value = "view")
public class RegistrationImpl implements Serializable, Registration {
	private static final long serialVersionUID = 3894850127039962986L;

	private Map<Kind,Object> persons = new HashMap<>();
	
	private Kind kind=Kind.NaturalPerson;
	
	private final User user;
	
	private final CustomerAO customer;
	

	@Autowired
	public RegistrationImpl(final LegalPersonAO legalPersonAO, final NaturalPersonAO naturalPersonAO, final User user, final CustomerAO customer) {
		persons.put(Kind.NaturalPerson, naturalPersonAO);
		persons.put(Kind.LegalPerson, legalPersonAO);
		persons.put(Kind.User, naturalPersonAO);
		this.user=user;
		this.customer=customer;
	}
	
	
	

	Object person(final Kind kind) {
		return persons.get(kind);
	}
	
	
	
	@NotNull(message="{mandatory_field}")
	public final   String getKind() {
		return kind.name();
	}
	
	
	public  final void setKind(final String kind) {
		if( kind == null) {
			return;
		} 
		this.kind=Kind.valueOf(kind);
	}
	
	@NotNull(message="{mandatory_field}")
	public  final String  getLanguage() {
		final Object person = person(this.kind);
		return (String) ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(person.getClass(), "getLanguage"), person);
	}
	
	
	
	
	public final Object getPerson() {
		return  person(this.kind);
	}


	
	public final void setLanguage(final String language) {
		final Object person = person(this.kind);
		final Method method = ReflectionUtils.findMethod(person.getClass(), "setLanguage" , String.class);
		ReflectionUtils.invokeMethod(method, person, language);
		user.setLanguage(language);
	}

	
	public final CustomerAO getCustomer() {
		return customer;
	}

	@Override
	public final Customer customer() {
		return this.customer.getCustomer();
	}
	
	@Override
	public final void assign(final Customer customer){
		this.customer.setCustomer(customer);
	}
	

	@Override
	public final Kind kind() {
		return kind;
	}
}
