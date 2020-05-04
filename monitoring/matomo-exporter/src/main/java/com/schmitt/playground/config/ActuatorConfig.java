package com.schmitt.playground.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.schmitt.playground.meterbinder.MatomoStatsProbe;

@Component
public class ActuatorConfig {

    @Bean
    public MatomoStatsProbe dataSourceStatusProbe() {
        return new MatomoStatsProbe();
    }
}