package de.mq.merchandise.subject.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.support.BasicEntity;

@Entity(name = "CommercialSubjectItemCondition")
@Table(name = "commercial_subject_item_condition")
@Cacheable(false)
class CommercialSubjectItemConditionImpl implements BasicEntity {
	@Id
	private String id;

	@NotNull(message = "jsr303_mandatory")
	@ManyToOne(targetEntity = CommercialSubjectItemImpl.class, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "commercial_subject_item_id", referencedColumnName = "id", updatable = false, nullable = false)
	@Valid
	private CommercialSubjectItem commercialSubjectItem;

	@NotNull(message = "jsr303_mandatory")
	@ManyToOne(targetEntity = ConditionImpl.class, optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "condition_id", referencedColumnName = "id", updatable = false, nullable = false)
	@Valid
	private Condition condition;

	
	@CollectionTable(name = "input_values", joinColumns = @JoinColumn(name = "commercial_subject_item_Condition_id" ) )
	@ElementCollection(targetClass = InputValueImpl.class , fetch = FetchType.LAZY )
	@OrderColumn(name="index_id")
	private List<InputValueImpl> inputValues = new ArrayList<>();

	@SuppressWarnings("unused")
	private CommercialSubjectItemConditionImpl() {

	}

	CommercialSubjectItemConditionImpl(final CommercialSubjectItem commercialSubjectItem, final Condition condition) {
		this.commercialSubjectItem = commercialSubjectItem;
		this.condition = condition;
		Assert.isTrue(condition.id().isPresent(), "Condition should be persistent.");
		Assert.notNull(condition.subject(), "CommercialSubjectItem should have a subject");
		Assert.isTrue(condition.subject().id().isPresent(), "Subject should be persistent.");
		this.id = new UUID(condition.subject().id().get(), condition.id().get()).toString();
	}

	@SuppressWarnings("unchecked")
	<T> Collection<T> values() {
		return Collections.unmodifiableCollection((Collection<T>) inputValues.stream().filter(iv -> iv.value().isPresent()).map(iv -> iv.value().get()).collect(Collectors.toList()));
	}

	<T> void assign(final T value) {
		inputValues.add(newInputValue(value));
	}

	private <T> InputValueImpl newInputValue(final T value) {
		Assert.notNull(value);

		try {
			return BeanUtils.instantiateClass(InputValueImpl.class.getDeclaredConstructor(value.getClass()), value);
		} catch (final Exception ex) {

			throw new IllegalStateException(ex);
		}
	}

	<T> void remove(final T value) {
		inputValues.remove(newInputValue(value));
	}

	 Condition condition() {
		return condition;
	}

	CommercialSubjectItem commercialSubjectItem() {
		return commercialSubjectItem;
	}

	@Override
	public int hashCode() {
		if (!valid(this)) {
			return super.hashCode();
		}
		return commercialSubjectItem.hashCode() + condition.hashCode();
	} 

	private boolean valid(CommercialSubjectItemConditionImpl commercialSubjectItemCondition) {
		if (commercialSubjectItemCondition.commercialSubjectItem == null) {
			return false;
		}
		if (commercialSubjectItemCondition.condition == null) {
			return false;
		}
		return true;
	}

	
	@Override
	public boolean equals(final Object obj) {
		if (!valid(this)) {
			return super.equals(obj);
		}
		if (!(obj instanceof CommercialSubjectItemConditionImpl)) {
			return super.equals(obj);
		}
		final CommercialSubjectItemConditionImpl other = (CommercialSubjectItemConditionImpl) obj;
		if (!valid(other)) {
			return super.equals(obj);
		}

		return commercialSubjectItem.equals(other.commercialSubjectItem) && condition.equals(other.condition);
	} 

}
