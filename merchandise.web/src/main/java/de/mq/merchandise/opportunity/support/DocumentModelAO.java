package de.mq.merchandise.opportunity.support;

import java.io.Serializable;

import javax.faces.model.DataModel;

import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.Setter;

public abstract class DocumentModelAO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Setter(value = "document")
	public abstract  void setDocument(final DocumentsAware document);
	
	@Getter(value = "document")
	public abstract DocumentsAware getDocument();
	
	
	
	@Getter(  value = "document", converter=Document2SimpleMapDataModel.class )
	public abstract DataModel<String> getDocuments(); 
	

	@Getter(value = "selected")
	public abstract String getSelected();

	@Setter(value = "selected")
	public abstract void setSelected(final String selected);
	
	@Setter(value = "width")
	public abstract void  setWidth(Integer width);
	@Getter(value = "width")
	public abstract Integer  getWidth();
	
	@Setter(value = "height")
	public abstract void  setHeight(Integer height);
	@Getter(value = "height")
	public abstract Integer  getHeight();

}
