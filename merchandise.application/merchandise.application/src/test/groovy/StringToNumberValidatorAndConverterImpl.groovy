import java.lang.invoke.MethodHandleImpl.BindCaller.T
import java.lang.reflect.Constructor
import java.text.NumberFormat;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.core.convert.converter.Converter;


class StringToNumberValidatorAndConverterImpl implements de.mq.merchandise.rule.Validator<String> , de.mq.merchandise.rule.Converter<String, Number> {

	static final String RESOURCE_KEY = 'StringToNumberValidatorAndConverter.message'

	static final String DEFAULT_MESSAGE = 'Not a valid number for %s'

	def   Class type = Double.class;
	
	@Override
	final String[] parameters() {
		return ['type'].toArray();
	}

	@Override
	final boolean validate(final String value) {
		try {
			convert(value);
			return true;
		} catch (final NumberFormatException ex){
		return false;
		}
		
	}

	@Override
	final String message(final MessageSource messagesource, final Locale locale) {
		return messagesource.getMessage(RESOURCE_KEY,[type.getSimpleName()].toArray() ,String.format(DEFAULT_MESSAGE, type.getSimpleName()) , locale);
	}

	@Override
	final Number convert(final String value) {
		return type.newInstance(value);
		
	}
	
	final void setTypeAsString(final String type){
		this.type=Class.forName(type);
	}

}
