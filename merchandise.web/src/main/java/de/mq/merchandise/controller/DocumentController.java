package de.mq.merchandise.controller;

import org.primefaces.event.FileUploadEvent;

import de.mq.mapping.util.proxy.ActionEvent;
import de.mq.mapping.util.proxy.MethodInvocation;
import de.mq.mapping.util.proxy.Parameter;
import de.mq.merchandise.opportunity.support.DocumentsAware;
import de.mq.merchandise.opportunity.support.OpportunityAO;

public interface DocumentController {
	
	@MethodInvocation(actions={@ActionEvent(params={@Parameter(clazz = FileUploadEvent.class , originIndex=0) }, name="handleFileUpload"), @ActionEvent(params={@Parameter(clazz = OpportunityAO.class , el="#arg.opportunity", elResultType=DocumentsAware.class) , @Parameter(clazz = FileUploadEvent.class , originIndex=0, el="#arg.file.fileName", elResultType=String.class)}, name="addAttachement")}, clazz = DocumentControllerImpl.class)
	void addOpportunityAttachement(final  FileUploadEvent fileUploadEvent);
	


}
