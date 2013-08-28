package de.mq.merchandise.opportunity.support;

import java.io.Serializable;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;

public abstract class DocumentModelAO implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Getter(value = "selectedKeyWord")
	public abstract String getSelected();

	@Setter(value = "selectedKeyWord")
	public abstract void setSelected(final String selected);

}
