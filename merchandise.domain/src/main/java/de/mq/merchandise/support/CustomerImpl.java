package de.mq.merchandise.support;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name="Customer")
@Table(name="customer")
public class CustomerImpl implements BasicEntity{
	@GeneratedValue
	@Id
	private Long id;
	
	@Column
	private final String name="minogue-music";

}
