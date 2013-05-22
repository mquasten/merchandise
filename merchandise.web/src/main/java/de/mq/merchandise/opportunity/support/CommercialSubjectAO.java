package de.mq.merchandise.opportunity.support;

import java.io.Serializable;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.mapping.util.proxy.support.Number2StringConverter;

public abstract class CommercialSubjectAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter(clazz = CommercialSubjectImpl.class, value = "id", converter = Number2StringConverter.class)
	public abstract String getId();

	@Getter(clazz = CommercialSubjectImpl.class, value = "name")
	public abstract String getName();

	@Setter(clazz = CommercialSubjectImpl.class, value = "name")
	public abstract void setName(final String name);

	@Getter(clazz = CommercialSubjectImpl.class, value = "description")
	public abstract String getDescription();

	@Setter(clazz = CommercialSubjectImpl.class, value = "description")
	public abstract void setDescription(final String description);
	
	
	

}
