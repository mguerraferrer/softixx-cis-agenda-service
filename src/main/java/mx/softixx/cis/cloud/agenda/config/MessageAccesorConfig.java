package mx.softixx.cis.cloud.agenda.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

@Configuration
public class MessageAccesorConfig {

	private final MessageSource messageSource;

	public MessageAccesorConfig(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource, LocaleContextHolder.getLocale());
	}

}