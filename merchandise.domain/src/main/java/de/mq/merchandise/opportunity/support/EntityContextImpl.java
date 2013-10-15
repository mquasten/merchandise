package de.mq.merchandise.opportunity.support;

import java.util.Date;

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

import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="EntityContext" )
@Table(name="entity_context")
@NamedQueries({
@NamedQuery(name = EntityContextRepository.ENTITYCONTEXT_FOR_RESOURCE, query = "select e from EntityContext e where resource=:resource")	
	
})
class EntityContextImpl  implements EntityContext{

	private static final long serialVersionUID = 101881563574996229L;
	
	@GeneratedValue
	@Id
	private final Long id;
	
	
	@Basic(optional=false)
	@Equals
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

}
