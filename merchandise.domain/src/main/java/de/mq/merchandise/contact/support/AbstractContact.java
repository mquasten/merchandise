package de.mq.merchandise.contact.support;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.mq.merchandise.contact.LoginContact;
import de.mq.merchandise.util.EntityUtil;
import de.mq.merchandise.util.Equals;

@Entity(name="Contact")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType=DiscriminatorType.STRING)
@Table(name="contact",uniqueConstraints={@UniqueConstraint(columnNames="login")})
public abstract  class AbstractContact implements LoginContact {

	
	private static final long serialVersionUID = 1L;
	@GeneratedValue()
	@Id
	protected Long id;
	@Column(length = 25, name = "account")
	@Equals
	protected final  String account;
	
	
	 //needed for hibernate, transient attributes are not present within callbacks, why? ask Gavin.
	protected boolean isLogin;
	
	@Column(length=50)
	private String login;
	

	protected abstract String  contactInfo();
	
	AbstractContact() {
		this.isLogin=false;
		this.account=null;
	}
	
	public AbstractContact(final String accout, final boolean isLogin) {
		this.account=accout;
		this.isLogin=isLogin;
	}

	public final long id() {
		EntityUtil.idAware(id);
		return this.id;
	}
	
	@Override
	public boolean hasId() {
		return (id != null);
	}
	
	@Override
	public int hashCode() {
	    return EntityUtil.equalsBuilder().withSource(this).buildHashCode();
	}

	@PrePersist
	@PreUpdate
	void onStore() {
		
		System.out.println("???" + isLogin);
		if(!isLogin){
			return;
		}
		login=contact().replaceAll("[ , \t]", "" ).toLowerCase();
	}
	
	@Override
	public boolean isLogin() {
		return isLogin;
	}
	
	@PostLoad
	void onLoad() {
		
		if( login != null){
			isLogin=true;
			return;
		}
		isLogin=false;
	}

	@Override
	public boolean equals(final Object obj) {
		return EntityUtil.equalsBuilder().withSource(this).withTarget(obj).forInstance(getClass()).isEquals();
	}

	@Override
	public final String contact() {
		return contactInfo();
	}

}