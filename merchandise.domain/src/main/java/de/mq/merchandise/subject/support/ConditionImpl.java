package de.mq.merchandise.subject.support;

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

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;

@Entity(name = "Condition")
@Table(name = "condition")
class ConditionImpl implements Condition {

	@GeneratedValue
	@Id
	protected Long id;

	@ManyToOne(targetEntity = SubjectImpl.class, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "subject_id", referencedColumnName = "id", updatable = false, nullable = false)
	private Subject subject;

	@Column(length = 20, nullable = false, name = "condition_type")
	private String conditionType;

	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	ConditionDataType dataType;


	@SuppressWarnings("unused")
	private ConditionImpl() {

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
	public final ConditionDataType conditionDataType() {
		return this.dataType;
	}
	
	@Override
	public Subject subject() {
		return this.subject;
	}


	@Override
	public final int hashCode() {
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
	public final boolean equals(final Object obj) {
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
