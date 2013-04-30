package de.mq.merchandise.opportunity.support;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.util.EntityUtil;

@Entity(name="CommercialSubject")
@Table(name="commercial_subject")
class CommercialSubjectImpl implements  CommercialSubject {
	

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=50)
	private String name; 
	
	@Column(length=250)
	private String description;
	
	@ManyToOne(targetEntity=CustomerImpl.class,optional=false)
	private Customer customer;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="documents")
	private Map<String,byte[]> storedDocuments=new HashMap<>();
	
	
	protected CommercialSubjectImpl() {
		
	}
	
	public CommercialSubjectImpl(final Customer customer, final String name, final String description) {
		this.name = name;
		this.description = description;
	}


	@Override
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}

	@Override
	public boolean hasId() {
		return (id != null);
	} 
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.CommercialSubject#assignDocument(java.lang.String, byte[])
	 */
	@Override
	public  void assignDocument(final String name, final byte[] document ) {
		storedDocuments.put(name, document);
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.CommercialSubject#remove(java.lang.String)
	 */
	@Override
	public  void removeDocument(final String name) {
		storedDocuments.remove(name);
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.CommercialSubject#name()
	 */
	@Override
	public String name() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.CommercialSubject#description()
	 */
	@Override
	public String description() {
		return description;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.CommercialSubject#customer()
	 */
	@Override
	public Customer customer() {
		return customer;
		
	}
	

}
