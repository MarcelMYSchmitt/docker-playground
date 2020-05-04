package com.schmitt.playground.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.schmitt.playground.meterbinder.ProducerStatsProbe;

import io.micrometer.core.instrument.MeterRegistry;

@Component
public class ActuatorConfig {

	@Autowired
	private MeterRegistry registry; 
	
    @Bean
    public ProducerStatsProbe dataSourceStatusProbe() {
        return new ProducerStatsProbe(registry);
    }
}
