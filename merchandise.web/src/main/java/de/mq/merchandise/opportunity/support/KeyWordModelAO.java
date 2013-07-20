package de.mq.merchandise.opportunity.support;

import java.io.Serializable;

import javax.validation.constraints.Size;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;


public abstract class KeyWordModelAO implements Serializable  {



	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Size(min=1,  message="{mandatory_field}")
	@Getter( value = "keyWord")
	public abstract String getKeyWord();

	@Setter( value = "keyWord")
	public abstract void setKeyWord(final String keyWord);
	
	@Getter(value = "selectedKeyWord")
	public abstract String getSelectedKeyWord();

	@Setter(value = "selectedKeyWord")
	public abstract void setSelectedKeyWord(final String selected);

}
