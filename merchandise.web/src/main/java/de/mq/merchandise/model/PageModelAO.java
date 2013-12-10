package de.mq.merchandise.model;

import java.io.Serializable;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;

public abstract class PageModelAO  implements Serializable {
	
	private static final long serialVersionUID = -3792623423766905764L;

	@Getter(value ="selectMode")
	public abstract boolean getSelectMode();
	
	@Setter(value="selectMode")
	public abstract void setSelectMode(final boolean selectMode);
	

}
