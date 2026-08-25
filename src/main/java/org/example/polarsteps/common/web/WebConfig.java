package org.example.polarsteps.common.web;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

@Configuration(proxyBeanMethods = false)
public class WebConfig {

	@Bean
	FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilter() {
		FilterRegistrationBean<ShallowEtagHeaderFilter> registration = new FilterRegistrationBean<>(
				new ShallowEtagHeaderFilter());
		registration.addUrlPatterns("/api/*");
		registration.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
		return registration;
	}

}
