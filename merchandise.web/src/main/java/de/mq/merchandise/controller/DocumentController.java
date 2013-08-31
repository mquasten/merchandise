package de.mq.merchandise.controller;

import org.primefaces.event.FileUploadEvent;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.opportunity.support.DocumentModelAO;
import de.mq.merchandise.opportunity.support.DocumentsAware;

public interface DocumentController {
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = FileUploadEvent.class , originIndex=0) }, name="handleFileUpload"), @ActionEvent(params={ @Parameter(clazz = DocumentModelAO.class , el="#arg.document", elResultType=DocumentsAware.class) , @Parameter(clazz = FileUploadEvent.class , originIndex=0, el="#arg.file.fileName", elResultType=String.class)}, name="addAttachement")}, clazz = DocumentControllerImpl.class)
	void addOpportunityAttachement(final  FileUploadEvent fileUploadEvent);
	

	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class , el="#arg.document" , elResultType=DocumentsAware.class), @Parameter(clazz=String.class, originIndex=0) }, name="url")}, clazz = DocumentControllerImpl.class)
	String url(final String name);
	
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class ), @Parameter(clazz=DocumentsAware.class, originIndex=0) })}, clazz = DocumentControllerImpl.class)
	void assign(final DocumentsAware documentAware );
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class ), @Parameter(clazz=DocumentModelAO.class, el="#arg.selected", elResultType=String.class) })}, clazz = DocumentControllerImpl.class)
	void removeAttachement();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = DocumentModelAO.class )})}, clazz = DocumentControllerImpl.class)
	void addLink();
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = String.class , originIndex=0 ), @Parameter(clazz = DocumentModelAO.class )})}, clazz = DocumentControllerImpl.class)
	void size(final String name);

}


