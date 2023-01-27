package mx.softixx.cis.cloud.agenda.config;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import lombok.val;

@Configuration
public class MessageSourceConfig {
	
	@Bean
    public LocaleResolver localeResolver() {
		val localeResolver = new AcceptHeaderLocaleResolver();
		localeResolver.setSupportedLocales(List.of(new Locale("es", "MX"), new Locale("en", "US")));
		localeResolver.setDefaultLocale(new Locale("es", "MX"));
        return localeResolver;
    }
	
	@Bean
    public MessageSource messageSource() {
        val messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:messages", "classpath:validator-messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return messageSource;
    }
	
}