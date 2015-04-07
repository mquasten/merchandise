package de.mq.merchandise.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.merchandise.domain.subject.support.Subject;
import de.mq.merchandise.domain.subject.support.SubjectImpl;

@Entity(name="Customer")
@Table(name="customer")
public class CustomerImpl implements Customer{
	@GeneratedValue
	@Id
	private Long id;
	
	@Column(length=50)
	private  String name;
	
	@OneToMany(cascade = {CascadeType.DETACH, CascadeType.REFRESH, CascadeType.REMOVE} , fetch = FetchType.LAZY, mappedBy = "customer", targetEntity=SubjectImpl.class)
	@OrderBy("date_created")
	private List<Subject> subjects = new ArrayList<>();
	
	
	@SuppressWarnings("unused")
	private CustomerImpl() {
		
	}
	
	public CustomerImpl(final String name){
		Assert.hasText(name, "Name is mandatory");
		this.name=name;
	}
	
	/* (non-Javadoc)
	 * @see de.mq.merchandise.support.Customer#name()
	 */
	@Override
	public final String name() {
		Assert.hasText(name, "Name is mandatory");
		return name;
	}
	
	public final List<Subject> subjects() {
		return Collections.unmodifiableList(subjects);
	}

	@Override
	public int hashCode() {
		if(! StringUtils.hasText(name)){
			return super.hashCode();
		}
		
		return name.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if(!StringUtils.hasText(name)){
			return super.equals(obj);
		}
		if (!(obj instanceof Customer)) {
			return super.equals(obj);
		}
		final Customer other = (Customer) obj;
		return name.equals(other.name());
	}
	
	
	

}
