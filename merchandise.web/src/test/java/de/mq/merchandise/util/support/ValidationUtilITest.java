package de.mq.merchandise.util.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.BeanUtils;
import org.springframework.context.MessageSource;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.server.UserError;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.TextField;

import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.subject.support.SubjectImpl;
import de.mq.merchandise.util.TableContainerColumns;
import de.mq.merchandise.util.ValidationUtil;

public class ValidationUtilITest {

	private static final long ID = 19680528L;

	private static final String ERROR_VALUE = "Mußfeld";

	private static final String ERROR_KEY = "subject_name_mandatory";

	private static final String ID_PROPERTY = "id";

	private static final String DESCRIPTION_PROPERTY = "description";

	private static final String NAME_PROPERTY = "name";

	private Validator validator = Mockito.mock(Validator.class);

	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	private final FieldGroup fieldGroup = Mockito.mock(FieldGroup.class);

	private final Map<Object, AbstractComponent> properties = new HashMap<>();

	private final ValidationUtil validationUtil = new SimpleValidationUtilImpl(validator, messageSource);

	@Before
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setup() {
		properties.clear();
		properties.put(ID_PROPERTY, Mockito.mock(TextField.class));
		properties.put(NAME_PROPERTY, Mockito.mock(TextField.class));
		properties.put(DESCRIPTION_PROPERTY, Mockito.mock(TextField.class));

		Mockito.when(fieldGroup.getBoundPropertyIds()).thenReturn(properties.keySet());
		properties.keySet().forEach(p -> Mockito.when(fieldGroup.getField(p)).thenReturn((Field) properties.get(p)));

	}

	@Test
	public final void validate() {

		final Subject subject = BeanUtils.instantiateClass(SubjectImpl.class);

		final Set<ConstraintViolation<Subject>> constraintViolations = new HashSet<>();

		@SuppressWarnings("unchecked")
		final ConstraintViolation<Subject> violation = Mockito.mock(ConstraintViolation.class);
		constraintViolations.add(violation);
		final Path path = Mockito.mock(Path.class);
		Mockito.when(path.toString()).thenReturn(NAME_PROPERTY);

		Mockito.when(violation.getMessage()).thenReturn(ERROR_KEY);
		Mockito.when(violation.getPropertyPath()).thenReturn(path);
		Mockito.when(validator.validate(subject)).thenReturn(constraintViolations);

		Mockito.when(messageSource.getMessage(ERROR_KEY, null, Locale.GERMAN)).thenReturn(ERROR_VALUE);

		Assert.assertFalse(validationUtil.validate(subject, fieldGroup, Locale.GERMAN));

		properties.values().forEach(field -> Mockito.verify(field).setComponentError(null));

		ArgumentCaptor<UserError> errorCaptor = ArgumentCaptor.forClass(UserError.class);
		Mockito.verify(properties.get(NAME_PROPERTY), Mockito.times(2)).setComponentError(errorCaptor.capture());

		Assert.assertEquals(2, errorCaptor.getAllValues().size());
		Assert.assertNull(errorCaptor.getAllValues().get(0));

		Assert.assertEquals(ERROR_VALUE, errorCaptor.getAllValues().get(1).getMessage());

	}

	@Test
	public final void reset() {

		validationUtil.reset(fieldGroup);
		properties.values().forEach(field -> Mockito.verify(field).setComponentError(null));
	}
	
	@Test
	public final void cleanup() {
		final TableContainerColumns col = Mockito.mock(TableContainerColumns.class);
		Mockito.when(col.nvl()).thenReturn(ID);
		final Item item = Mockito.mock(Item.class);
		Mockito.when(fieldGroup.getItemDataSource()).thenReturn(item);
		@SuppressWarnings("unchecked")
		final Property<Long> property = Mockito.mock(Property.class);
		Mockito.when(item.getItemProperty(col)).thenReturn(property);
		
		validationUtil.cleanValues(fieldGroup, new TableContainerColumns[] {col});
		
		Mockito.verify(property).setValue(ID);
	}

}
