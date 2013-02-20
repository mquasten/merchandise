package de.mq.merchandise.customer.support;

import java.util.Date;
import java.util.Locale;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Target;

import de.mq.merchandise.customer.LegalForm;
import de.mq.merchandise.customer.LegalPerson;
import de.mq.merchandise.customer.TradeRegister;
import de.mq.merchandise.util.Equals;

@Entity(name="LegalPerson")
class LegalPersonImpl extends AbstractPerson implements LegalPerson{
	
	private static final long serialVersionUID = 1L;

	@Column(name="tax_id", length=20)
	private final  String taxId;
	
	@Embedded()
	@Basic(optional=false)
	@Equals
	@Target(TradeRegisterImpl.class)
	private final TradeRegister tradeRegister; 
	
	@Column(name="fondation_date")
	@Temporal(TemporalType.DATE)
	private final Date foundationDate;
	
	@Enumerated(EnumType.STRING)
	@Column(length=50, name="legal_form")
	private LegalForm legalForm; 
	
	public LegalPersonImpl(final String name, final String taxId, final TradeRegister tradeRegister, final LegalForm legalForm, final Date foundationDate, final Locale locale) {
		super(name, locale);
		this.taxId=taxId;
		this.tradeRegister=tradeRegister;
		this.foundationDate=foundationDate;
		this.legalForm=legalForm;
	}
	
	public LegalPersonImpl(final String name, final String taxId, final TradeRegister tradeRegister, final LegalForm legalForm, final Date foundationDate) {
		this(name, taxId, tradeRegister, legalForm, foundationDate, DEFAULT_LOCALE);
	}
	
	@SuppressWarnings("unused")
	private LegalPersonImpl(){
		this(null, null, null, null,null, DEFAULT_LOCALE);
	}
	
	
	public final String taxId() {
		return this.taxId;
	}
	
	
	public final TradeRegister tradeRegister() {
		return this.tradeRegister;
	}
	
	public final Date foundationDate() {
		return this.foundationDate;
	}
	
	public final LegalForm legalForm() {
		return this.legalForm;
	}

	

	
	
	

}
