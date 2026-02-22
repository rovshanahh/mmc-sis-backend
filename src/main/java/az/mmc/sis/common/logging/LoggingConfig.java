package az.mmc.sis.common.logging;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoggingConfig {

    @Bean
    public FilterRegistrationBean<RequestLoggingFilter> requestLoggingFilterRegistration(
            RequestLoggingFilter filter
    ) {
        FilterRegistrationBean<RequestLoggingFilter> reg = new FilterRegistrationBean<>();
        reg.setFilter(filter);
        reg.setOrder(1); // early
        reg.addUrlPatterns("/*");
        return reg;
    }
}
