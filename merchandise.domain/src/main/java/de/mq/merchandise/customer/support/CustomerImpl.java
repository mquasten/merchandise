package de.mq.merchandise.customer.support;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Target;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.State;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;


@Entity(name = "Customer")
@Table(name="customer")
@Cacheable(false)
@NamedQueries({ @NamedQuery(name=CustomerRepository.PERSON_FOR_LOGIN, query = "select p from Person  p  where exists (select c from p.contacts c where c.login= :login  ) and p.state.active=true "), 
                @NamedQuery(name=CustomerRepository.CUSTOMER_FOR_PERSON , query="Select r.customer from UserRelation r where r.person.id = :personId and r.state.active=true and r.customer.state.active=true")

})
class CustomerImpl implements Customer {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(targetEntity = AbstractPerson.class, fetch = FetchType.LAZY,cascade={CascadeType.PERSIST, CascadeType.MERGE})
	@Equals
	private Person person;

	@Embedded()
	@Basic(optional=false)
	@Target(StateImpl.class)
	private State state=EntityUtil.create(StateImpl.class);; 
	
	
	@OneToMany(orphanRemoval=true,  targetEntity=UserRelationImpl.class, fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE} , mappedBy="customer")
	private final  Set<UserRelation> userRelations = new HashSet<>();
	
	

	/**
	 * Especially for persistence. A default constructor is needed;
	 */
	 @SuppressWarnings("unused")
	private CustomerImpl() {
		super();
	}

	public CustomerImpl(final Person person) {
		this.person = person;
	}

	public long id() {
		EntityUtil.idAware(id);
		return this.id;
	}

	public Person person() {
		return person;

	}

	

	public final State state() {
		stateEXistsGuard();
		return state;
	}

	private void stateEXistsGuard() {
		if (state ==null) {
			throw new IllegalArgumentException("State should not be empty");
		}
	}
	
	
	public final void grant(final Person person, final CustomerRole...  roles){
		UserRelation existing = relation(person);
		if (existing == null ){
			existing=new UserRelationImpl(this, person);
				
			userRelations.add(existing);
		}
		
		existing.grant(roles);
	}
	
	
	private UserRelation relation(final Person person) {
		for(UserRelation userRelation : userRelations){
			if( userRelation.isOwner(person) ) {
				return userRelation;
			}
		}
		return null;
	}
	
	
	public final boolean hasUser(final Person person) {
		if ( relation(person) != null) {
			return true;
		}
		return false;
	}
	
	public final void revoke(final Person person, final CustomerRole ... roles){
		UserRelation existing = relation(person);
		if (existing==null){
			return;
		}
		
		if (roles.length==0){
			userRelations.remove(existing);
			return;
		}
		
		existing.revoke(roles);
	}
	
	public final  State state(final Person person) {
		final UserRelation existing = relation(person);
		return existing.state();
	}
	
	
	public final List<Person> activePersons() {
		return filterPersons(true);
	}
	
	public final List<Person> inActivePersons() {
		return filterPersons(false);
	}

	private List<Person> filterPersons(final boolean active) {
		final List<Person> results = new ArrayList<Person>();
		for(final UserRelation userRelation : userRelations) {
			if( userRelation.state().isActive() == active){
				results.add(userRelation.person());
			}
			
		}
		return Collections.unmodifiableList(results);
		
	}
	
	public final Map<Person,State> persons() {
		final Map<Person, State> results = new HashMap<Person, State>();
		for(final UserRelation  userRelation : userRelations){
			results.put(userRelation.person(), userRelation.state());
		}
		return Collections.unmodifiableMap(results);
	}
	
	public final List<CustomerRole> roles(final Person person) {
		final UserRelation existing = relation(person);
		userRelationExistsGuard(existing);
		return Collections.unmodifiableList(new ArrayList<CustomerRole>(existing.roles()));
	}

	private void userRelationExistsGuard(final UserRelation existing) {
		if( existing == null){
			throw new IllegalArgumentException("Person is not assigned to the customer");
		}
	}

	
	
	
	public final boolean hasRole(final Person person, final CustomerRole role) {
		final UserRelation existing = relation(person);
		userRelationExistsGuard(existing);
		return existing.hasRole(role);
		
	}
	
	@Override
	public boolean hasId() {
		return (id != null);
	}

	

	@Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(Customer.class).isEquals();
	}

	@Override
	public String toString() {
		if(person == null){
			return super.toString();
		}
		return"person=" + person.toString();
	}
	
	

}
