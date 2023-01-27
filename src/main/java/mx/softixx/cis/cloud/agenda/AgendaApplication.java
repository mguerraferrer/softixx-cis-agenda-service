package mx.softixx.cis.cloud.agenda;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.MessageSourceAccessor;

import mx.softixx.cis.common.core.message.MessageUtils;

@SpringBootApplication
@ComponentScan({"mx.softixx.cis.cloud.agenda", "mx.softixx.cis.common.error.advice"})
public class AgendaApplication implements CommandLineRunner {

	private final MessageSourceAccessor messageSourceAccessor;
	
	public AgendaApplication(MessageSourceAccessor messageSourceAccessor) {
		this.messageSourceAccessor = messageSourceAccessor;
	}

	public static void main(String[] args) {
		SpringApplication.run(AgendaApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		MessageUtils.setAccessor(messageSourceAccessor);
	}

}