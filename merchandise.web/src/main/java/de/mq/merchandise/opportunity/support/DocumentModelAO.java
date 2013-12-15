package de.mq.merchandise.opportunity.support;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.model.DataModel;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.Getter;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.mapping.util.proxy.Setter;
import de.mq.merchandise.controller.DocumentControllerImpl;
import de.mq.merchandise.model.support.FacesContextFactory;
import de.mq.merchandise.model.support.String2LongConverter;

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
	
	
	@Getter(value = "link")
	public abstract String getLink();

	@Setter(value = "link")
	public abstract void setLink(final String link);
	
	
	@Setter(value = "width")
	public abstract void  setWidth(Integer width);
	@Getter(value = "width")
	public abstract Integer  getWidth();
	
	@Setter(value = "height")
	public abstract void  setHeight(Integer height);
	@Getter(value = "height")
	public abstract Integer  getHeight();
	
	@Getter(value = "returnFromUpload")
	public abstract String getReturnFromUpload();
	
	
	@Setter(value = "returnFromUpload")
	public abstract void setReturnFromUpload(String url);
	
	@Getter(value = "returnFromShowAttachement")
	public abstract String getReturnFromShowAttachement();
	
	
	@Setter(value = "returnFromShowAttachement")
	public abstract void setReturnFromShowAttachement(String url);
	
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class, proxy=true ), @Parameter(clazz = FacesContextFactory.class , el="#arg.facesContext().externalContext.requestParameterMap['documentId']" , elResultType=Long.class , converter=String2LongConverter.class),  @Parameter(clazz = FacesContextFactory.class , el="#arg.facesContext().externalContext.requestParameterMap['page']" , elResultType=String.class ), @Parameter(clazz = FacesContextFactory.class , el="#arg.facesContext().externalContext.requestParameterMap['selected']" , elResultType=String.class ),  @Parameter(clazz = FacesContextFactory.class , el="#arg.facesContext().externalContext.requestParameterMap['returnPage']" , elResultType=String.class )})}, clazz = DocumentControllerImpl.class)
	@PostConstruct
	public abstract void init();
		
	

}
