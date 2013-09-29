package de.mq.merchandise.controller;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.opportunity.support.CommercialSubjectsModelAO;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.DocumentsAware;
import de.mq.merchandise.opportunity.support.OpportunityModelAO;

public interface DocumentController {
	
	@MethodInvocation(actions={ @ActionEvent(params={ @Parameter(clazz = DocumentModelAO.class , el="#arg.document", elResultType=DocumentsAware.class) , @Parameter(clazz = FileUploadEvent.class , originIndex=0, el="#arg.file", elResultType=UploadedFile.class)}, name="addAttachement")}, clazz = DocumentControllerImpl.class)
	void addAttachement(final  FileUploadEvent fileUploadEvent);
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class , el="#arg.document" , elResultType=DocumentsAware.class), @Parameter(clazz=DocumentModelAO.class,  el="#arg.selected" , elResultType=String.class ) })}, clazz = DocumentControllerImpl.class)
	String url();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class ), @Parameter(clazz=OpportunityModelAO.class, el="#arg.selected.opportunity" , elResultType=DocumentsAware.class) }, name="assign" , startConversation=true)}, clazz = DocumentControllerImpl.class)
	String assignFromOpportunity();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class ), @Parameter(clazz=CommercialSubjectsModelAO.class, el="#arg.selected.commercialSubject" , elResultType=DocumentsAware.class) }, name="assign", startConversation=true)}, clazz = DocumentControllerImpl.class)
	String assignFromSubject();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class ), @Parameter(clazz=DocumentModelAO.class, el="#arg.selected", elResultType=String.class) })}, clazz = DocumentControllerImpl.class)
	void removeAttachement();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class )})}, clazz = DocumentControllerImpl.class)
	void addLink();
	
	@MethodInvocation(actions={@ActionEvent(params={ @Parameter(clazz = DocumentModelAO.class )})}, clazz = DocumentControllerImpl.class)
	String showAttachement();
	
	@MethodInvocation(actions={@ActionEvent(params={ @Parameter(clazz = DocumentModelAO.class , el="#arg.returnFromUpload" , elResultType=String.class)}, endConversation=false)}, clazz = DocumentControllerImpl.class)
	String cancelUpLoad();

}


