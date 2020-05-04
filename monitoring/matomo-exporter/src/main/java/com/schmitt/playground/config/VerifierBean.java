package com.schmitt.playground.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.env.Environment;

@Configuration
public class VerifierBean implements BeanFactoryPostProcessor, PriorityOrdered {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory)
			throws BeansException {
		final Environment environment = configurableListableBeanFactory.getBean(Environment.class);

		checkEnvironmentVariable(environment, "matomoBaseUrl");
		checkEnvironmentVariable(environment, "matomoReportingApiEndpoint");
		checkEnvironmentVariable(environment, "matomoTrackerApiEndpoint");
		checkEnvironmentVariable(environment, "matomoReportingApiAuthToken");
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	private void checkEnvironmentVariable(Environment environment, String name) {
		if (environment.getProperty(name) == null) {
			throw new ApplicationContextException("Missing property on context bootstrap: " + name);
		} else {
			System.out.println(name + ": " + environment.getProperty(name));
		}
	}
}
