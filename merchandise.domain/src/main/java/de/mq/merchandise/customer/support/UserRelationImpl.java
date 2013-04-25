package de.mq.merchandise.customer.support;



import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Target;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.CustomerRole;
import de.mq.merchandise.customer.Person;
import de.mq.merchandise.customer.State;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="UserRelation")
@Table(name="user_relation")
@Cacheable(false)
class UserRelationImpl implements UserRelation  {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private  Long id;
	
	@ManyToOne(targetEntity=AbstractPerson.class, cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH}  )
	@Equals
	@JoinColumn(name="person_id" )
	private final Person person;
	
	@ManyToOne(targetEntity=CustomerImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
	@Equals
	@JoinColumn(name="customer_id")
	private final Customer customer;
	
	@Embedded()
	@Column(nullable=false)
	@Target(StateImpl.class)
	private State state = EntityUtil.create(StateImpl.class);

	@ElementCollection()
	@CollectionTable(name = "user_relation_role", joinColumns={@JoinColumn(name="user_relation_id")})
	@Column(name="role" , length=25)
	@Enumerated(EnumType.STRING)
	private Set<CustomerRole> roles = new HashSet<CustomerRole>();
	
	@SuppressWarnings("unused")
	private UserRelationImpl() {
		id=null;
		person=null;
		customer=null;
	}

	UserRelationImpl(final Customer customer, final Person person) {
		this.person=person;
		this.customer=customer;
	}

	

	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#grant(de.mq.merchandise.domain.customer.CustomerRole)
	 */
	@Override
	public final void grant(final CustomerRole... roles) {
		for (final CustomerRole role : roles) {
			this.roles.add(role);
		}
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#revoke(de.mq.merchandise.domain.customer.CustomerRole)
	 */
	@Override
	public final void revoke(final CustomerRole... roles) {
		for (final CustomerRole role : roles) {
			this.roles.remove(role);
		}
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#roles()
	 */
	@Override
	public final Set<CustomerRole> roles() {
		return Collections.unmodifiableSet(roles);
	}

	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#state()
	 */
	@Override
	public final State state() {
		return this.state;
	}

	@Override
	public final  int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public final boolean equals(final Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#id()
	 */
	@Override
	public final long id() {
		EntityUtil.idAware(id);
		return id;
	}
	
    /* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#isOwner(de.mq.merchandise.domain.person.Person)
	 */
    @Override
	public final boolean isOwner(final Person person){
		return this.person.equals(person);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#hasRole(de.mq.merchandise.domain.customer.CustomerRole)
	 */
	@Override
	public final boolean hasRole(final CustomerRole role) {
		return this.roles.contains(role);
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#person()
	 */
	@Override
	public final Person  person() {
		return this.person;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.domain.customer.support.UserRelation#customer()
	 */
	@Override
	public final Customer customer() {
	   return this.customer;	
	}
	
	@Override
	public boolean hasId() {
		return (id != null);
	}

}
