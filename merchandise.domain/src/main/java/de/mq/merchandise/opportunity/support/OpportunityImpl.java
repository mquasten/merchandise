package de.mq.merchandise.opportunity.support;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;

import de.mq.merchandise.customer.Customer;
import de.mq.merchandise.customer.support.CustomerImpl;
import de.mq.merchandise.opportunity.support.CommercialSubject.DocumentType;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Opportunity")
public class OpportunityImpl implements Opportunity {
	
	
	private static final long serialVersionUID = 1L;

	@GeneratedValue
	@Id
	private Long id;
	
	@Enumerated
	private Kind kind;
	
	@ManyToMany( targetEntity=ClassificationImpl.class,  fetch=FetchType.LAZY,  cascade={ CascadeType.REFRESH} )
	@JoinTable(name="opportunity_classification" , joinColumns={@JoinColumn(name="opportunity_id")}, inverseJoinColumns={@JoinColumn(name="classification_id")})
	private Set<Classification> classifications=new HashSet<>();
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="opportunity_keyword", joinColumns=@JoinColumn(name="opportunity_id" ) )
	@Column(name="keyword", length=50 )
	private Set<String> keyWords = new HashSet<>(); 
	
	@Column(length=50)
	@Equals
	private String name;

	@Column(length=250)
	@Equals
	private String description;
	
	@ManyToOne(targetEntity=CustomerImpl.class, cascade={CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH} )
	@Equals
	@JoinColumn(name="customer_id" )
	private Customer customer;
	
	@OneToMany(mappedBy="commercialSubject", targetEntity=CommercialRelationImpl.class,  fetch=FetchType.LAZY,  cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private Set<CommercialRelation> commercialRelations = new HashSet<>(); 
	
	
	@ElementCollection(fetch=FetchType.LAZY)
	@CollectionTable(name="opportunity_documents", joinColumns=@JoinColumn(name="opportunity_id" ) )
	@MapKeyColumn(name="document_name", length=50)
	@Column(name="stored_document"  )
	@Lob()
	private Map<String,byte[]> storedDocuments=new HashMap<>();
	
	protected OpportunityImpl() {
		
	}
	
	public OpportunityImpl(final Customer customer, final String name, final String description) {
		this.customer = customer;
		this.name = name;
		this.description = description;
	}
	
	public OpportunityImpl(final Customer customer, final String name) {
		this.customer = customer;
		this.name = name;
	}
	
	public long id() {
		EntityUtil.idAware(id);
		return id;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.Opporunity#name()
	 */
	@Override
	public String name() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.Opporunity#description()
	 */
	@Override
	public String description() {
		return name;
	}
	
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.opportunity.support.Opporunity#customer()
	 */
	@Override
	public final Customer customer() {
		return customer;
	}

	@Override
	public boolean hasId() {
		return (id==null);
	}
	
	
	@Override
	public int hashCode() {
		return  EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@Override
	public boolean equals(final Object obj) {
	    return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(Opportunity.class).isEquals();
	}

	@Override
	public Map<String, byte[]> documents() {
		return Collections.unmodifiableMap(storedDocuments);
	}

	@Override
	public void assignDocument(final String name, final DocumentType documentType, byte[] document) {
		storedDocuments.put(name, document);
		
	}

	@Override
	public void removeDocument(final String name, final DocumentType documentType) {
		storedDocuments.remove(name);		
	}
	
	@Override
	public Collection<Classification> classifications() {
		return Collections.unmodifiableSet(classifications);
	}
	
	@Override
	public void assignClassification(final Classification classification) {
		classifications.add(classification);
	}
	
	@Override
	public void removeClassification(final Classification classification) {
		classifications.add(classification);
	}
	
	@Override
	public Collection<String> keyWords() {
		return Collections.unmodifiableSet(keyWords);
	}
	
	@Override
	public void assignKeyWord(final String keyWord){
		this.keyWords.add(keyWord);
	}
	
	@Override
	public void removeKeyWord(final String keyWord){
		this.keyWords.remove(keyWord);
	}

}
