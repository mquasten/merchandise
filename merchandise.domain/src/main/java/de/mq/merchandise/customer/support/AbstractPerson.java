package de.mq.merchandise.customer.support;


import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.hibernate.annotations.Target;

import de.mq.merchandise.contact.CityAddress;
import de.mq.merchandise.contact.Contact;
import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.contact.support.AbstractCityAddress;
import de.mq.merchandise.contact.support.AbstractContact;
import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.PersonRole;
import de.mq.merchandise.customer.State;


import de.mq.merchandise.util.DigestUtil.Algorithm;
import de.mq.merchandise.util.DigestUtil;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name = "Person")
@Table(name="person")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="person_type" ,discriminatorType=DiscriminatorType.STRING , length=25)
@Cacheable(false)
abstract class AbstractPerson implements Person {

	
	private static final long serialVersionUID = 1L;
	
	private static final Algorithm ALGORITHM = Algorithm.MD5;
	private static final int PASSWORD_MAX_LEN = 20 ; 
	

	static final Locale DEFAULT_LOCALE = new Locale("", "");
	
	@Id
	@GeneratedValue()
	protected Long id;
	
	@Column(length=50, nullable=false)
	@Equals
	protected final String name;
	
	@Column(length=50)
	protected String password; 
	
	@Column(length=2)
	protected String language;
	
	@Column(length=2)
	protected String country;
	
	@Embedded()
	@Target(StateImpl.class)
	private final State state = EntityUtil.create(StateImpl.class);
	

	
	@OneToMany( orphanRemoval=true, targetEntity=AbstractContact.class, fetch=FetchType.LAZY , cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="person_id")
	private final Set<LoginContact> contacts = new HashSet<>();
	
	
	@OneToMany( orphanRemoval=true, targetEntity=AbstractCityAddress.class, fetch=FetchType.LAZY , cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@JoinColumn(name="person_id")
	private final Set<CityAddress> addresses = new HashSet<>();
	
	@ElementCollection(fetch=FetchType.LAZY )
	@CollectionTable(name="person_role", joinColumns={@JoinColumn(name="person_id")})
	@Enumerated(EnumType.STRING)
	@Column(name="role" , length=25)
	private final Set<PersonRole> roles = new HashSet<>();
	
	@OneToMany(orphanRemoval=true,  targetEntity=UserRelationImpl.class, fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} ,mappedBy="person")
	private final  Set<UserRelationImpl> userRelations = new HashSet<>();
	
	AbstractPerson(final String name, final Locale locale) {
		this.name=name;
		this.country=locale.getCountry();
		this.language=locale.getLanguage();
	}
	
	
	/*especially  for jpa, for what ever, without it will not work... */ 
	AbstractPerson() {
		this(null, DEFAULT_LOCALE);
	
	} 
	
	@Override
	public final long id() {
		EntityUtil.idAware(id);
		return id;
	}
	@Override
	public String name() {
		return name;
	}
	
	@Override
	public final void assign(final LoginContact contact) {
		remove(contact);
		contacts.add(contact);
	}
	
	@Override
	public final Set<Contact> contacts() {
		final Set<Contact> results = new HashSet<>();
		results.addAll(contacts);
		results.addAll(addresses);
		return Collections.unmodifiableSet(results);
	}

	@Override
	public final void remove(final LoginContact contact) {
		contacts.remove(contact);
	}
	
	@Override
	public final void assign(final CityAddress cityAddress) {
		remove(cityAddress);
		addresses.add(cityAddress);
	}
	
	@Override
	public final void remove(final CityAddress cityAddress) {
		addresses.remove(cityAddress);
	}
	
	@Override
	public final State state() {
		return state;
	}
	
	@Override
	public final void assign(final PersonRole personRole){
		remove(personRole);
		roles.add(personRole);
	}

	@Override
	public void remove(final PersonRole personRole) {
		roles.remove(personRole);
	}
	
	
	@Override
	public Set<PersonRole> roles() {
		return Collections.unmodifiableSet(roles);
	}
	
	@Override
	public Set<Customer> customers() {
		final Set<Customer> results = new HashSet<>();
		for(final UserRelation userRelation : this.userRelations){
			if(!userRelation.state().isActive()){
				continue;
			} 
			results.add(userRelation.customer());
		}
		return Collections.unmodifiableSet(results);
	}

	@Override
	public boolean hasRole(final PersonRole role) {
		return roles.contains(role);
	}
	
	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}
	
	
	public final Locale locale() {
		return new Locale(language, country);
	}
	
	@Override
	public boolean hasId() {
		return (id != null);
	}
	
	public final void assignPassword(final String password) {
		
		if(password.length() > PASSWORD_MAX_LEN ) {
			throw new IllegalArgumentException("Password should be less than " + PASSWORD_MAX_LEN +  " characters");
		}
		this.password=DigestUtil.digestAsHex(password, ALGORITHM);
	}
	
	
	@PrePersist
	@PreUpdate
	void digestPassword() {
		if( password == null){
			EntityUtil.mandatoryGuard(password, "password");
		}
		if(  password.length() <=  PASSWORD_MAX_LEN ) {
			this.password=DigestUtil.digestAsHex(password, ALGORITHM);
		}
	}


	
	
}
