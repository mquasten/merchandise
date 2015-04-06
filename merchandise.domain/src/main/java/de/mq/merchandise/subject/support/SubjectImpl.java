package de.mq.merchandise.subject.support;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import de.mq.merchandise.support.BasicEntity;
;

@Entity(name="Subject")
@Table(name="subject")
public class SubjectImpl implements BasicEntity{
	
	@GeneratedValue
	@Id
	protected  Long id;
	
	
	@Column(nullable=false, length=30)
	private String name;
	
	@Column(length=50)
	private String description; 
	
	
	
	public SubjectImpl(final String name) {
		this(name,null);
	}
	
	public SubjectImpl(final String name, final String description) {
		this.name=name;
		this.description=description;
	}
	
	
	
	
	public final String name() {
		return name;
	}
	
	public final String description() {
		return description;
	}

	

}
