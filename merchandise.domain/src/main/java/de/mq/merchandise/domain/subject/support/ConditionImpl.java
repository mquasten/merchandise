package de.mq.merchandise.domain.subject.support;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

@Entity(name="Condition")
@Table(name ="condition")
class ConditionImpl<T> implements Condition<T> {
	

	@GeneratedValue
	@Id
	protected  Long id;
	
	@ManyToOne(targetEntity=SubjectImpl.class ,optional=false,fetch=FetchType.LAZY)
	@JoinColumn(name="subject_id" ,  referencedColumnName="id",updatable=false, nullable=false)
	private Subject subject;
	
	@Column(length=20, nullable=false, name="condition_type")
	private String conditionType;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20, nullable=false)
	ConditionDataType dataType; 
	
	@ElementCollection(fetch=FetchType.LAZY, targetClass=InputValueImpl.class)
	@CollectionTable(joinColumns={@JoinColumn(name="condition_id")},name="condition_value" )
	@Column(name="value", length=50)
	private final List<InputValueImpl> values= new ArrayList<>(); 
	
	@SuppressWarnings("unused")
	private ConditionImpl() {
		
	};
	
	ConditionImpl(final Subject subject, final String conditionType, final ConditionDataType datatype){
		this.subject=subject;
		this.conditionType=conditionType;
		this.dataType=datatype;
	}
	
	@Override
	public  void add(final T value) {
		try {
			Assert.isTrue(dataType.targetClass.isInstance(value), String.format("Value isn't an instance from %s",dataType.targetClass.getName()));
			
			
			values.add(BeanUtils.instantiateClass(InputValueImpl.class.getDeclaredConstructor(dataType.targetClass), value));
		} catch (final Exception ex) {
			 throw new IllegalStateException(ex);
		} 
	}
	
	@Override
	public final void remove(final T value) {
		values.remove(value);
	}
	
	@Override
	public final void clear() {
		values.clear();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final List<T> values() {
		return (List<T>) Collections.unmodifiableList(values.stream().filter(v -> v.value().isPresent()).map(v -> v.value().get()).collect(Collectors.toList()));
	}
	
}
