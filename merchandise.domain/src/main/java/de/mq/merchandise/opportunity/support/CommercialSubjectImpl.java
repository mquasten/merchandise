package de.mq.merchandise.opportunity.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="CommercialSubject")
@Table(name="commercial_subject")
public class CommercialSubjectImpl implements  CommercialSubject {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length=50)
	@Equals
	private String name; 
	
	@Column(length=250)
	private String description;
	
	@ManyToOne(targetEntity=CustomerImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
	@Equals
	@JoinColumn(name="customer_id" )
	private Customer customer;
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="commercial_subject_documents",joinColumns=@JoinColumn(name="commercial_subject_id"))
   @MapKeyColumn(name="document_name", length=50)
	@Column(name="stored_document" ,columnDefinition="BLOB")
	private Map<String,byte[]> storedDocuments=new HashMap<>();
	
	protected CommercialSubjectImpl() {
		
	}
	
	public CommercialSubjectImpl(final Customer customer, final String name, final String description) {
		this.customer=customer;
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
	public  void assignDocument(final String name, final DocumentType documentType, final byte[] document ) {
		storedDocuments.put(documentType.key(name), document);
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.CommercialSubject#remove(java.lang.String)
	 */
	@Override
	public  void removeDocument(final String name, final DocumentType documentType) {
		storedDocuments.remove(documentType.key(name));
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
	
	@Override
	public final Map<String, byte[]> documents() {
		return Collections.unmodifiableMap(storedDocuments);
	}

	@Override
	public int hashCode() {
		return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		 return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(CommercialSubject.class).isEquals();
	}
	

	
	
}
