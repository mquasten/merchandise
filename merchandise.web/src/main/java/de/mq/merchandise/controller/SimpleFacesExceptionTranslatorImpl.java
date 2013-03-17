package de.mq.merchandise.controller;


import javax.faces.application.FacesMessage;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import de.mq.mapping.util.proxy.AOProxyFactory;
import de.mq.mapping.util.proxy.Action;
import de.mq.mapping.util.proxy.ExceptionTranslation;
import de.mq.mapping.util.proxy.ModelRepository;
import de.mq.merchandise.model.support.FacesContextFactory;

@Component
public class SimpleFacesExceptionTranslatorImpl implements Action {
	
	private static final String ARGS = "args";
	static final String VALIDATION_FAILED = "validationFailed";
	final private MessageSouceController messageSourceController;
	final private FacesContextFactory facesContextFactory;
	
	@Autowired
	public SimpleFacesExceptionTranslatorImpl(final MessageSouceController messageSourceController, final FacesContextFactory facesContextFactory) {
        this.messageSourceController=messageSourceController;		
        this.facesContextFactory=facesContextFactory;
	}

	@Override
	public Object execute(final ExceptionTranslation exceptionTranslation, final ModelRepository modelRepository, final Throwable ex, final Object[] args ) throws Exception {
		if (ex instanceof ConstraintViolationException) {
			addConstraintViolations(ex);
			
		} else {
			addFacesMessage(null, messageSourceController.get(exceptionTranslation.bundle()));
		}
		
		
		facesContextFactory.requestContext().addCallbackParam(VALIDATION_FAILED, true);
		
		
		if(! exceptionTranslation.resultExpression().trim().isEmpty()) {
			return parseEl(exceptionTranslation, args);
		}
		
		
		if( exceptionTranslation.result().equals(java.lang.Void.class)){
			return null;
		}
		return modelRepository.beanResolver().getBeanOfType(AOProxyFactory.class).createProxy(exceptionTranslation.result(), modelRepository);
	}

	private Object parseEl(final ExceptionTranslation exceptionTranslation, final Object[] args) {
		final ExpressionParser parser = new SpelExpressionParser();
		final StandardEvaluationContext context = new StandardEvaluationContext();
		context.setVariable(ARGS, CollectionUtils.arrayToList(args));
		return parser.parseExpression(exceptionTranslation.resultExpression()).getValue(context);
	}

	private void addConstraintViolations(final Throwable ex) {
		final ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex ; 
		
		for(ConstraintViolation<?> constraintViolation : constraintViolationException.getConstraintViolations()){
			addFacesMessage(null, constraintViolation.getMessage());
		}
	}

	private void addFacesMessage(final String clientId, final String message) {
		final FacesMessage facesMessage = new FacesMessage(message);
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		
		facesContextFactory.facesContext().addMessage(clientId, facesMessage);
	}
	
	

}
