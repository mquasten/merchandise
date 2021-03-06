package de.mq.merchandise.subject.support;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

@Entity(name = "Condition")
@Table(name = "condition")
@Cacheable(false)
public class ConditionImpl implements Condition {

	@GeneratedValue
	@Id
	protected Long id;

	@ManyToOne(targetEntity = SubjectImpl.class, optional = false, fetch = FetchType.LAZY )
	@JoinColumn(name = "subject_id", referencedColumnName = "id", updatable = false, nullable = false)
	@NotNull(message="jsr303_mandatory")
	@Valid
	private Subject subject;

	@Column(length = 20, nullable = false, name = "condition_type")
	@NotNull(message="jsr303_mandatory")
   @Size(min=1, max=20 , message="jsr303_condition_conditionType_size")
	private String conditionType;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	@NotNull(message="jsr303_mandatory")
	ConditionDataType dataType;


	
	protected ConditionImpl() {

	};

	ConditionImpl(final Subject subject, final String conditionType, final ConditionDataType datatype) {
		Assert.hasText(conditionType);
		Assert.notNull(datatype);
		Assert.notNull(subject);
		this.subject = subject;
		this.conditionType = conditionType;
		this.dataType = datatype;
	}

	
	@Override
	public String conditionType() {
		return this.conditionType;
	}

	@Override
	public  ConditionDataType conditionDataType() {
		return this.dataType;
	}
	
	@Override
	public Subject subject() {
		return this.subject;
	}


	@Override
	public  int hashCode() {
		if (!valid(this)) {
			return super.hashCode();
		}
		return conditionType.hashCode() + subject.hashCode();
	} 

	private boolean valid(Condition condition) {
		if (!StringUtils.hasText(condition.conditionType())) {
			return false;
		}
		if (condition.subject() == null) {
			return false;
		}
		return true;
	}


	
	@Override
	public  boolean equals(final Object obj) {
		if (!valid(this)) {
			return super.equals(obj);
		}
		if (!(obj instanceof Condition)) {
			return super.equals(obj);

		}

		final Condition other = (Condition) obj;
		if (!valid(other)) {
			return super.equals(obj);
		}

		return conditionType().equals(other.conditionType()) && subject().equals(other.subject());

	} 
}
