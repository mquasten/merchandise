package de.mq.merchandise.opportunity.support;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="EntityContext" )
@Table(name="entity_context")
@NamedQueries({
@NamedQuery(name = EntityContextRepository.ENTITYCONTEXT_FOR_RESOURCE, query = "select e from EntityContext e where resource=:resource"),	
@NamedQuery(name =  EntityContextRepository.ENTITYCONTEXT_AGGREGATION,  query = "select  new de.mq.merchandise.opportunity.support.EntityContextAggregationImpl(count(distinct e.resourceId) ,  min(e.created))  from EntityContext e")	
})
class EntityContextImpl  implements EntityContext{

	private static final long serialVersionUID = 101881563574996229L;
	
	@GeneratedValue
	@Id
	private final Long id;
	
	
	@Basic(optional=false)
	@Equals
	@Column(name="resource_id")
	private Long resourceId;
	

	@Enumerated(EnumType.STRING)
	@Column(length=25)
	@Basic(optional=false)
	@Equals
	private Resource resource;
	
	@Column(name="delete_row")
	@Basic(optional=false)
	@Equals
	private boolean delete;
	
	@Temporal(TemporalType.DATE)
	@Equals
	@Basic(optional=false)
	private Date created;
	
	@Enumerated
	private State state = State.New ; 

	@Temporal(TemporalType.DATE)
	@Column(name="last_error")
	private Date lastErrorDate;
	
	@Basic(optional=false)
	@Column(name="error_counter")
	private int errorCounter=0;
	
	@Transient
	private Map<Class<?>, Object> references = new HashMap<>();
	
	EntityContextImpl(final Long resourceId, final Resource resource, boolean delete) {
		this.id=null;
		this.resourceId = resourceId;
		this.resource = resource;
		this.delete = delete;
		this.created=new Date();
	}
	
	EntityContextImpl(final Long resourceId, final Resource resource) {
		this(resourceId, resource, false);
	}
	
	@SuppressWarnings("unused")
	private EntityContextImpl() {
		this(null, null, false);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.EntityContext#id()
	 */
	
	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}

	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.EntityContext#hasId()
	 */
	
	@Override
	public boolean hasId() {
		return (id!=null);
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.EntityContext#reourceId()
	 */
	@Override
	public final Long reourceId() {
		return resourceId;	
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.EntityContext#resource()
	 */
	@Override
	public final Resource resource(){
        return resource;		
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.EntityContext#isForDeleteRow()
	 */
	@Override
	public final boolean isForDeleteRow() {
       return delete;		
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.EntityContext#created()
	 */
	@Override
	public final Date created() {
		return this.created;
	}
	
	@Override
	public boolean equals(final Object obj) {
		 return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(EntityContext.class).isEquals();
	}
	
	@Override
	public final int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}
	
	@Override
	public final <T> void assign(final Class<T> clazz, final T reference){
		instanceOfGuard(clazz, reference);
		
		references.put(clazz, reference);
	}

	private <T> void instanceOfGuard(final Class<T> clazz, final T reference) {
		if (! clazz.isInstance(reference) ) {
			throw new IllegalArgumentException("Assigned Object isn't an instance of class " + clazz );
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final  <T>  T reference(final Class<T> clazz) {
		
		if(!references.containsKey(clazz)){
			throw new IllegalStateException("Object isn't assigned to key " + clazz);
		}
		return (T) references.get(clazz);
	}
	
	@Override
	public final boolean containsReference(final Class<?> clazz) {
		return ( references.get(clazz) != null); 
	}
	
	@Override
	public final void assign(final State state){
		EntityUtil.notNullGuard(state, "state");
	    stateIsAssignableGuard(state);
		this.state=state;
		if( state.error()){
			this.lastErrorDate=new Date();
			errorCounter++;
		}
	}

	private void stateIsAssignableGuard(final State state) {
		if( ! state.assignable() ) {
	    	throw new IllegalArgumentException("State " +state + " isn't assignabe");
	    }
	}

	@Override
	public boolean finished() {
		EntityUtil.notNullGuard(state, "state");
		return state.finised();
	}

	@Override
	public boolean error() {
		EntityUtil.notNullGuard(state, "state");
		return this.state.error();
	}
	
	
	

	
}


class EntityContextAggregationImpl implements EntityContextAggregation {
	
	private Number counter;
	
	private Date date;
	
	public EntityContextAggregationImpl(final Number counter, final Date date) {
		this.counter=counter;
		this.date=date;
	}
	
	public long counter(){
		if(this.counter==null){
			return 0;
		}
		return this.counter.longValue();
	}
	
	public Date minDate() {
		if( this.date==null){
			return new Date();
		}
		return this.date;
	}
	
	
}
