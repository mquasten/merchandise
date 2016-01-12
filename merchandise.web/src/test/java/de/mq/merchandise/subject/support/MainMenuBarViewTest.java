package de.mq.merchandise.subject.support;

import java.util.HashMap;
import java.util.Locale;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;

import de.mq.merchandise.util.support.ViewNav;
import de.mq.util.event.Observer;

public class MainMenuBarViewTest {

	private final UserModel userModel = Mockito.mock(UserModel.class);
	private final MessageSource messageSource = Mockito.mock(MessageSource.class);
	private final ViewNav viewNav = Mockito.mock(ViewNav.class);

	private final MainMenuBarView mainMenuBarView = new MainMenuBarView(userModel, messageSource, viewNav);
	private final Map<UserModel.EventType, Observer<UserModel.EventType>> observers = new HashMap<>();

	@SuppressWarnings("unchecked")
	@Before
	public final void setup() {
		Mockito.when(userModel.getLocale()).thenReturn(Locale.GERMAN);
		Mockito.doAnswer(i -> {
			observers.put((UserModel.EventType) i.getArguments()[1], (Observer<UserModel.EventType>) i.getArguments()[0]);
			return null;
		}).when(userModel).register(Mockito.any(Observer.class), Mockito.any(UserModel.EventType.class));

		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_MENU_PRODUCT, null, Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_MENU_PRODUCT);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_MENU_PRODUCT_DEFINITION, null, Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_MENU_PRODUCT_DEFINITION);
		Mockito.when(messageSource.getMessage(MainMenuBarView.I18N_MENU_PRODUCT_TEMPLATE, null, Locale.GERMAN)).thenReturn(MainMenuBarView.I18N_MENU_PRODUCT_TEMPLATE);
	}

	@Test
	public final void init() {
		mainMenuBarView.init();
		Assert.assertEquals(Locale.GERMAN, mainMenuBarView.getLocale());
		final MenuBar menuBar = (MenuBar) ReflectionTestUtils.getField(mainMenuBarView, "root");
		Assert.assertEquals(ValoTheme.BUTTON_BORDERLESS, menuBar.getStyleName());
		Assert.assertEquals(1, observers.size());
		Assert.assertEquals(UserModel.EventType.LocaleChanged, observers.keySet().stream().findAny().get());

		observers.values().stream().findAny().get().process(UserModel.EventType.LocaleChanged);
		Assert.assertEquals(1, menuBar.getItems().size());
		final MenuItem item = menuBar.getItems().stream().findFirst().get();
		Assert.assertEquals(MainMenuBarView.I18N_MENU_PRODUCT, item.getText());
		Assert.assertNull(item.getCommand());
		Assert.assertEquals(2, item.getChildren().size());
		Assert.assertEquals(MainMenuBarView.I18N_MENU_PRODUCT_TEMPLATE, item.getChildren().get(0).getText());
		Assert.assertEquals(MainMenuBarView.I18N_MENU_PRODUCT_DEFINITION, item.getChildren().get(1).getText());

		item.getChildren().get(0).getCommand().menuSelected(item.getChildren().get(0));
		item.getChildren().get(1).getCommand().menuSelected(item.getChildren().get(1));
		Mockito.verify(viewNav).navigateTo(SubjectViewImpl.class);

		Mockito.verify(viewNav).navigateTo(CommercialSubjectViewImpl.class);

	}

}
