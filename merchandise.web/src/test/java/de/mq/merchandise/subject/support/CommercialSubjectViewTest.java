package de.mq.merchandise.subject.support;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;

import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;

import de.mq.merchandise.subject.Condition;
import de.mq.merchandise.subject.Subject;
import de.mq.merchandise.support.Mapper;
import de.mq.merchandise.util.ValidationUtil;
import de.mq.merchandise.util.support.RefreshableContainer;
import de.mq.merchandise.util.support.ViewNav;

public class CommercialSubjectViewTest {

	private final CommercialSubjectModel commercialSubjectModel = Mockito.mock(CommercialSubjectModel.class);

	private final UserModel userModel = Mockito.mock(UserModel.class);

	private final MessageSource messageSource = Mockito.mock(MessageSource.class);

	private final ViewNav viewNav = Mockito.mock(ViewNav.class);

	private final MainMenuBarView mainMenuBarView = Mockito.mock(MainMenuBarView.class);

	private final RefreshableContainer lazyQueryContainer = Mockito.mock(RefreshableContainer.class);

	private final Item commercialSubjectSearchItem = Mockito.mock(Item.class);
	private final ValidationUtil validationUtil = Mockito.mock(ValidationUtil.class);

	@SuppressWarnings("unchecked")
	private final Converter<Item, CommercialSubject> itemToCommercialSubjectConverter = Mockito.mock(Converter.class);

	@SuppressWarnings("unchecked")
	private final Converter<CommercialSubject, Item> commercialSubjectToItemConverter = Mockito.mock(Converter.class);

	@SuppressWarnings("unchecked")
	private final Converter<Collection<Subject>, Container> entriesToConatainerConverter = Mockito.mock(Converter.class);

	@SuppressWarnings("unchecked")
	private final Converter<CommercialSubjectItem, Item> commercialSubjectItemConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Item, CommercialSubjectItem> itemToCommercialSubjectItemConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Collection<CommercialSubjectItem>, Container> commercialSubjectItemToContainerConverter = Mockito.mock(Converter.class);
	@SuppressWarnings("unchecked")
	private final Converter<Collection<Condition>, Container> conditionToContainerConverter = Mockito.mock(Converter.class);
	final Item conditionValueItem = Mockito.mock(Item.class);
	@SuppressWarnings("unchecked")
	private final Mapper<Item, CommercialSubjectModel> itemIntoCommercialSubjectModel = Mockito.mock(Mapper.class);

	@SuppressWarnings("unchecked")
	private final Converter<Collection<?>, Container> inputValuesConverter = Mockito.mock(Converter.class);

	private final CommercialSubject commercialSubject = Mockito.mock(CommercialSubject.class);

	private final CommercialSubjectItem commercialSubjectItem = Mockito.mock(CommercialSubjectItem.class);
	private final CommercialSubjectViewImpl view = new CommercialSubjectViewImpl(commercialSubjectModel, userModel, messageSource, viewNav, mainMenuBarView, lazyQueryContainer, commercialSubjectSearchItem, itemToCommercialSubjectConverter, validationUtil, commercialSubjectToItemConverter, entriesToConatainerConverter, commercialSubjectItemConverter, itemToCommercialSubjectItemConverter, commercialSubjectItemToContainerConverter,

	conditionToContainerConverter, conditionValueItem,

	itemIntoCommercialSubjectModel,

	inputValuesConverter);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Before
	public final void setup() {

		final Property<CommercialSubjectCols> commercialSubjectSearchItemPropertyName = Mockito.mock(Property.class);
		Mockito.when(commercialSubjectSearchItem.getItemProperty(CommercialSubjectCols.Name)).thenReturn(commercialSubjectSearchItemPropertyName);

		final Property<CommercialSubjectCols> commercialSubjectSearchItemPropertyItemName = Mockito.mock(Property.class);
		Mockito.when(commercialSubjectSearchItem.getItemProperty(CommercialSubjectCols.ItemName)).thenReturn(commercialSubjectSearchItemPropertyItemName);

		Mockito.when(lazyQueryContainer.getContainerPropertyIds()).thenReturn((Collection) (Arrays.asList(CommercialSubjectCols.Name)));

		Mockito.when(commercialSubjectModel.getCommercialSubject()).thenReturn(Optional.of(commercialSubject));

		Mockito.when(commercialSubjectModel.getCommercialSubjectItem()).thenReturn(Optional.of(commercialSubjectItem));

		final Property<ConditionValueCols> valuePropertyInputValue = Mockito.mock(Property.class);
		Mockito.when(conditionValueItem.getItemProperty(ConditionValueCols.InputValue)).thenReturn(valuePropertyInputValue);
	}

	@Test
	public final void init() {
		view.init();
	}

}
