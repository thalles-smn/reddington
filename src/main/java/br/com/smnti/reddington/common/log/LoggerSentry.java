package br.com.smnti.reddington.common.log;

import br.com.smnti.reddington.common.util.EnvironmentEnum;
import io.sentry.Sentry;
import io.sentry.event.EventBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;

@Component
public class LoggerSentry {

    @Value("${sentry.dsn}")
    private String sentryDsn;
    @Value("${env}")
    private String environment;

    public LoggerSentry() {
    }

    @PostConstruct
    public void init() {
        if (this.environment.equals(EnvironmentEnum.STAGING.getText()) || this.environment.equals(EnvironmentEnum.PRODUCTION.getText())) {
            Sentry.init(MessageFormat.format("{0}?environment={1}", this.sentryDsn, this.environment));
        }

    }

    public void capture(Throwable e) {
        if (!this.environment.equals(EnvironmentEnum.STAGING.getText()) && !this.environment.equals(EnvironmentEnum.PRODUCTION.getText())) {
            e.printStackTrace();
        } else {
            Sentry.capture(e);
        }

    }

}
